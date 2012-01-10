package be.jcdo.mp3searchengines.connectors.mrtz;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;

import be.jcdo.mp3searchengines.Mp3SearchEngines;
import be.jcdo.mp3searchengines.enums.DownloadStatus;
import be.jcdo.mp3searchengines.http.GetThread;
import be.jcdo.mp3searchengines.http.MyHttpClient;
import be.jcdo.mp3searchengines.tools.FileUtils;
import be.jcdo.mp3searchengines.track.TrackMrtz;


/**
 *
 * @author forma208
 */
public class DownMrtz {
	
	private int MAXTRIES = 3;
	private boolean success = false;
	private int numTests = 0;
	
    private TrackMrtz trackTDM;
    
    private MyHttpClient mhc;

    public DownMrtz(TrackMrtz track) {
        this.trackTDM = track;
        
        this.mhc = MyHttpClient.getInstance();
    }
    
    public void download()
    {     
    	//update track.state
        trackTDM.setStatus(DownloadStatus.STARTING);
        
    	do
    	{
    		numTests++;
    		
    		//If the session ID of the track doesn't match the current session ID, reload the search
    		if(ConnectorMrtz.sessionID != trackTDM.getSessionID())
    			reloadSearch();
    		
	        String lastUrl = null;
	        GetThread getThread = null;
	            
	        try { 
	        	
	            //Click on blue button
	            HttpGet httpget = new HttpGet();
	            httpget.setURI(new URI(trackTDM.getFinalLink().replace(" ", "%20")));
	            httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	            httpget.addHeader("Accept-Language", "fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
	            httpget.addHeader("Accept-Encoding", "gzip, deflate");
	            httpget.addHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
	
	            getThread = mhc.getRequest(httpget, null);
	            getThread.start();
	            getThread.join();
	            
	            
	            //Download the file
	            File destDir = new File(Mp3SearchEngines.getDestDir());
	            if(!destDir.exists()) destDir.mkdirs();
	            
	            String filename = destDir + "\\" + FileUtils.encodeFilename(trackTDM.getSongName()) + ".mp3";
	            File trackFile = new File(filename);
	
	            httpget.setURI(new URI("http://www.mrtzcmp3.net/Song_"+trackTDM.getSessionID()+trackTDM.getMrtzID()));
	            httpget.setHeader("Referer", lastUrl);
	            
	            //update track.state
	            trackTDM.setStatus(DownloadStatus.DOWNLOADING);
	            
	            Thread downThread = mhc.simpleGetRequest(httpget, trackFile);
	            downThread.start();
	            downThread.join();
	            
	            
	            //If the size of the file is under 1000 bytes
	            if(trackFile.length() < 1000)
	            {
	            	success = false;
	            	
	            	System.out.println("Restarting download for : " + trackTDM.getSongName() + " - Attempt n°"+numTests);
	            	
	            	//update track.state
	                trackTDM.setStatus(DownloadStatus.RESTARTING);
	            }
	            else 
	            {
	            	success = true;
	            	
	            	//update track.state
		            trackTDM.setStatus(DownloadStatus.COMPLETED);
	            }
	            
	        } catch (InterruptedException ex) {
	            Thread.currentThread().interrupt();
	        } catch (URISyntaxException ex) {
	        	HandleException(ex);
	        } catch (HttpResponseException ex) {
	        	HandleException(ex);
	        } catch (MalformedURLException ex) {
	        	HandleException(ex);
	        } catch (IOException ex) {
	        	HandleException(ex);
	        }
    	}while(success==false && numTests < MAXTRIES);
    }
	
    private void HandleException(Exception e)
    {
    	e.printStackTrace();
    	
    	//update track.state
        trackTDM.setStatus(DownloadStatus.ERROR);
        
        success = false;
    }
    
    //Reload the search if the server is overloaded :
    private void reloadSearch()
    {
    	SearchMrtz sMrtz = new SearchMrtz(trackTDM.getSearchQuery(), trackTDM);
	    	
	    sMrtz.search();
	    	
	    TrackMrtz newest = (TrackMrtz) sMrtz.getTracks().get(0);
	    if(newest != null)
	    {
	    	trackTDM.setFinalLink(newest.getFinalLink());
			trackTDM.setSessionID(newest.getSessionID());
	    }
    }
}
