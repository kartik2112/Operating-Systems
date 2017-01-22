/**
 *
 * @author Kartik Shenoy
 * https://github.com/kartik2112
 */

import java.util.concurrent.*;
import java.util.*;

class RWMRPReader extends Thread{
    ConstructRWMRP c;
    int ind;

    public RWMRPReader(int i,ConstructRWMRP c) {
        ind=i;
        this.c=c;
        this.setName("Reader"+ind);
    }
    public void run(){
        System.out.println(Thread.currentThread().getName()+" has arrived");
        c.read();        
    }
}

class RWMRPWriter extends Thread{
    ConstructRWMRP c;
    int ind;

    public RWMRPWriter(int i,ConstructRWMRP c) {
        ind=i;
        this.c=c;
        this.setName("Writer"+ind);
    }
    public void run(){
        System.out.println(Thread.currentThread().getName()+" has arrived");
        c.write();
    }
}

class ConstructRWMRP{
    Object wsemCond=new Object(); //condition variable
    Semaphore mutex=new Semaphore(1);
    int readcount=0,writecount=0;
    int wsem=1;

    void read(){             
        synchronized(this){
            System.out.println(Thread.currentThread().getName()+" enters monitor");   
            readcount++;
            if(readcount==1 && wsem<=0){
                wsem--;
                while(readcount==1 && wsem<=0){
                    System.out.println(Thread.currentThread().getName()+" is blocked on wsem condition variable");
                    synchronized(wsemCond){
                        try{
                            wsemCond.wait();
                        }catch(Exception e){}
                    }                
                }
            }
         
            Random r=new Random();
            int time=r.nextInt(2000);
            System.out.println(Thread.currentThread().getName()+" started reading for "+(time+2000));
            try{
                Thread.sleep(2000+time);
            }
            catch(Exception e){

            }
            System.out.println(Thread.currentThread().getName()+" finished reading");
        
            readcount--;
            if(readcount==0){
                wsem++;
                synchronized(wsemCond){
                    wsemCond.notify();
                }
            }
            
        }
    }
    
    void write(){
              
        synchronized(this){
            System.out.println(Thread.currentThread().getName()+" enters monitor");  
            if(wsem<=0){
                wsem--;
                while(wsem<=0){
                    System.out.println(Thread.currentThread().getName()+" is blocked on wsem condition variable");
                    synchronized(wsemCond){
                        try{
                            wsemCond.wait();
                        }catch(Exception e){}
                    }                
                }
            }
            Random r=new Random();
            int time=r.nextInt(2000);
            System.out.println(Thread.currentThread().getName()+" started writing for "+(time+2000));
            try{
                Thread.sleep(2000+time);
            }
            catch(Exception e){

            }
            System.out.println(Thread.currentThread().getName()+" finished writing");
            synchronized(wsemCond){
                wsemCond.notify();
            }
            
            
        }
        
    }
    
}




public class RWMonRP {
    public static void main(String[] args) {
        ConstructRWMRP c=new ConstructRWMRP();
        RWMRPReader r1=new RWMRPReader(1, c);
        RWMRPReader r2=new RWMRPReader(2, c);
        RWMRPReader r3=new RWMRPReader(3, c);
        
        RWMRPWriter w1=new RWMRPWriter(1, c);
        RWMRPWriter w2=new RWMRPWriter(2, c);
        
        r1.start();
        try{
            Thread.sleep(1000);
        }catch(Exception e){}
        
        w1.start();
        try{
            Thread.sleep(1000);
        }catch(Exception e){}
        
        r2.start();
        try{
            Thread.sleep(1000);
        }catch(Exception e){}
        
        w2.start();
        try{
            Thread.sleep(1000);
        }catch(Exception e){}
        
        r3.start();
        try{
            Thread.sleep(1000);
        }catch(Exception e){}
    }
}
