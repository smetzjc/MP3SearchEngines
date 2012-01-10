package be.jcdo.mp3searchengines.connectors.youtube;

import java.util.List;

import be.jcdo.mp3searchengines.connectors.Connector;
import be.jcdo.mp3searchengines.connectors.RunnableQueue;
import be.jcdo.mp3searchengines.connectors.mrtz.SearchMrtz;
import be.jcdo.mp3searchengines.track.Track;

/**
 * 
 * @author KrN@ge666
 */
public class ConnectorYoutube implements Connector
{

	private static ConnectorYoutube	instance;
	private static Object			synchronisedObject__	= 1;
	public static String			sessionID;

	private RunnableQueue			runQueue;

	private SearchYoutube			youtubeSearch;

	public static ConnectorYoutube getInstance()
	{
		if (null == instance)
		{ // 1st call
			synchronized (synchronisedObject__)
			{
				if (null == instance)
				{
					instance = new ConnectorYoutube();
				}
			}
		}
		return instance;
	}

	@Override
	public List<Track> search(String search)
	{
		youtubeSearch = new SearchYoutube(search);

		youtubeSearch.search();

		return youtubeSearch.getTracks();
	}

	@Override
	public void download(Track t)
	{

	}
}
