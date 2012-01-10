package be.jcdo.mp3searchengines;
import java.util.List;


import be.jcdo.mp3searchengines.connectors.mrtz.ConnectorMrtz;
import be.jcdo.mp3searchengines.track.Track;


public class TestMp3SE
{
	public static void main(String[] args)
	{
		Mp3SearchEngines mp3SE = new Mp3SearchEngines();
		List<Track> tracks = mp3SE.search("Indivision");
		
		//ConnectorMrtz cmrtz = ConnectorMrtz.getInstance();
		//List<Track> tracks = cmrtz.search("Indivision");
		
		System.out.println("Found " + tracks.size() + " tracks");
		for(Track t : tracks)
		{
			System.out.println(t.getSongName());
			System.out.println(t.getType());
		}
	}
}
