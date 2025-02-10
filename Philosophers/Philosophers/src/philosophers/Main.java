package philosophers;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;

/*NOTA Importante. Crear fichero Jar. Clic derecho en el proyecto y "Clean and Build".
Dejar el archivo en la misma carpeta que los otros.
Luego, desde consola, dirigirse a la carpeta y lanzar el comando: 
(java -jar "Philosophers.jar")
*/

/**
 *
 * @author Frederic
 */
public class Main {

    public static void main(String[] args) {
        Bar bar=new Bar(Constants.PATH);
        Table table=new Table(Constants.PLATESONTABLE,bar);
        Chopstick[] chopsticks=new Chopstick[Constants.PHILOSOPHERS];
        
        for(int i=0;i<Constants.COOCKERS;i++){
            try {
                Process newCooker=Runtime.getRuntime().exec("java -jar "+"Kitchen.jar "+Constants.PATH+" "+Constants.PLATESTOCOOK);
                System.out.println("Creado el cocinero " +(i+1));  
            } catch (SecurityException ex){
                System.err.println("Ha ocurrido un error de Seguridad."+
                    "No se ha podido crear el proceso por falta de permisos.");
            }catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i=0;i<Constants.SERVERS;i++){
            Server server=new Server(table,bar);
            server.start();
        }
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i=0;i<Constants.PHILOSOPHERS;i++){
            chopsticks[i]=new Chopstick();
        }
        
        for(int i=0;i<Constants.PHILOSOPHERS;i++){
            Philosopher philosopher;
            String name=Philosopher.choiceName();
            
            int leftChopstickID=i;
            int rightChopstickID=i-1;
            if(i==0){
                rightChopstickID=Constants.PHILOSOPHERS-1;
            }
            philosopher=new Philosopher(name,chopsticks[leftChopstickID],chopsticks[rightChopstickID],table);
            philosopher.start();
        }
    }
}
