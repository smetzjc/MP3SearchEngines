package be.jcdo.mp3searchengines.tools;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UrlUtils {

    public static String convertURL(String url) throws MalformedURLException {
        String urlFormated = null; 
        
        
        URL urlLink = new URL(url);
        try {
            urlFormated = new URI(
                             "http", 
                             urlLink.getHost(), 
                             urlLink.getPath(),
                             null).toString();
        } catch (URISyntaxException ex) {
            Logger.getLogger(UrlUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return urlFormated;
    }
    
    public static URI string2URI(String url) throws MalformedURLException {
        URI urlFormated = null; 
        
        
        URL urlLink = new URL(url);
        try {
            urlFormated = new URI(
                             "http", 
                             urlLink.getHost(), 
                             urlLink.getPath(),
                             null);
        } catch (URISyntaxException ex) {
            Logger.getLogger(UrlUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return urlFormated;
    }
}
