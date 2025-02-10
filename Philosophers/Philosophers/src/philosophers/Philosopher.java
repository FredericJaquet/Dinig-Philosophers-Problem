package philosophers;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;

/**
 *
 * @author Frederic
 */
public class Philosopher extends Thread {
    public static final int CHATTING=0;
    public static final int WAITING=1;
    public static final int EATING=2;
    
    private static int counter=1;
    private static ArrayList<String> names=new ArrayList();
    
    private boolean thereIsFood=true;
    private String name;
    private int id;
    private int state;
    private int minDelay,maxDelay;
    private int timeChatting=0,timeEating=0,timeWaiting=0,hasEated=0;
    private Chopstick left,right;
    private Table table;
    
    public Philosopher(String name,Chopstick left,Chopstick right,Table table){
        this.name=name;
        this.left=left;
        this.right=right;
        this.table=table;
        id=counter;
        minDelay=6000-((id*1000)/2);
        maxDelay=6000+((id*1000)/2);
        counter++;
    }
    
    @Override
    public void run(){
        while(thereIsFood){
            chatting();
        }
    }
    
    private void chatting(){
        Random random=new Random();
        int delay=random.nextInt(minDelay,maxDelay+1);
        
        state=CHATTING;
        System.out.println("El Filósofo "+name+" está charlando");
        
        //The philosopher is chatting for a random time, then he tries to eat.
        try {
            Thread.sleep(delay);
            timeChatting+=delay;
        } catch (InterruptedException ex) {
            Logger.getLogger(Philosopher.class.getName()).log(Level.SEVERE, null, ex);
        }

        eating();
    }
    
    private void eating(){
        Random random=new Random();
        int delay=random.nextInt(minDelay,maxDelay+1);
        int hunger=1;
        long waitingTime=System.nanoTime();
        String plate;
        
        state=WAITING;
        System.out.println("El Filósofo "+name+" tiene hambre. Espera para comer");
        this.setPriority(NORM_PRIORITY);
        
        do{
            if(left.acquireSem()&&right.acquireSem()){
                plate=table.takePlate();
                if(!plate.equals("")){
                    state=EATING;
                    waitingTime=(System.nanoTime()-waitingTime)/1_000_000;
                    timeWaiting+=waitingTime;
                    System.out.println("El Filósofo "+name+" está comiendo "+plate+" usando los palillos "+left.getId()+" y "+right.getId());
                }else{
                    thereIsFood=false;
                }
            }if(state==EATING){
                try {
                    Thread.sleep(delay);
                    timeEating+=delay;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Philosopher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                left.releaseSem();
                right.releaseSem();
                hunger++;
            }
        }while(state!=EATING&&thereIsFood);
        
        stopEating();
    }
    
    private void stopEating(){
        left.releaseSem();
        right.releaseSem();
        state=CHATTING;
        if(state==EATING){
            System.out.println("El Filósofo "+name+" ha dejado de comer. Libera los palillos "+left.getId()+" y "+right.getId());
        }
    
        if(!thereIsFood){
            System.out.println("Se ha acabado la comida, el Filósofo "+name+" se retira.\n"
                    + "\tHa estado "+timeChatting/1000+ " segundos charlando; "+timeEating/1000+" segundos comiendo y; "+timeWaiting/1000+" segundos esperando");
        }
    }
    
    public static String choiceName(){
        Random ran=new Random();
        String name;
        boolean nameExists;
            
        do{
            nameExists=false;
            name=Constants.NAMES[ran.nextInt(Constants.NAMES.length)];
            for(int j=0;j<names.size();j++){
                if(name.equals(names.get(j))){
                    nameExists=true;
                }
            }
        }while(nameExists);
        
        names.add(name);
        
        return name;
    }
    
}

