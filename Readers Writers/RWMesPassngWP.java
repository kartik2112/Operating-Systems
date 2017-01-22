/**
 *
 * @author Kartik Shenoy
 * https://github.com/kartik2112
 */

import java.util.*;

class RWMesPsngWPConstruct extends Thread{
    Queue<Integer> writereq,readreq,finished,writeAcc[],readAcc[];
    int count;
    public RWMesPsngWPConstruct(Queue wr,Queue rr,Queue fin,Queue wa[],Queue ra[]) {
        writereq=wr;
        readreq=rr;
        finished=fin;
        writeAcc=wa;
        readAcc=ra;
        count=100;
        this.setName("controller");
    }
    
    void send(Queue<Integer> q,Integer in){
        q.add(in);
    }
    
    int receive(Queue<Integer> q){
        if(q.size()==0){
            System.out.println(Thread.currentThread().getName()+" is blocked on empty mailbox");
            while(q.size()==0){
                try{
                    Thread.sleep(200);
                }
                catch(Exception e){
                    
                }
            }
        }
        return q.remove();
    }
    
    public void run(){
        int writerID=0;
        while(true){
            if(count>0){
                if(finished.size()!=0){                    
                    receive(finished);
                    count++;
                }
                else if(writereq.size()!=0){
                    writerID=receive(writereq);
                    count-=100;
                }
                else if(readreq.size()!=0){
                    int readID=receive(readreq);                    
                    send(readAcc[readID],1);
                    System.out.println("---approval sent to Reader"+readID);
                    count--;
                }
            }
            if(count==0){
                send(writeAcc[writerID],1);
                System.out.println("---approval sent to Writer"+writerID);
                receive(finished);
                count=100;
            }
            while(count<0){
                receive(finished);
                count++;
            }
        }
        
    }
}


class RWMesPsngWPReader extends Thread{
    int ind;
    RWMesPsngWPConstruct c;
    
    public RWMesPsngWPReader(int i,RWMesPsngWPConstruct c) {
        this.c=c;
        ind=i;
        this.setName("Reader"+ind);
    }
    
    public void run(){
        System.out.println(Thread.currentThread().getName()+" has arrived");
        c.send(c.readreq,ind);
        System.out.println(Thread.currentThread().getName()+" has sent request to readrequest mailbox");
        System.out.println(Thread.currentThread().getName()+" is waiting for approval");
        c.receive(c.readAcc[ind]);
        System.out.println(Thread.currentThread().getName()+" has started reading");
        try{
            Thread.sleep(1000);
        }
        catch(Exception e){}
        System.out.println(Thread.currentThread().getName()+" has stopped reading");
        c.send(c.finished,ind);
    }    
}

class RWMesPsngWPWriter extends Thread{
    int ind;
    RWMesPsngWPConstruct c;
    
    public RWMesPsngWPWriter(int i,RWMesPsngWPConstruct c) {
        this.c=c;
        ind=i;
        this.setName("Writer"+ind);
    }
    
    public void run(){
        System.out.println(Thread.currentThread().getName()+" has arrived");
        c.send(c.writereq,ind);
        System.out.println(Thread.currentThread().getName()+" has sent request to writerequest mailbox");
        System.out.println(Thread.currentThread().getName()+" is waiting for approval");
        c.receive(c.writeAcc[ind]);
        System.out.println(Thread.currentThread().getName()+" has started writing");
        try{
            Thread.sleep(1000);
        }
        catch(Exception e){}
        System.out.println(Thread.currentThread().getName()+" has stopped writing");
        c.send(c.finished,ind);
    }    
}

public class RWMesPassngWP {
    public static void main(String[] args) {
        Queue<Integer> wr=new LinkedList();
        Queue<Integer> rr=new LinkedList();
        Queue<Integer> fin=new LinkedList();
        Queue<Integer> wa[]=new Queue[3];
        Queue<Integer> ra[]=new Queue[4];
        for(int i=1;i<=3;i++){
            ra[i]=new LinkedList();
        }
        for(int i=1;i<=2;i++){
            wa[i]=new LinkedList();
        }
        
        RWMesPsngWPConstruct c=new RWMesPsngWPConstruct(wr, rr, fin, wa, ra);
        
        c.start();
        
        
            
        
        for(int i=1;i<=1;i++){
            new RWMesPsngWPReader(i, c).start();
        }
        for(int i=1;i<=2;i++){
            new RWMesPsngWPWriter(i, c).start();
        }
        for(int i=2;i<=3;i++){
            new RWMesPsngWPReader(i, c).start();
        }
        
        
    }
}
