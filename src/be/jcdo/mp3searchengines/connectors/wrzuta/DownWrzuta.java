package be.jcdo.mp3searchengines.connectors.wrzuta;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;

import be.jcdo.mp3searchengines.Mp3SearchEngines;
import be.jcdo.mp3searchengines.connectors.mrtz.ConnectorMrtz;
import be.jcdo.mp3searchengines.enums.DownloadStatus;
import be.jcdo.mp3searchengines.http.GetThread;
import be.jcdo.mp3searchengines.http.MyHttpClient;
import be.jcdo.mp3searchengines.tools.FileUtils;
import be.jcdo.mp3searchengines.track.TrackWrzuta;

public class DownWrzuta {
	private int MAXTRIES = 3;
	private boolean success = false;
	private int numTests = 0;
	
    private TrackWrzuta trackDW;
    
    private MyHttpClient mhc;

    public DownWrzuta(TrackWrzuta track) {
        this.trackDW = track;
        
        this.mhc = MyHttpClient.getInstance();
    }
    
    public void download()
    {     
    	//update track.state
        trackDW.setStatus(DownloadStatus.STARTING);
        
    	do
    	{
    		numTests++;
	            
	        try { 
	        	
	            //Click on blue button
	            HttpGet httpget = new HttpGet();
	            httpget.setURI(new URI(trackDW.getFinalLink().replace(" ", "%20")));
	            httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	            httpget.addHeader("Accept-Language", "fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
	            httpget.addHeader("Accept-Encoding", "gzip, deflate");
	            httpget.addHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
	
	            
	            //Download the file
	            File destDir = new File(Mp3SearchEngines.getDestDir());
	            if(!destDir.exists()) destDir.mkdirs();
	            
	            String filename = destDir + "\\" + FileUtils.encodeFilename(trackDW.getSongName()) + ".mp3";
	            File trackFile = new File(filename);
	            
	            //update track.state
	            trackDW.setStatus(DownloadStatus.DOWNLOADING);
	            
	            Thread downThread = mhc.simpleGetRequest(httpget, trackFile);
	            downThread.start();
	            downThread.join();
	            
	            
	            //If the size of the file is under 1000 bytes
	            if(trackFile.length() < 1000)
	            {
	            	success = false;
	            	
	            	System.out.println("Restarting download for : " + trackDW.getSongName() + " - Attempt n°"+numTests);
	            	
	            	//update track.state
	                trackDW.setStatus(DownloadStatus.RESTARTING);
	            }
	            else 
	            {
	            	success = true;
	            	
	            	//update track.state
		            trackDW.setStatus(DownloadStatus.COMPLETED);
	            }
	            
	        } catch (InterruptedException ex) {
	            Thread.currentThread().interrupt();
	        } catch (IOException ex) {
	        	HandleException(ex);
	        } catch (URISyntaxException ex) {
	        	HandleException(ex);
			}
    	}while(success==false && numTests < MAXTRIES);
    }
    
    private void HandleException(Exception e)
    {
    	e.printStackTrace();
    	
    	//update track.state
        trackDW.setStatus(DownloadStatus.ERROR);
        
        success = false;
    }
}
