package be.jcdo.mp3searchengines.track;

public interface TrackListener {
	public void addTrack(Track track);
	public void removeTrack(Track track);
	public void updateTrack(Track track);
}
