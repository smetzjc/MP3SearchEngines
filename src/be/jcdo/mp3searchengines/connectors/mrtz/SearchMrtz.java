package be.jcdo.mp3searchengines.connectors.mrtz;

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

import be.jcdo.mp3searchengines.enums.Address;
import be.jcdo.mp3searchengines.http.GetThread;
import be.jcdo.mp3searchengines.http.MyHttpClient;
import be.jcdo.mp3searchengines.track.Track;
import be.jcdo.mp3searchengines.track.TrackMrtz;


/**
 *
 * @author Hervé
 */
public class SearchMrtz {
	private List<Track> tracks;
	
    private MyHttpClient mhc;
    
    private String request;
    private TrackMrtz trackTSM;

    public SearchMrtz(String request) {
        this(request, null);
    }

	public SearchMrtz(String request, TrackMrtz trackTSM) {
        mhc = MyHttpClient.getInstance();
        tracks = new ArrayList<Track>();
        
        this.request = request;
        this.trackTSM = trackTSM;
    }
    
    public void search()
	{
		try 
		{
            File temp;

            //Get a clean html page from a search on MRTZCMP3.NET
            temp = File.createTempFile("sMRTZ", ".html");
            temp.deleteOnExit();
            System.out.println(temp.getAbsolutePath());

            //Create a new request, start it and wait it finishes
            GetThread t = mhc.getRequest(Address.MRTZADDR + request + "_1s.html", temp);
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
            XPath xpa = XPath.newInstance("/html/descendant-or-self::table[@id='myTable']/tbody/tr");

            List<?> results = xpa.selectNodes(racine);
            
            Iterator<?> iter = results.iterator();
            Element noeudCourant = null;

            while (iter.hasNext()) {
            	try
            	{
	                noeudCourant = (Element) iter.next();
	
	                //id
	                xpa = XPath.newInstance("./td[1]");
	                int mrtzID = Integer.parseInt(xpa.valueOf(noeudCourant))-1;
	                
	                //artist
	                xpa = XPath.newInstance("./td[2]");
	                String artist = xpa.valueOf(noeudCourant);
	
	                //title
	                xpa = XPath.newInstance("./td[3]");
	                String title = xpa.valueOf(noeudCourant);
	                
	                //quality
	                xpa = XPath.newInstance("./td[4]");
	                int quality = Integer.parseInt(xpa.valueOf(noeudCourant).replace("kbps", ""));
	                
	                //duration
	                xpa = XPath.newInstance("./td[5]");
	                String duration = xpa.valueOf(noeudCourant);
	
	                //finalLink (preview link)
	                xpa = XPath.newInstance("./td[7]/a/@href");
	                String link = xpa.valueOf(noeudCourant);
	                
	                //SessionID
	                String sessionID = null;
	                Pattern pattern = Pattern.compile("(&b=)([a-zA-Z]{5})");
		            Matcher matcher = pattern.matcher(link);
		
		            if(matcher.find()) {
		                String match = matcher.group();
		                
		                sessionID = match.substring(3);
		                
		                if(ConnectorMrtz.sessionID != sessionID) 
		                	ConnectorMrtz.sessionID = sessionID;
		            }
	                
	                //Create a new ThrackMrtz and fire event
	                TrackMrtz t = null;
	                if(!artist.isEmpty() || !title.isEmpty())
	                {
	                    t = new TrackMrtz(artist, title, duration, quality, null, link, mrtzID, sessionID, request);
	                    
	                    if(trackTSM == null)
	                    	tracks.add(t);
	                    else
	                    {
	                    	if(trackTSM.getMrtzID() == t.getMrtzID())
	                    	{
	                    		tracks.add(t);
	                    		break;
	                    	}
	                    }
	                }
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

	public TrackMrtz getTrackTSM()
	{
		return trackTSM;
	}
	public void setTrackTSM(TrackMrtz trackTSM)
	{
		this.trackTSM = trackTSM;
	}
}
