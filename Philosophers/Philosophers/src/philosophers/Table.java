package philosophers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frederic
 */
public class Table {
    private String[] plates;
    private int platesOnTable=0;
    private ArrayList<Integer>indexes=new ArrayList();
    private Bar bar;
    
    public Table(int numPlates,Bar bar){
        this.bar=bar;
        plates=new String[numPlates];
        for(int i=0;i<plates.length;i++){
            plates[i]="";
        }
    }
    
    public synchronized void addPlate(String plate){
        while(platesOnTable==5){
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for(int i=0;i<plates.length;i++){
            if(plates[i].equals("")){
            plates[i]=plate;
            indexes.add(i);
            i=plates.length;
            }
        }
        platesOnTable++;
        this.notify();
    }
    
    public synchronized String takePlate(){
        String plate="";
        int index;
        
        while(platesOnTable==0&&!bar.isKitchenClose()){
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(!indexes.isEmpty()){
            index=indexes.remove(0);
            plate=plates[index];
            plates[index]="";
            platesOnTable--;
        }
        
        this.notify();
        
        return plate;
    }
    
    public int getPlatesOnTable(){
        return platesOnTable;
    }
    
}
