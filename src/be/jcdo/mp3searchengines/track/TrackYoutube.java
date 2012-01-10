package be.jcdo.mp3searchengines.track;

import be.jcdo.mp3searchengines.enums.HostType;

/**
 *
 * @author forma209
 */
public class TrackYoutube extends Track {
    
	
	private String linkVideoImage;
	
    public TrackYoutube(String songName, String duration, int quality, String size, String url) {
        super(songName, duration, quality, size, HostType.YOUTUBE, url);
    }
}
