package be.jcdo.mp3searchengines.connectors.wrzuta;

import java.util.List;
import java.util.concurrent.TimeUnit;

import be.jcdo.mp3searchengines.connectors.Connector;
import be.jcdo.mp3searchengines.connectors.RunnableQueue;
import be.jcdo.mp3searchengines.connectors.mrtz.SearchMrtz;
import be.jcdo.mp3searchengines.track.Track;
import be.jcdo.mp3searchengines.track.TrackWrzuta;

public class ConnectorWrzuta implements Connector {
	
	private static ConnectorWrzuta instance;
    private static Object synchronisedObject__ = 1;
    
    private RunnableQueue runQueue;
    
    private SearchWrzuta wrzutaSearch;
    
    public static ConnectorWrzuta getInstance() {
        if (null == instance) { // 1st call
            synchronized(synchronisedObject__) {
                if (null == instance) {
                    instance = new ConnectorWrzuta();
                }
            }
        }
        return instance;
    }
    
    private ConnectorWrzuta() {
    	//We don't really need to wait for mrtzcmp3 - 3 sec between each download is enough
        runQueue = new RunnableQueue(3L, TimeUnit.SECONDS);
    }
    
	@Override
	public List<Track> search(String search) {
		wrzutaSearch = new SearchWrzuta(search);
		wrzutaSearch.search();
		
		return wrzutaSearch.getTracks();
	}

	@Override
	public void download(Track t) {
		final TrackWrzuta track = (TrackWrzuta) t;
		
		runQueue.submitTask(new Runnable()
		{
			@Override
			public void run()
			{
				DownWrzuta dWrzuta = new DownWrzuta(track);
				dWrzuta.download();
			}
		});
	}

}
