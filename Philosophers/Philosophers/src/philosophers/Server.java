package philosophers;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frederic
 */
public class Server extends Thread {
    private static int counter=0;
    
    private int id;
    private String path;
    private Table table;
    private Bar bar;
    
    public Server(Table table, Bar bar){
        this.table=table;
        this.bar=bar;
        counter++;
        this.id=counter;
    }
    
    @Override
    public void run(){
        while(!bar.isKitchenClose()){
            serve();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void serve(){
        String plate;
        
        plate=bar.takePlateFromKitchen();
        if(!plate.equals("")){
            table.addPlate(plate);
            System.out.println("El camarero Nº"+id+" ha servido "+plate+" en la mesa, hay "+table.getPlatesOnTable()+" platos en la mesa");
        }
    }
    
    public int getNumber(){
        return id;
    }
    
}

