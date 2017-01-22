/**
 *
 * @author Kartik Shenoy
 * https://github.com/kartik2112
 */

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author VPS
 */

class ProdMon extends Thread{
    RealMonitor m;
    ProdMon(RealMonitor m){
        this.m=m;
    }
    public void run(){
        Random r=new Random();
        for(int i=0;i<20;i++){
            int putNo=r.nextInt(100);
            m.append(putNo);
            try{
                Thread.sleep(200);
            }
            catch(InterruptedException e){
                
            }
        }
    }
}

class ConsMon extends Thread{
    RealMonitor m;
    ConsMon(RealMonitor m){
        this.m=m;
    }
    public void run(){
        Random r=new Random();
        for(int i=0;i<20;i++){
            
            int out=m.take();
            try{
                Thread.sleep(900);
            }
            catch(InterruptedException e){
                
            }
            System.out.println(out+" consumed");
        }
    }
}

class RealMonitor{
    boolean notFull=true;
    boolean notEmpty=false;
    Queue<Integer> buffer;
    int bufferSize;
    
    Object notFullO=new Object();
    Object notEmptyO=new Object();
    
    public RealMonitor(int size) {
        buffer=new LinkedList();
        bufferSize=size;
    }
    
    void cwait(String var){
        if(var.equals("notFull")){
            while(notFull==false){
                try{
                    Thread.sleep(200);
                }
                catch(InterruptedException e){
                    
                }                
            }
        }
        else if(var.equals("notEmpty")){
            while(notEmpty==false){
                try{
                    Thread.sleep(200);
                }
                catch(InterruptedException e){
                    
                }                
            }
        }
    }
    void csignal(String var){
        if(var.equals("notFull")){
            notFull=true;
        }
        else if(var.equals("notEmpty")){
            notEmpty=true;
        }
    }
    void append(int no){
        while(buffer.size()>=bufferSize){
            System.out.println("Buffer is full. suspend Producer");
            notFull=false;
            //cwait("notFull");
            
            synchronized(notFullO){
                try {
                    notFullO.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(RealMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            System.out.println("Buffer is not full. Resume Producer");
        }
        buffer.add(no);
        System.out.println(no+" added");
        //csignal("notEmpty");
        synchronized(notEmptyO){
            notEmptyO.notify();
        }
    }
    int take(){
        while(buffer.size()==0){
            System.out.println("Buffer is empty. suspend Consumer");
            notEmpty=false;
            //cwait("notEmpty");
            synchronized(notEmptyO){
                try {
                    notEmptyO.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(RealMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Buffer is not empty. Resume Consumer");
        }
        System.out.println(buffer.peek()+" retrieved");
        int No= buffer.remove();
        //csignal("notFull");
        synchronized(notFullO){
            notFullO.notify();
        }
        return No;
    }
}

public class ProdConsMonitor {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter buffer size:");
        RealMonitor m=new RealMonitor(sc.nextInt());
        new ConsMon(m).start();
        new ProdMon(m).start();
    }
}
