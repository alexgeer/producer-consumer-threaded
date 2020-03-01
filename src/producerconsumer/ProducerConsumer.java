package producerconsumer;


import java.lang.Thread;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Alexander Geer
 */
public class ProducerConsumer {
    public int[] arr;
    public int nElem;
    static final int SIZE = 32;
    int times;
    static final int MAXTIMES = 250;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ProducerConsumer pc = new ProducerConsumer();
        System.out.println("Alexander Geer\nProgramming Assignment 1\nOperating Systems Fall '17");
        System.out.println("Problem 1: A Producer/Consumer solution using threading in Java");
        System.out.println("Array size is " + pc.arr.length);
        //make producer and thread
        Producer p = new Producer(pc);
        Thread pThd = new Thread(p);
        
        //make consumer and thread
        Consumer c = new Consumer(pc);
        Thread cThd = new Thread(c);
        
        //start threads
        pThd.start();
        cThd.start();

    }
    
    public ProducerConsumer(){
        arr = new int[SIZE];
        nElem = 0;
        times = 0;
    }
}

class Producer implements Runnable {
    int i;
    
    ProducerConsumer pc;
    
    public Producer(ProducerConsumer pc){
        i = 0;
        this.pc = pc;
    }
    
    @Override
    public void run(){
        while(true){
            try{
                //if full, wait
                synchronized(pc){
                    while(pc.nElem == pc.arr.length){
                        System.out.println("Array is full, producer will wait --> number of elements: " + pc.nElem);
                        pc.wait();
                    }//while
                }//synch
                
                //not full, so we can produce
                synchronized(pc){
                    if(pc.arr[i%pc.SIZE] == 0){
                        pc.arr[i%pc.SIZE] = 1;
                        System.out.println("Producer set array[ " + (i%pc.SIZE) + " ] to FULL");
                        i++;
                        pc.nElem++;
                    }
                    pc.notify();
                }//synch
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}

class Consumer implements Runnable{
    int[] arr;
    int i = 0;
    
    ProducerConsumer pc;
    
    public Consumer(ProducerConsumer pc){
      
        this.pc = pc;
    }
    
    @Override
    public void run(){
        while(true){
            try{
                
                //if full, wait
            synchronized(pc){  
                while(pc.nElem == 0){
                      
                        System.out.println("Array is empty, consumer will wait --> number of elements: " + pc.nElem);
                        pc.wait();
                    }//while
                }//synch
                
                //consume
                synchronized(pc){
                    if(pc.arr[i%pc.SIZE] == 1){
                        pc.arr[i%pc.SIZE] = 0;
                        System.out.println("Consumer set array[ " + (i%pc.SIZE) + " ] to EMPTY");
                        i++;
                        pc.nElem--;
                    }
                   
                    pc.notify();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
