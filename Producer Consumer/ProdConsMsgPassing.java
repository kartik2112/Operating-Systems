/**
 *
 * @author Kartik Shenoy
 * https://github.com/kartik2112
 */

import java.util.*;
import java.util.concurrent.*;

class ProdMP extends Thread{
    MailBoxMP mb;
    ProdMP(MailBoxMP mb){
        this.mb=mb;
        this.setName("Producer");
    }
    public void run(){
        Random r=new Random();
        for(int i=0;i<20;i++){
            mb.receive("mayProduce");            
            try{
                Thread.sleep(200);
            }catch(Exception e){}
            int msg=r.nextInt(100);
            mb.send("mayConsume",msg); 
            System.out.println(msg+" sent");
            
        }
    }
}

class ConMP extends Thread{
    MailBoxMP mb;
    ConMP(MailBoxMP mb){
        this.mb=mb;
        this.setName("Consumer");
    }
    public void run(){
        Random r=new Random();
        for(int i=0;i<20;i++){
            int msg=mb.receive("mayConsume");
            System.out.println(msg+" received");
            try{
                Thread.sleep(200);
            }catch(Exception e){}
            mb.send("mayProduce",1); 
            try{
                Thread.sleep(500);
            }catch(Exception e){}
        }
    }
}


class MailBoxMP{
    Queue<Integer> mayProduce;
    Queue<Integer> mayConsume;
    Semaphore mayPsem=new Semaphore(1);
    Semaphore mayCsem=new Semaphore(1);
    MailBoxMP(int bufferSize){
        mayProduce=new LinkedList();
        mayConsume=new LinkedList();
        for(int i=0;i<bufferSize;i++){
            send("mayProduce",1);
        }
    }
    
    void send(String dest,int msg){
        if(dest.equals("mayProduce")){
            mayProduce.add(msg);
        }
        else if(dest.equals("mayConsume")){
            mayConsume.add(msg);
        }
    }
    int receive(String src){
        if(src.equals("mayProduce")){
            try{
                mayPsem.acquire();
            }
            catch(Exception e){}
            finally{
                if(mayProduce.size()==0){
                    System.out.println(Thread.currentThread().getName()+" blocked because mayProduce mailbox is empty");
                    while(mayProduce.size()==0){
                        try{
                            Thread.sleep(200);
                        }
                        catch(Exception e){}
                    }
                }
                int msg=mayProduce.remove();
                mayPsem.release();
                return msg;
            }            
        }
        else if(src.equals("mayConsume")){
            try{
                mayCsem.acquire();
            }
            catch(Exception e){}
            finally{
                while(true){
                    try{
                        if(mayConsume.size()==0){
                            System.out.println(Thread.currentThread().getName()+" blocked because mayConsume mailbox is empty");
                            while(mayConsume.size()==0){
                                try{
                                    Thread.sleep(200);
                                }
                                catch(Exception e){}
                            }
                        }
                        int msg=mayConsume.remove();
                        mayCsem.release();
                        return msg;
                    }
                    catch(Exception e){
                        System.out.println("Exception caught");
                    }
                    
                }
                
            }
        }
        else{
            return -1;
        }
    }
}

public class ProdConsMsgPassing {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter buffer size:");
        int buffsize=sc.nextInt();
        MailBoxMP mb=new MailBoxMP(buffsize);
        new ConMP(mb).start();
        new ProdMP(mb).start();
    }
}
