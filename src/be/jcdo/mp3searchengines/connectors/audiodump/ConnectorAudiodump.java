package be.jcdo.mp3searchengines.connectors.audiodump;

import java.util.List;
import java.util.concurrent.TimeUnit;

import be.jcdo.mp3searchengines.connectors.Connector;
import be.jcdo.mp3searchengines.connectors.RunnableQueue;
import be.jcdo.mp3searchengines.track.Track;
import be.jcdo.mp3searchengines.track.TrackAudiodump;


/**
 *
 * @author forma208
 */
public class ConnectorAudiodump implements Connector{
    
	private static ConnectorAudiodump instance;
    private static Object synchronisedObject__ = 1;
    
    private RunnableQueue runQueue;
    
    private SearchAudiodump audiodumpSearch;
    private Thread searchThread;
    
    
    public static ConnectorAudiodump getInstance() {
        if (null == instance) { // 1st call
            synchronized(synchronisedObject__) {
                if (null == instance) {
                    instance = new ConnectorAudiodump();
                }
            }
        }
        return instance;
    }
    
    private ConnectorAudiodump() {
        //We need to wait 30 seconds between each download for Audiodump
        runQueue = new RunnableQueue(30L, TimeUnit.SECONDS);
    }
    
    @Override
    public List<Track> search(String search) {
    	audiodumpSearch = new SearchAudiodump(search);
        audiodumpSearch.search();
        
        return audiodumpSearch.getTracks();
    }

    @Override
    public void download(Track t) {
        final TrackAudiodump trackAudiodump = (TrackAudiodump) t;
        
        runQueue.submitTask(new Runnable()
		{
			
			@Override
			public void run()
			{
				DownAudiodump dAudiodump = new DownAudiodump(trackAudiodump);
				dAudiodump.download();
			}
		});
    }
}
