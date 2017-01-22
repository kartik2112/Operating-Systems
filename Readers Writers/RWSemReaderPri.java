/**
 *
 * @author Kartik Shenoy
 * https://github.com/kartik2112
 */

import java.util.concurrent.*;
import java.util.*;

class RWSRPReader extends Thread{
    ConstructRWSRP c;
    int ind;

    public RWSRPReader(int i,ConstructRWSRP c) {
        ind=i;
        this.c=c;
        this.setName("Reader"+ind);
    }
    public void run(){
        System.out.println(Thread.currentThread().getName()+" has arrived");
        c.semWait("x");
        c.readcount++;
        if(c.readcount==1){
            c.semWait("wsem");
        }
        c.semSignal("x");
        
        Random r=new Random();
        int time=r.nextInt(2000);
        System.out.println(Thread.currentThread().getName()+" started reading for "+(time+2000));
        try{
            Thread.sleep(2000+time);
        }
        catch(Exception e){
            
        }
        System.out.println(Thread.currentThread().getName()+" finished reading");
        c.semWait("x");
        c.readcount--;
        if(c.readcount==0){
            c.semSignal("wsem");
        }
        c.semSignal("x");
    }
}

class RWSRPWriter extends Thread{
    ConstructRWSRP c;
    int ind;

    public RWSRPWriter(int i,ConstructRWSRP c) {
        ind=i;
        this.c=c;
        this.setName("Writer"+ind);
    }
    public void run(){
        System.out.println(Thread.currentThread().getName()+" has arrived");
        c.semWait("wsem");
            
        Random r=new Random();
        int time=r.nextInt(2000);
        System.out.println(Thread.currentThread().getName()+" started writing for "+(time+2000));
        try{
            Thread.sleep(2000+time);
        }
        catch(Exception e){
            
        }
        System.out.println(Thread.currentThread().getName()+" finished writing");
        
        c.semSignal("wsem");            
        
    }
}

class ConstructRWSRP{
    int readcount=0;
    int wsem=1,x=1;
    Object wsemQ=new Object();
    Semaphore wsemSem,xSem;

    public ConstructRWSRP() {
        wsemSem=new Semaphore(1);
        xSem=new Semaphore(1);
    }
    
    
    void semWait(String var){
        if(var.equals("wsem")){
            try{
                System.out.println(wsemSem.availablePermits());
                wsemSem.acquire();
                wsem--;
                while(wsem<0){
                    System.out.println(Thread.currentThread().getName()+" is waiting");
                    while(wsem<0){
                        try{
                            Thread.sleep(200);
                        }
                        catch(Exception e){}
                    }

                }
            }
            catch(InterruptedException e){}
            finally{
                wsemSem.release();
            }            
        }
        else if(var.equals("x")){
            try{
                xSem.acquire();
                x--;
                while(x<0){
                    System.out.println(Thread.currentThread().getName()+" is waiting");
                    while(x<0){
                        try{
                            Thread.sleep(200);
                        }
                        catch(Exception e){}
                    }

                }
            }
            catch(InterruptedException e){}
            finally{
                xSem.release();
            }
        }
    }
    void semSignal(String var){
        if(var.equals("wsem")){
            wsem++;
        }
        else if(var.equals("x")){
            x++;
        }
    }
}

public class RWSemReaderPri {
    public static void main(String[] args) {
        ConstructRWSRP c=new ConstructRWSRP();
        RWSRPReader r1=new RWSRPReader(1, c);
        RWSRPReader r2=new RWSRPReader(2, c);
        RWSRPReader r3=new RWSRPReader(3, c);
        
        RWSRPWriter w1=new RWSRPWriter(1, c);
        RWSRPWriter w2=new RWSRPWriter(2, c);
        
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
