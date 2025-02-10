package philosophers;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frederic
 */
public class Chopstick {
    
    private static int counter=1;
    private Semaphore sem=new Semaphore(1);
    private int id;
    
    
    public Chopstick(){
        id=counter;
        counter++;
    }

    /**
     * @return the sem
     */
    public boolean acquireSem() {
        boolean acquired=false;
        
        try{
            sem.acquire();
            acquired=true;
        }catch (InterruptedException ex) {
            Logger.getLogger(Philosopher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return acquired;
    }
    
    public boolean releaseSem() {
        boolean released=false;
        if(sem.availablePermits()==0){
            sem.release();
            released=true;
        }
        return released;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    
}
