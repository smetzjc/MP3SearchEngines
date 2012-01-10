package be.jcdo.mp3searchengines.connectors.wrzuta;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import be.jcdo.mp3searchengines.connectors.mrtz.ConnectorMrtz;
import be.jcdo.mp3searchengines.enums.Address;
import be.jcdo.mp3searchengines.http.GetThread;
import be.jcdo.mp3searchengines.http.MyHttpClient;
import be.jcdo.mp3searchengines.track.Track;
import be.jcdo.mp3searchengines.track.TrackMrtz;
import be.jcdo.mp3searchengines.track.TrackWrzuta;

public class SearchWrzuta {
private List<Track> tracks;
	
    private MyHttpClient mhc;
    
    private String request;
    
    public SearchWrzuta(String request) {
    	mhc = MyHttpClient.getInstance();
        tracks = new ArrayList<Track>();
        
        this.request = request;
    }
    
    public void search()
	{
		try 
		{
            File temp;

            //Get a clean html page from a search on MRTZCMP3.NET
            temp = File.createTempFile("sWRZUTA", ".html");
            temp.deleteOnExit();
            System.out.println(temp.getAbsolutePath());

            //Create a new request, start it and wait it finishes
            GetThread t = mhc.getRequest(Address.WRZUTAADDR + "szukaj/audio/" + request, temp);
            t.start();
            t.join();
            
            //Retrieve Track objects from xml file
            getTracks(temp);
        } 
		catch (URISyntaxException ex) 
		{
            ex.printStackTrace();
        } 
		catch (IOException ex) {
			ex.printStackTrace();
        } 
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}
    
    private void getTracks(File input) throws IOException 
    {
        /******************** get Tracks ********************/
        org.jdom.Document document = null;
        try {
            SAXBuilder sxb = new SAXBuilder();
            document = sxb.build(input.getAbsolutePath());


            /* Initializing a new Element with the root Element*/
            Element racine = document.getRootElement();
            XPath xpa = XPath.newInstance("/html/descendant-or-self::div[@class='file audio']");

            List<?> results = xpa.selectNodes(racine);
            
            Iterator<?> iter = results.iterator();
            Element noeudCourant = null;

            while (iter.hasNext()) {
            	try
            	{
	                noeudCourant = (Element) iter.next();
	
	                //url
	                xpa = XPath.newInstance("./a[@class='title']/@href");
	                String link = xpa.valueOf(noeudCourant);
	                
	                Pattern pattern = Pattern.compile("(&b=)([a-zA-Z]{5})");
		            Matcher matcher = pattern.matcher(link);
		
		            if(matcher.find()) {
		            	String match = matcher.group();
		            }
	                
	                //songName
	                xpa = XPath.newInstance("./a[@class='title']");
	                String songName = xpa.valueOf(noeudCourant);
	                
	                //duration
	                xpa = XPath.newInstance("./descendant-or-self::span[@class='duration']");
	                String duration = xpa.valueOf(noeudCourant);
	
	                //size
	                xpa = XPath.newInstance("./descendant-or-self::span[@class='size']");
	                String size = xpa.valueOf(noeudCourant);
	                
	                //Create a new ThrackMrtz and fire event
	                TrackWrzuta t = new TrackWrzuta(songName, duration, size, link);
	                
            	}
            	catch(Exception ex)
            	{
            		continue;
            	}
            }
        } catch (JDOMException ex) {
            ex.printStackTrace();
        }
    }
    
    /******************** Getters and setters ********************/
	public List<Track> getTracks()
	{
		return tracks;
	}
	
	public String getRequest()
	{
		return request;
	}
	public void setRequest(String request)
	{
		this.request = request;
	}
}
