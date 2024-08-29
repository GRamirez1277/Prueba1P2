import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoManager {
    private RandomAccessFile rcods,remps;
    public EmpleadoManager(){
        try{
            //1- Asegurar que el folder de Company exista
            File mf=new File("company");
            mf.mkdir();
            //2- Instanciar los archivos RAFs dentro dell folder company
            rcods=new RandomAccessFile("company/codigos.emp","rw");
            remps=new RandomAccessFile("company/empleados.emp","rw");
            //3- Inicializar el archivo de codigos, si, es nuevo.
            initCodes();
        }catch(IOException e){
            System.out.println("Error");
        }
    }
    /*
    Formato Codigos.emp
    int code;
    */
    private void initCodes() throws IOException{
        //Cotejar el tamaño del archivo
        if(rcods.length()==0)
            rcods.writeInt(1);
    }
    
    /*
    Crear la función getCode, para generar
    el código siguiente e indicarme cual es
    el código actual.
    */
    
    private int getCode() throws IOException{
        //Leer el archivo
        //Puntero
        rcods.seek(0);
        int codigo=rcods.readInt();
        rcods.seek(0);
        rcods.writeInt(codigo+1);
        return codigo;
    }
    
    /*
    Formato Empleados.emp
    
    int code
    String name
    double salary
    long fechaContratacion
    long fechaDespido
    
    */
    public void addEmployee(String name,double monto) throws IOException{
        remps.seek(remps.length());
        //Code
        int code=getCode();
        remps.writeInt(code);
        //Nombre
        remps.writeUTF(name);
        //Salario
        remps.writeDouble(monto);
        //Fecha Contratación
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        //Fecha Despido
        remps.writeLong(0);
        //Crear carpeta y archivo individual de cada empleado.
        createEmployeeFolders(code);
    }
    
    private String employeeFolder(int code){
        return "company/empleado"+code;
    }
    
    private void createEmployeeFolders(int code)throws IOException{
        File edir=new File(employeeFolder(code));
        edir.mkdir();
        //Crear archivo del empleado
        this.createYearSalesFileFor(code);
    }
    
    private RandomAccessFile salesFileFor(int code)throws IOException{
        String dirPadre=employeeFolder(code);
        int yearActual=Calendar.getInstance().get(Calendar.YEAR);
        String path=dirPadre+"/ventas"+yearActual+".emp";
        
        return new RandomAccessFile(path,"rw");
    }
    /*
    Formato VentasYear.emp
    double ventas 
    boolean estadoPagar
    */
    private void createYearSalesFileFor(int code) throws IOException
    {
        RandomAccessFile raf=salesFileFor(code);
       if(raf.length()==0){
           for(int mes=0;mes<12;mes++){
               raf.writeDouble(0);
               raf.writeBoolean(false);
            }
        }
    }
    
    public void imprimirEmpleados()throws IOException{
        SimpleDateFormat formatoFecha=new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("**** LISTA DE EMPLEADOS ****");
        remps.seek(0);
        while(remps.getFilePointer()<remps.length()){
            int codigo;
            codigo=remps.readInt();
            String nombre;
            nombre=remps.readUTF();
            double salario;
            salario=remps.readDouble();
            long fechaContratacion;
            fechaContratacion=remps.readLong();
            long fechaDespido;
            fechaDespido=remps.readLong();
            if(fechaDespido==0){
                String fecha=formatoFecha.format(fechaContratacion);
                System.out.println("Código: "+codigo+"\nNombre: "+nombre+"\nSalario: Lps. "+ salario+"\nContratado el "+fecha);
            }
        }
    }
    
    public boolean empleadoActivo(int codigo) throws IOException{
        remps.seek(0);
        while(remps.getFilePointer()<remps.length()){
            int codigoEmpleado;
            codigoEmpleado=remps.readInt();
            remps.readUTF(); 
            remps.readDouble();
            remps.readLong();
            long fechaDespido;
            fechaDespido=remps.readLong();
            if(codigoEmpleado==codigo&&fechaDespido==0){
                return true;
            }
        }
        return false;
    }
    
    public String infoEmpleado(int codigo) throws IOException{
        SimpleDateFormat formatoFecha=new SimpleDateFormat("dd/MM/yyyy");
        remps.seek(0);
        while (remps.getFilePointer()<remps.length()){
            int codigoEmpleado=remps.readInt();
            String nombre=remps.readUTF();
            double salario=remps.readDouble();
            long fechaContratacion=remps.readLong();
            long fechaDespido=remps.readLong();

            if(codigoEmpleado==codigo){
                if(fechaDespido==0){
                    String fecha;
                    fecha=formatoFecha.format(new Date(fechaContratacion));
                    return "Código: "+codigoEmpleado+"\nNombre: "+nombre+"\nSalario: Lps. "+salario+"\nContratado el: "+fecha+"\nEstatus: Activo";
                } else {
                    return "El empleado con código "+codigo+" no está activo";
                }
            }
        }
        return "El empleado con código "+codigo+" no existe";
    }
    
    public boolean despedirEmpleado(int codigo) throws IOException{
        remps.seek(0);
        while(remps.getFilePointer()<remps.length()){
            int codigoEmpleado;
            codigoEmpleado=remps.readInt();
            String nombre;
            nombre=remps.readUTF();
            remps.readDouble();
            remps.readLong();
            long punteroDespido;
            punteroDespido=remps.getFilePointer();
            long fechaDespido;
            fechaDespido=remps.readLong();
            if(codigoEmpleado==codigo&&fechaDespido==0){
                remps.seek(punteroDespido);
                remps.writeLong(Calendar.getInstance().getTimeInMillis());
                System.out.println("Se despidió a: "+nombre);
                return true;
            }
        }
        return false;
    }
}
