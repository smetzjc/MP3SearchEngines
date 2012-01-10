package be.jcdo.mp3searchengines.connectors.audiodump;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import be.jcdo.mp3searchengines.enums.Address;
import be.jcdo.mp3searchengines.http.MyHttpClient;
import be.jcdo.mp3searchengines.tools.URIUtils;
import be.jcdo.mp3searchengines.track.Track;
import be.jcdo.mp3searchengines.track.TrackAudiodump;


/**
 *
 * @author Hervé
 */
public class SearchAudiodump {
	
	private List<Track> tracks;
	
    private MyHttpClient mhc;
    
    private String request;

    public SearchAudiodump(String request) {
        mhc = MyHttpClient.getInstance();
        
        this.request = request;
        
        tracks = new ArrayList<Track>();
    }

    public void search() {
        try {
            File temp;

            //Get a clean html page from a search on MRTZCMP3.NET
            temp = File.createTempFile("sAudiodump", ".html");
            temp.deleteOnExit();
            System.out.println(temp.getAbsolutePath());

            //Create a new request, start it and wait it finishes
            URI uri = new URI(URIUtils.encodeQuery(Address.AUDIODUMPADDR + "search.php?p=1&q=" + request + "&x=0&y=0"));

            HttpGet httpget = new HttpGet();
            httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpget.addHeader("Accept-Language", "fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
            httpget.addHeader("Accept-Encoding", "gzip, deflate");
            httpget.addHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            httpget.setURI(uri);
            
            
            Thread t = mhc.getRequest(httpget, temp);
            t.start();
            t.join();

            //Retrieve Track objects from xml file
            getTracks(temp);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (HttpResponseException ex) {
        	ex.printStackTrace();
        } catch (MalformedURLException ex) {
        	ex.printStackTrace();
        } catch (IOException ex) {
        	ex.printStackTrace();
        } catch (URISyntaxException ex) {
        	ex.printStackTrace();
        }
    }

	//Get the list of Tracks from a (clean) html file search
    private void getTracks(File input) throws IOException 
    {
        /******************** get Tracks ********************/
        org.jdom.Document document = null;
        try {
            SAXBuilder sxb = new SAXBuilder();
            document = sxb.build(input.getAbsolutePath());


            /* Initializing a new Element with the root Element*/
            Element racine = document.getRootElement();
            XPath xpa = XPath.newInstance("/html/descendant-or-self::span[@class='links']/a");

            List<?> results = xpa.selectNodes(racine);

            Iterator<?> iter = results.iterator();
            Element noeudCourant = null;

            while (iter.hasNext()) {
                noeudCourant = (Element) iter.next();
                
                //songName
                xpa = XPath.newInstance(".");
                String songname = xpa.valueOf(noeudCourant);
                
                //url
                xpa = XPath.newInstance("./@href");
                String url = xpa.valueOf(noeudCourant);
                
                //duration
                xpa = XPath.newInstance("./following-sibling::text()[1]");
                String duration = xpa.valueOf(noeudCourant).trim().replace("(", "").replace(")", "");
                
                //Create a new ThrackAudiodump and add it to the list
                TrackAudiodump t = null;
                if(!duration.trim().isEmpty())
                {
                    t = new TrackAudiodump(songname, duration, url);
                    tracks.add(t);
                }
            }
        } catch (JDOMException ex) {
        	ex.printStackTrace();
        }
    }
    
    
    public List<Track> getTracks()
	{
		return tracks;
	}
}
