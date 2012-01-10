package be.jcdo.mp3searchengines.connectors.audiodump;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.httpclient.HttpURL;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;

import be.jcdo.mp3searchengines.Mp3SearchEngines;
import be.jcdo.mp3searchengines.enums.Address;
import be.jcdo.mp3searchengines.enums.DownloadStatus;
import be.jcdo.mp3searchengines.http.MyHttpClient;
import be.jcdo.mp3searchengines.tools.FileUtils;
import be.jcdo.mp3searchengines.track.TrackAudiodump;

public class DownAudiodump {
	
	private TrackAudiodump track;
    
    private MyHttpClient mhc;

    public DownAudiodump(TrackAudiodump track) {
        this.track = track;
        
        this.mhc = MyHttpClient.getInstance();
    }

    public void download()
    {     
        try
        {
	        //Download the file
        	File destDir = new File(Mp3SearchEngines.getDestDir());
            if(!destDir.exists()) destDir.mkdirs();
            
            String filename = destDir + "\\" + FileUtils.encodeFilename(track.getSongName()) + ".mp3";
	        File trackFile = new File(filename);
	        
	        //Update track.state
	        track.setStatus(DownloadStatus.DOWNLOADING);
	        
	        HttpGet httpget = new HttpGet(new HttpURL(Address.AUDIODUMPADDR + track.getFinalLink()).getEscapedURI());
	        httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	        httpget.addHeader("Accept-Language", "fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
	        httpget.addHeader("Accept-Encoding", "gzip, deflate");
	        httpget.addHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
	        
	        Thread downThread = mhc.simpleGetRequest(httpget, trackFile);
	        downThread.start();
	        downThread.join();
	        
	        //Update track.state
	        track.setStatus(DownloadStatus.COMPLETED);
        } 
        catch (HttpResponseException e)
		{
			e.printStackTrace();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
    }
}
