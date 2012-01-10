package be.jcdo.mp3searchengines.connectors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class RunnableQueue 
{
	private long waitTime;
	private TimeUnit unit;
	
	ExecutorService e;
	
	public RunnableQueue(long waitTime, TimeUnit unit) {
		e = Executors.newSingleThreadExecutor();
		
		this.waitTime = waitTime;
		this.unit = unit;
	}
	
	public void submitTask(final Runnable r){
	    e.submit(new Runnable(){
	       public void run(){
	    	   Thread t = new Thread(r);
	    	   t.start();
	    	   
	    	   try {
				t.join();
				Thread.sleep(unit.toMillis(waitTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	       }
	    });
	}
}
