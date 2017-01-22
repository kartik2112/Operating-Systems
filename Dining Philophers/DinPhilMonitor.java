/**
 *
 * @author Kartik Shenoy
 * https://github.com/kartik2112
 */

import java.util.*;
class DinPhilMonConstruct{
    Object forkCond[]=new Object[6];  //condition vars
    boolean fork[]=new boolean[6];
    //int forkCount[]=new int[6];

    public DinPhilMonConstruct() {
        for(int i=1;i<=5;i++){
            forkCond[i]=new Object();
            fork[i]=true;
            //forkCount[i]=0;
        }
    }
    synchronized void getForks(int i){
            System.out.println(Thread.currentThread().getName()+" is in monitor");
            int left=i;
            int right=i%5+1;
            while(fork[left]==false){
                System.out.println(Thread.currentThread().getName()+" is waiting for availability of left fork");
                //forkCount[left]++;
                synchronized(forkCond[left]){
                    try{
                        forkCond[left].wait();
                    }catch(Exception e){}
                }    
                //forkCount[left]--;
            }
            fork[left]=false;
            System.out.println(Thread.currentThread().getName()+" picks up left fork");
            while(fork[right]==false){
                System.out.println(Thread.currentThread().getName()+" is waiting for availability of right fork");
                //forkCount[right]++;
                synchronized(forkCond[right]){
                    try{
                        forkCond[right].wait();
                    }catch(Exception e){}
                }    
                //forkCount[right]--;
            }
            fork[right]=false;  
            System.out.println(Thread.currentThread().getName()+" picks up right fork");
        
        
    }
    void releaseForks(int i){
            System.out.println(Thread.currentThread().getName()+" is in monitor");
            int left=i;
            int right=i%5+1;
            fork[left]=true;
            System.out.println(Thread.currentThread().getName()+" places left fork back on table");
            synchronized(forkCond[left]){
                forkCond[left].notifyAll();
            }

            fork[right]=true;
            System.out.println(Thread.currentThread().getName()+" places right fork back on table");
            synchronized(forkCond[right]){
                forkCond[right].notifyAll();
            }
        
        
    }
}

class MonPhilosopher extends Thread{
    int ind;
    DinPhilMonConstruct c;

    public MonPhilosopher(int i,DinPhilMonConstruct c1) {
        ind=i;
        c=c1;
        this.setName("Philosopher"+ind);
    }
    public void run(){
        System.out.println(Thread.currentThread().getName()+" is thinking");
        c.getForks(ind);
        System.out.println(Thread.currentThread().getName()+" started eating");
        try{
            Thread.sleep(2000);
        }catch(Exception e){}
        System.out.println(Thread.currentThread().getName()+" stopped eating");
        c.releaseForks(ind);
        
    }
        
}

public class DinPhilMonitor {
    public static void main(String[] args) {
        DinPhilMonConstruct c=new DinPhilMonConstruct();
        for(int i=1;i<=5;i++){
            new MonPhilosopher(i, c).start();
        }
    }
}
