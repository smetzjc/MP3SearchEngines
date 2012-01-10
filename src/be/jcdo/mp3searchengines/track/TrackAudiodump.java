package be.jcdo.mp3searchengines.track;

import be.jcdo.mp3searchengines.enums.HostType;

/**
 *
 * @author Hervé
 */
public class TrackAudiodump extends Track {
	
    public TrackAudiodump(String songName, String duration, String url) {
        super(songName, duration, HostType.AUDIODUMP, url);
    }
}
