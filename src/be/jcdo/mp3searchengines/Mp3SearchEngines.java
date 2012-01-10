package be.jcdo.mp3searchengines;

import java.util.ArrayList;
import java.util.List;

import be.jcdo.mp3searchengines.connectors.Connector;
import be.jcdo.mp3searchengines.connectors.audiodump.ConnectorAudiodump;
import be.jcdo.mp3searchengines.connectors.mrtz.ConnectorMrtz;
import be.jcdo.mp3searchengines.track.Track;

public class Mp3SearchEngines
{
	private static String destDir;
	
	private List<Connector> connectorList;
	private ConnectorMrtz cMrtz;
	private ConnectorAudiodump cAudiodump;
	

	public Mp3SearchEngines()
	{
		if(destDir == null)
			destDir = "C:\\Downloads\\";
		
		connectorList = new ArrayList<Connector>();
		
		cAudiodump = ConnectorAudiodump.getInstance();
		cMrtz = ConnectorMrtz.getInstance();
		
		connectorList.add(cAudiodump);
		connectorList.add(cMrtz);
	}
	
	public List<Track> search(final String query)
    {
		final List<Track> tracks = new ArrayList<Track>();
        
        for(final Connector c : connectorList)
        {
        	List<Track> trackList = c.search(query);
			if(!trackList.isEmpty())
				tracks.addAll(trackList);
        }
        
        return tracks;
    }
    
    public void download(Track t)
    {
        switch(t.getType())
        {
            case MRTZ : cMrtz.download(t);
                break;
            case AUDIODUMP : cAudiodump.download(t);
            	break;
            default : 
            	try
				{
					throw new Exception("Unrecognized Host " + t.getType());
				} catch (Exception e){
					e.printStackTrace();
				}
            	
                break;
        }
    }
	
	/******************** Getters and setters ********************/
	public static String getDestDir()
	{
		return destDir;
	}
	public static void setDestDir(String destDir)
	{
		Mp3SearchEngines.destDir = destDir;
	}
	
}
