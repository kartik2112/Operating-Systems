/**
 *
 * @author Kartik Shenoy
 * https://github.com/kartik2112
 */
import java.util.*;
import java.util.concurrent.Semaphore;

class Prod extends Thread{
    Mon m;
    Prod(Mon m){
        this.m=m;
        this.setName("Producer");
    }
    public void run(){
        Random r=new Random();
        for(int i=0;i<20;i++){
            int putN=r.nextInt(100);
            m.semWait("e");
            m.semWait("sem");
            m.append(putN);
            try{
                Thread.sleep(200);
            }
            catch(InterruptedException e){
                System.out.println("Exc caught");
            }
            System.out.println("Producer added "+putN+" successfully");
            m.semSignal("sem");
            m.semSignal("n");
            try{
                Thread.sleep(200);
            }
            catch(InterruptedException e){
                System.out.println("Exc caught");
            }
        }
    }
}

class Cons extends Thread{
    Mon m;
    Cons(Mon m){
        this.m=m;
        this.setName("Consumer");
    }
    public void run(){
        Random r=new Random();
        for(int i=0;i<20;i++){
            m.semWait("n");
            m.semWait("sem");
            int getN=m.take();            
            System.out.println("Consumer got "+getN+" successfully");
            try{
                Thread.sleep(200);
            }
            catch(InterruptedException e){
                System.out.println("Exc caught");
            }
            m.semSignal("sem");
            m.semSignal("e");
            try{
                Thread.sleep(200);
            }
            catch(InterruptedException e){
                System.out.println("Exc caught");
            }
        }
    }
}

class Mon{
    private int sem,e,n;
    Queue<Integer> buffer;
    Semaphore forSem;
    Mon(int buffSize){
        sem=1;
        e=buffSize;
        n=0;
        buffer=new LinkedList();
        forSem=new Semaphore(1);
    }
    
    void semWait(String var){
        if(var.equals("e")){
            e--;
            if(e<0){
                System.out.println("This thread is blocked coz buffer is full");
                while(e<0){
                    try{
                        Thread.sleep(200);
                    }
                    catch(InterruptedException e){
                        System.out.println("Exc caught");
                    }
                    
                }
            }
        }
        else if(var.equals("n")){
            n--;
            if(n<0){
                System.out.println("This thread is blocked coz buffer is empty");
                while(n<0){
                    try{
                        Thread.sleep(200);
                    }
                    catch(InterruptedException e){
                        System.out.println("Exc caught");
                    }
                    
                }
            }
        }
        else if(var.equals("sem")){
            try{
                
                forSem.acquire();
            }
            catch(Exception e){}
            
            sem--;
            if(sem<0){
                System.out.println("This thread "+Thread.currentThread().getName()+" is blocked some other thread is accessing buffer");
                while(sem<0){
                    try{
                        Thread.sleep(200);
                    }
                    catch(InterruptedException e){
                        System.out.println("Exc caught");
                    }
                    
                }
            }
            try{
                forSem.release();
            }
            catch(Exception e){}
        }
    }
    
    void semSignal(String var){
        if(var.equals("e")){
            e++;
        }
        else if(var.equals("n")){
            n++;
        }
        else if(var.equals("sem")){
            sem++;
        }
    }
    
    void append(int n){
        buffer.add(n);
    }
    int take(){
        if(buffer.size()!=0){
            return buffer.remove();
        }
        else{
            return -1;
        }        
    }
}

public class ProdConsumSem {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter buffer size:");
        Mon m=new Mon(sc.nextInt());
        new Cons(m).start();
        new Prod(m).start();
        
    }
}
