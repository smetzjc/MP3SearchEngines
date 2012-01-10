package be.jcdo.mp3searchengines.http;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import be.jcdo.mp3searchengines.tools.FileUtils;
import be.jcdo.mp3searchengines.tools.UrlUtils;


public class MyHttpClient {

    public static boolean VERBOSE = false;
    
    private static MyHttpClient instance;
    private static Object synchronisedObject__ = 1;
    private HttpClient httpclient;
    
    public static MyHttpClient getInstance() {
        if (null == instance) { // 1st call
            synchronized(synchronisedObject__) {
                if (null == instance) {
                    instance = new MyHttpClient();
                }
            }
        }
        return instance;
    }
    
    private MyHttpClient() {
        
    	// Create and initialize scheme registry 
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);
        // Increase max total connection to 200
        cm.setMaxTotal(200);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);
        // Increase max connections for localhost:80 to 50
        HttpHost localhost = new HttpHost("locahost", 80);
        cm.setMaxForRoute(new HttpRoute(localhost), 50);
        
        // Create the HttpClient instance
        httpclient = new DefaultHttpClient(cm);
        
        // Set the User Agent as Firefox browser v8.0.1
        HttpProtocolParams.setUserAgent(httpclient.getParams(), "Mozilla/5.0 (Windows NT 6.1; rv:8.0.1) Gecko/20100101 Firefox/8.0.1");
    }
    
    public GetThread getRequest(URI uri) throws HttpResponseException, MalformedURLException, IOException{
        return getRequest(uri, null);
    }
    
    public GetThread getRequest(String url) throws HttpResponseException, MalformedURLException, IOException, URISyntaxException{
        return getRequest(UrlUtils.string2URI(url), null);
    }
    
    public GetThread getRequest(URI uri, File dest) throws HttpResponseException, MalformedURLException, IOException  {   
        HttpGet httpget = new HttpGet(uri);
        
        GetThread getThread = new GetThread(httpclient, httpget, dest);
        
        return getThread;
    }
    
    public GetThread getRequest(String url, File dest) throws HttpResponseException, MalformedURLException, IOException, URISyntaxException{
        return getRequest(UrlUtils.string2URI(url), dest);
    }
    
    public GetThread getRequest(HttpGet httpget, File dest) throws HttpResponseException, MalformedURLException, IOException  {
        
        GetThread getThread = new GetThread(httpclient, httpget, dest);
        
        return getThread;
    }
    
    
    
    public SimpleGetThread simpleGetRequest(HttpGet httpget, File dest) throws HttpResponseException, MalformedURLException, IOException  {
        
        SimpleGetThread sGetThread = new SimpleGetThread(httpclient, httpget, dest);
        
        return sGetThread;
    }

    
    
    public void close() {
        httpclient.getConnectionManager().shutdown();
    }
    
    
    
    /******************** Same as GetThread but without cleaning *****************/ 
    class SimpleGetThread extends Thread 
    {
        private final HttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        private HttpResponse response;
        private HttpEntity entity;
        private File dest;

        public SimpleGetThread(HttpClient httpClient, HttpGet httpget, File dest) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
            this.dest = dest;
        }

        @Override
        public void run() {
            try {
                
                response = this.httpClient.execute(this.httpget, this.context);
                
                entity = response.getEntity();
                
                if (entity != null && dest != null) {
                    FileUtils.inputStream2File(entity.getContent(), dest);
                    
                    EntityUtils.consume(entity);
                }
            } catch (Exception ex) {
                this.httpget.abort();
            }
        }

    }
}
