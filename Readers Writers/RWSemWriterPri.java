/**
 *
 * @author Kartik Shenoy
 * https://github.com/kartik2112
 */

import java.util.concurrent.*;
import java.util.*;
class RWSWPReader extends Thread{
    ConstructRWSWP c;
    int ind;

    public RWSWPReader(int i,ConstructRWSWP c) {
        this.c=c;
        ind=i;
        this.setName("Reader"+ind);
    }
    public void run(){
        System.out.println(Thread.currentThread().getName()+" has arrived");
        c.semWait("z");
        c.semWait("rsem");
        c.semWait("x");
        c.readcount++;
        if(c.readcount==1){
            c.semWait("wsem");
        }
        c.semSignal("x");
        c.semSignal("rsem");
        c.semSignal("z");
        Random r=new Random();
        int time=r.nextInt(3000);
        System.out.println(Thread.currentThread().getName()+" has started reading (time="+(time+2000)+" )");
        try{
            Thread.sleep(2000+time);
        }catch(Exception e){}
        System.out.println(Thread.currentThread().getName()+" has stopped reading");
        
        c.semWait("x");
        c.readcount--;
        if(c.readcount==0){
            c.semSignal("wsem");
        }
        c.semSignal("x");
    }
}

class RWSWPWriter extends Thread{
    ConstructRWSWP c;
    int ind;

    public RWSWPWriter(int i,ConstructRWSWP c) {
        this.c=c;
        ind=i;
        this.setName("Writer"+ind);
    }
    public void run(){
        System.out.println(Thread.currentThread().getName()+" has arrived");
        c.semWait("y");
        c.writecount++;
        if(c.writecount==1){
            c.semWait("rsem");
        }
        c.semSignal("y");
        
        c.semWait("wsem");
        Random r=new Random();
        int time=r.nextInt(3000);
        System.out.println(Thread.currentThread().getName()+" has started writing (time="+(time+2000)+" )");
        try{
            Thread.sleep(2000+time);
        }catch(Exception e){}
        System.out.println(Thread.currentThread().getName()+" has stopped writing");
        c.semSignal("wsem");
        
        c.semWait("y");
        c.writecount--;
        if(c.writecount==0){
            c.semSignal("rsem");
        }
        c.semSignal("y");
    }
}

class ConstructRWSWP{
    int readcount,writecount;
    int x,y,z,wsem,rsem;
    Semaphore xSem,ySem,zSem,wsemSem,rsemSem;

    public ConstructRWSWP() {
        readcount=writecount=0;
        x=y=z=wsem=rsem=1;
        xSem=new Semaphore(1);
        ySem=new Semaphore(1);
        zSem=new Semaphore(1);
        wsemSem=new Semaphore(1);
        rsemSem=new Semaphore(1);
    }
    
    void semWait(String var){
        if(var.equals("x")){
            try{
                xSem.acquire();
                x--;
                if(x<0){
                    System.out.println(Thread.currentThread().getName()+" is blocked on x");
                    while(x<0){
                        try{
                            Thread.sleep(200);
                        }
                        catch(Exception e){}                            
                    }
                }
            }
            catch(Exception e){}
            finally{
                xSem.release();
            }
        }
        else if(var.equals("y")){
            try{
                ySem.acquire();
                y--;
                if(y<0){
                    System.out.println(Thread.currentThread().getName()+" is blocked on y");
                    while(y<0){
                        try{
                            Thread.sleep(200);
                        }
                        catch(Exception e){}                            
                    }
                }
            }
            catch(Exception e){}
            finally{
                ySem.release();
            }
        }
        else if(var.equals("z")){
            try{
                zSem.acquire();
                z--;
                if(z<0){
                    System.out.println(Thread.currentThread().getName()+" is blocked on z");
                    while(z<0){
                        try{
                            Thread.sleep(200);
                        }
                        catch(Exception e){}                            
                    }
                }
            }
            catch(Exception e){}
            finally{
                zSem.release();
            }
        }
        else if(var.equals("rsem")){
            try{
                rsemSem.acquire();
                rsem--;
                if(rsem<0){
                    System.out.println(Thread.currentThread().getName()+" is blocked on rsem");
                    while(rsem<0){
                        try{
                            Thread.sleep(200);
                        }
                        catch(Exception e){}                            
                    }
                }
            }
            catch(Exception e){}
            finally{
                rsemSem.release();
            }
        }
        else if(var.equals("wsem")){
            try{
                wsemSem.acquire();
                wsem--;
                if(wsem<0){
                    System.out.println(Thread.currentThread().getName()+" is blocked on wsem");
                    while(wsem<0){
                        try{
                            Thread.sleep(200);
                        }
                        catch(Exception e){}                            
                    }
                }
            }
            catch(Exception e){}
            finally{
                wsemSem.release();
            }
        }
        
    }
    
    
    void semSignal(String var){
        if(var.equals("x")){
            x++;
        }
        else if(var.equals("y")){
            y++;
        }
        else if(var.equals("z")){
            z++;
        }
        else if(var.equals("rsem")){
            rsem++;
        }
        else if(var.equals("wsem")){
            wsem++;
        }
    }
}

public class RWSemWriterPri {
    public static void main(String[] args) {
        ConstructRWSWP c=new ConstructRWSWP();
        RWSWPReader r1=new RWSWPReader(1, c);
        RWSWPReader r2=new RWSWPReader(2, c);
        RWSWPReader r3=new RWSWPReader(3, c);
        
        RWSWPWriter w1=new RWSWPWriter(1, c);
        RWSWPWriter w2=new RWSWPWriter(2, c);
        
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
