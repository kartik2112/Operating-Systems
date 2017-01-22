/**
 *
 * @author Kartik Shenoy
 * https://github.com/kartik2112
 */

import java.util.*;
import java.util.concurrent.*;

class DinPhilSemConstructor{
    Semaphore forksemSem[]=new Semaphore[6];
    int forksem[]=new int[6];
    Semaphore roomSem=new Semaphore(1);
    int room=4;

    public DinPhilSemConstructor() {
        for(int i=1;i<=5;i++){
            forksemSem[i]=new Semaphore(1);
            forksem[i]=1;
        }
    }
    
    void semWaitFork(int forkNo){
        try{
            System.out.println(forksemSem[forkNo].availablePermits());
            forksemSem[forkNo].acquire();
            forksem[forkNo]--;
            if(forksem[forkNo]<0){
                System.out.println(Thread.currentThread().getName()+" is waiting for availability of fork "+forkNo);
                while(forksem[forkNo]<0){
                    try{
                        Thread.sleep(200);
                    }catch(Exception e){}
                }
            }
        }
        catch(Exception e){}
        finally{
            forksemSem[forkNo].release();
        }  
    }
    
    void semSignalFork(int i){
        forksem[i]++;
    }
    
    void semWaitRoom(){
        try{
            roomSem.acquire();
            room--;
            if(room<0){
                System.out.println(Thread.currentThread().getName()+" is waiting outside room since 4 people already present in room");
                while(room<0){
                    try{
                        Thread.sleep(200);
                    }catch(Exception e){}
                }
            }
        }catch(Exception e){}
        finally{
            roomSem.release();
        }
    }
    
    void semSignalRoom(){
        room++;
    }
        
}

class Philosopher extends Thread{
    int no;
    DinPhilSemConstructor c;

    public Philosopher(int i,DinPhilSemConstructor c1) {
        no=i;
        c=c1;
        this.setName("Philosopher"+no);
    }
    
    public void run(){
        System.out.println(Thread.currentThread().getName()+" is thinking");
        System.out.println(Thread.currentThread().getName()+" wants to eat");
        c.semWaitRoom();
        System.out.println(Thread.currentThread().getName()+" enters room");
        System.out.println(Thread.currentThread().getName()+" tries to pickup left fork");
        c.semWaitFork(no);
        System.out.println(Thread.currentThread().getName()+" picks up left fork");
        System.out.println(Thread.currentThread().getName()+" tries to pick up right fork");
        c.semWaitFork(no%5+1);
        System.out.println(Thread.currentThread().getName()+" picks up right fork");
        System.out.println(Thread.currentThread().getName()+" starts eating");
        try{
            Thread.sleep(2000);
        }
        catch(Exception e){}
        System.out.println(Thread.currentThread().getName()+" finishes eating");
        c.semSignalFork((no+1)%5);
        c.semSignalFork(no);
        System.out.println(Thread.currentThread().getName()+" places right fork back on table");
        c.semSignalRoom();
        System.out.println(Thread.currentThread().getName()+" places left fork back on table");
        System.out.println(Thread.currentThread().getName()+" leaves room");
        
    }
    
}

public class DinPhilSem {
    public static void main(String[] args) {
        DinPhilSemConstructor c=new DinPhilSemConstructor();
        for(int i=1;i<=5;i++){
            new Philosopher(i, c).start();
        }
    }
}
