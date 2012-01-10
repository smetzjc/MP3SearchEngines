package be.jcdo.mp3searchengines.connectors.mrtz;

import java.util.List;
import java.util.concurrent.TimeUnit;

import be.jcdo.mp3searchengines.connectors.Connector;
import be.jcdo.mp3searchengines.connectors.RunnableQueue;
import be.jcdo.mp3searchengines.track.Track;
import be.jcdo.mp3searchengines.track.TrackMrtz;

/**
 * 
 * @author forma208
 */
public class ConnectorMrtz implements Connector {

	private static ConnectorMrtz	instance;
	private static Object			synchronisedObject__	= 1;
	public static String			sessionID;

	private RunnableQueue			runQueue;

	private SearchMrtz				mrtzSearch;

	public static ConnectorMrtz getInstance()
	{
		if (null == instance)
		{ // 1st call
			synchronized (synchronisedObject__)
			{
				if (null == instance)
				{
					instance = new ConnectorMrtz();
				}
			}
		}
		return instance;
	}

	private ConnectorMrtz()
	{
		// We don't really need to wait for mrtzcmp3 - 3 sec between each
		// download is enough
		runQueue = new RunnableQueue(0L, TimeUnit.SECONDS);
	}

	@Override
	public List<Track> search(String search)
	{
		mrtzSearch = new SearchMrtz(search);

		mrtzSearch.search();

		return mrtzSearch.getTracks();
	}

	@Override
	public void download(Track t)
	{
		final TrackMrtz trackmrtz = (TrackMrtz) t;

		runQueue.submitTask(new Runnable()
		{
			@Override
			public void run()
			{
				DownMrtz dMrtz = new DownMrtz(trackmrtz);
				dMrtz.download();
			}
		});
	}
}
