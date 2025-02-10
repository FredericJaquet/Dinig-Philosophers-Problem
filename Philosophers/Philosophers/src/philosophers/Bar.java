
package philosophers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frederic
 */
public class Bar {
    
    private String path;
    private boolean kitchenIsClose=false;
    
    public Bar(String path){
        this.path=path;
    }
    
    public synchronized String takePlateFromKitchen(){
        String line;
        String firstPlate="";
        ArrayList<String> platesFromKitchen=new ArrayList();
        File file=new File(path);
        RandomAccessFile raf=null;
        FileChannel channel=null;
        FileLock lock=null;
        
        try {
            raf=new RandomAccessFile(file,"rwd");//Open the file
            channel=raf.getChannel();

            //***************
            //Critical Section
            while(lock==null){
                try{
                    lock=channel.lock();
                }catch(IOException  e){
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Bar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }   

            while((line=raf.readLine())!=null){
                platesFromKitchen.add(line);
            }

            if (platesFromKitchen.isEmpty()) {
                System.out.println("La cocina ha cerrado");
                kitchenIsClose=true;
            }else{
                firstPlate=platesFromKitchen.remove(0);
                
                raf.setLength(0);
                for(int i=0;i<platesFromKitchen.size();i++){
                    raf.writeBytes(platesFromKitchen.get(i)+"\n");
                }
            }
            lock.release();//We unlock the access channel to the file
            lock=null;
            this.notify();
            //*******************
            //End of Critical Section
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try{
                if(raf!=null) raf.close();
                if(lock!=null) lock.release();
            }catch (IOException e2){
                System.err.println("Error al cerrar el fichero");
                System.err.println(e2.toString());
                System.exit(1);//If error, we stop the app
            }
        }
        
        return firstPlate;
    }
    
    public boolean isKitchenClose(){
        return kitchenIsClose;
    }
    
    public void setKitchenIsClose(boolean kitchenIsClose){
        this.kitchenIsClose=kitchenIsClose;
    }
    
}
