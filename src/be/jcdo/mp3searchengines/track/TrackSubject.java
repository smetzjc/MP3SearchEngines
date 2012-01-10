package be.jcdo.mp3searchengines.track;

public interface TrackSubject {
	public void addTrackListener(TrackListener trackListener);
	public void removeTrackListener(TrackListener trackListener);
	public void clearTrackListener();
}
