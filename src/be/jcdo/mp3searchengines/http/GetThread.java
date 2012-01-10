package be.jcdo.mp3searchengines.http;


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import be.jcdo.mp3searchengines.tools.FileUtils;

/**
 *
 * @author forma208
 */
public class GetThread extends Thread {
    
    private HttpClient httpClient;
    private HttpContext context;
    private HttpGet httpget;
    
    private File dest;
    private String encoding;
    
	private HttpResponse response;
    private HttpEntity entity;

    public GetThread(HttpClient httpClient, HttpGet httpget)
    {
        this(httpClient, httpget, null);
    }
    
    public GetThread(HttpClient httpClient, HttpGet httpget, File dest) {
        this.httpClient = httpClient;
        this.context = new BasicHttpContext();
        this.httpget = httpget;
        this.dest = dest;
    }

    @Override
    public void run() {
        try {
            response = httpClient.execute(httpget, context);

            entity = response.getEntity();

            if (getEntity() != null && getDest() != null) {
                FileUtils.inputStream2File(entity.getContent(), getDest());

                //encoding = FileUtils.detectEncoding(dest.getAbsolutePath());
                if (encoding == null) {
                    encoding = "UTF-8";
                }

                FileUtils.cleanHtmlFile(dest, "UTF-8");
                
                //Ensure the InputStream (Entity) gets released
                EntityUtils.consume(entity);
            }
        } catch (ClientProtocolException ex) {
            Logger.getLogger(GetThread.class.getName()).log(Level.SEVERE, null, ex);
            getHttpget().abort();
        } catch (IOException ex) {
            Logger.getLogger(GetThread.class.getName()).log(Level.SEVERE, null, ex);
            getHttpget().abort();
        } 
    }
    
    public String write2String()
    {
    	String result = null;
    	
    	if(dest == null)
    	{
			try {
				result = FileUtils.inputStream2String(entity.getContent(), "UTF-8");
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	else
    	{	    	
	    	try {
				result = FileUtils.file2String(dest);
			} catch (IOException ex) {
				Logger.getLogger(GetThread.class.getName()).log(Level.SEVERE, null, ex);
			}
    	}
    	
    	return result;
    }

    /******************** Getters and setters ********************/
    public HttpClient getHttpClient() {
        return httpClient;
    }
    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpContext getContext() {
        return context;
    }
    public void setContext(HttpContext context) {
        this.context = context;
    }

    public HttpGet getHttpget() {
        return httpget;
    }
    public void setHttpget(HttpGet httpget) {
        this.httpget = httpget;
    }

    public File getDest() {
        return dest;
    }
    public void setDest(File dest) {
        this.dest = dest;
    }
    
    public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public HttpEntity getEntity() {
        return entity;
    }
    public void setEntity(HttpEntity entity) {
        this.entity = entity;
    }
}