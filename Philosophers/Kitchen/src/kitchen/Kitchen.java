package kitchen;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Random;

/*NOTA Importante. Crear fichero Jar. Clic derecho en el proyecto y "Clean and Build".
Dejar el archivo en la misma carpeta que los otros.
*/

/**
 *
 * @author Frederic
 */

public class Kitchen {

    private static final String[] PLATES={"Paella", "Ceviche", "Risotto", "Sushi", "Tacos", "Empanada", "Gazpacho", "Burrito", "Lasagna", "Arepa", "Tamal", "Moussaka", "Croqueta", "Tortilla", "Falafel", "Shawarma", "Tempura", "Goulash", "Samosa", "Padthai", "Curry", "Bibimbap", "Quiche", "Satay", "Tapenade", "Polenta", "Ratatouille", "Bruschetta", "Carpaccio", "Fondue", "Bouillabaisse", "Stroganoff", "Ramen", "Udon", "Kimchi", "Poutine", "Schnitzel", "Kebab", "Couscous", "Manchurian", "Rendang", "Tagine", "Soba", "Churro", "Mole", "Jambalaya", "Hummus", "Okonomiyaki", "Pozole", "Cassoulet"};

    public static void main(String[] args) {
        String path;
        int platesToCook;
        File file;
        
        path=getPath(args);
        platesToCook=getPlatesToCook(args);
        
        startCooking(path, platesToCook);
    }
    
    public static String getPath(String[] args){
        String fileName="nuevo.txt";//Default value
        String osName = System.getProperty("os.name");//The name of the OS
        File file=null;
                
        if(args.length>0){
            fileName=args[0];
        }else{
            System.out.println("No se ha indicado ningun nombre, se tomará un valor por defecto (nuevo.txt)");
        }
        
        if(osName.toUpperCase().contains("WIN")){//If windows
            fileName=fileName.replace("\\", "\\\\");
        }
        
        file=new File(fileName);
        if(file.exists()){//Delete the file if exists.
            file.delete();
        }
        
        return fileName;
    }
    
    private static int getPlatesToCook(String[] args){
        int wordsSize=10;//Default value
        
        if(args.length>1){
            wordsSize=Integer.parseInt(args[1]);
        }else{
            System.out.println("No se ha indicado el número de platos a cocinar, se tomará un valor por defecto (10)");
        }
        
        return wordsSize;
    }
    
    public static void startCooking(String path,int platesToCook){
        File file=new File(path);
        RandomAccessFile raf=null;
        FileLock lock=null;
        String plate;
        
        for(int i=0;i<platesToCook;i++){
            plate=choicePlate();
            try{
                raf=new RandomAccessFile(file,"rwd");
                //***************
                //Critical Section
                lock=raf.getChannel().lock();//We lock the access channel to the file
                raf.seek(raf.length());//We go to the end of the file
                raf.writeChars(plate);//We write the current plate
                lock.release();//We unlock the access channel to the file
                lock=null;
                //End of Critical Section
                //*******************
                System.out.println("Un cocinero ha preparado "+plate);
            }catch(IOException e){
                System.err.println("Error al acceder al fichero");
                System.err.println(e.toString());
            }finally{
                try{
                    if(raf!=null) raf.close();
                    if(lock!=null) lock.release();
                    Thread.sleep(50);
                }catch (IOException | InterruptedException e2){
                    System.err.println(" Error al cerrar el fichero");
                    System.err.println(e2.toString());
                    System.exit(1);//If error, we stop the app
                }
            }
        } 
    }
    
    public static String choicePlate(){
        Random ran=new Random();
        int i=ran.nextInt(PLATES.length);
        
        return PLATES[i]+"\n";
    }
}
