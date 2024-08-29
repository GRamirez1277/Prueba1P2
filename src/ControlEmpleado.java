import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ControlEmpleado{
    public static void main(String[] args) {
        Scanner leer=new Scanner(System.in);
        EmpleadoManager em=new EmpleadoManager();
        int opcion=0;
        
        while (opcion!=5) {
            System.out.println("**** MENU ****");
            System.out.println("1- Agregar Empleado");
            System.out.println("2- Listar Empleado No Despedidos");
            System.out.println("3- Despedir Empleado");
            System.out.println("4- Buscar Empleado Activo");
            System.out.println("5- Salir");
            System.out.print("Seleccione una opción: ");
            try {
                opcion=leer.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Opción no válida. Ingresar solo las opciones disponibles");
                leer.next();
                continue;
            }

            if(opcion==1){
                agregarEmpleado(em,leer);
            }else if(opcion==2){
                listarEmpleadosNoDespedidos(em);
            }else if(opcion==3){
                despedirEmpleado(em, leer);
            }else if(opcion==4){
                buscarEmpleadoActivo(em, leer);
            }else if(opcion==5){
                System.exit(0);
            } else {
                System.out.println("Opción no válida. Ingresar solo las opciones disponibles");
            }
        }
        leer.close();
    }

    private static void agregarEmpleado(EmpleadoManager em, Scanner leer) {
        leer.nextLine();
        try {
            System.out.print("Ingrese el nombre del empleado: ");
            String nombre=leer.nextLine();
            System.out.print("Ingrese el salario del empleado: ");
            double salario=leer.nextDouble();
            em.addEmployee(nombre, salario);
            System.out.println("El empleado ha sido agregado");
        } catch (InputMismatchException e) {
            System.out.println("No se pudo registrar ese empleado debido a un error");
            leer.next();
        } catch (IOException e) {
            System.out.println("No se pudo agregar al empleado debido a un error");
        }
    }

    private static void listarEmpleadosNoDespedidos(EmpleadoManager em) {
        try {
            em.imprimirEmpleados();
        } catch (IOException e) {
            System.out.println("No se pudo imprimir la lista de empleados");
        }
    }

    private static void despedirEmpleado(EmpleadoManager em,Scanner leer){
        try{
            System.out.print("Ingrese el código del empleado que desea despedir: ");
            int codigo=leer.nextInt();
            if(em.despedirEmpleado(codigo)) {
                System.out.println("Se despidió al empleado");
            }else{
                System.out.println("Ese empleado no está activo");
            }
        }catch(InputMismatchException e){
            System.out.println("El código debe ser un número");
            leer.next();
        } catch (IOException e){
            System.out.println("No se pudo despedir al empleado");
        }
    }

    private static void buscarEmpleadoActivo(EmpleadoManager em,Scanner leer){
    try {
        System.out.print("Ingrese el código del empleado que desea buscar: ");
        int codigo = leer.nextInt();
        String info=em.infoEmpleado(codigo);
        System.out.println(info);
    }catch(InputMismatchException e){
        System.out.println("El código debe ser un número");
        leer.next();
    }catch (IOException e){
        System.out.println("No se pudo encontrar al empleado");
        }
    }
}
