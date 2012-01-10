package be.jcdo.mp3searchengines.connectors;

import java.util.List;

import be.jcdo.mp3searchengines.track.Track;


/**
 *
 * @author forma208
 */
public interface Connector {
    public List<Track> search(String search);
    public void download(Track t);
}
