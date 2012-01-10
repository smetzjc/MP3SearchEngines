package be.jcdo.mp3searchengines.track;

import be.jcdo.mp3searchengines.enums.HostType;

public class TrackWrzuta extends Track{
	public TrackWrzuta(String songName, String duration, String size, String url) {
        super(songName, duration, 0, size, HostType.AUDIODUMP, url); //(songName, duration, HostType.AUDIODUMP, url);
    }

}
