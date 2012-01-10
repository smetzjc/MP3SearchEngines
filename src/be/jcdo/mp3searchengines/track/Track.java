package be.jcdo.mp3searchengines.track;

import java.beans.PropertyChangeSupport;

import be.jcdo.mp3searchengines.enums.DownloadStatus;
import be.jcdo.mp3searchengines.enums.HostType;

/**
 *
 * @author forma208
 */
public abstract class Track {
    
    
    private int trackID;
    private String songName;    //Artist - Title
    private String duration;    //xx:xx
    private int quality;        //kbps
    private String size;        //MB
    private HostType type;
    private DownloadStatus status;
    
    private int progress = 0;   //download progress (0 - 100)
    private String finalLink;
    
    PropertyChangeSupport changeSupport;

    public Track(String songName, String duration, HostType type, String url) {
        this(songName, duration, 0, null, type, url);
    }
    
    public Track(String songName, String duration, int quality, String size, HostType type, String url) {
    	changeSupport = new PropertyChangeSupport(this);
    	
        this.songName = songName;
        this.duration = duration;
        this.quality = quality;
        this.size = size;
        this.type = type;
        this.status = DownloadStatus.QUEUED;
        this.finalLink = url;
    }
    
    
    
    /******************** Getters and setters ********************/
    public HostType getType() {
        return type;
    }
    public void setType(HostType type) {
        HostType oldValue = this.type;
        this.type = type;
        
        changeSupport.firePropertyChange("type", oldValue, type);
    }
    
    public DownloadStatus getStatus() {
        return status;
    }
    public void setStatus(DownloadStatus status) {
    	DownloadStatus oldValue = this.status; 
        this.status = status;
        
        changeSupport.firePropertyChange("status", oldValue, status);
    }

    public int getTrackID() {
        return trackID;
    }
    public void setTrackID(int trackID) {
    	int oldValue = this.trackID;
        this.trackID = trackID;
        
        changeSupport.firePropertyChange("trackID", oldValue, trackID);
    }

    public String getSongName() {
        return songName;
    }
    public void setSongName(String songName) {
        String oldValue = this.songName;
    	this.songName = songName;
        
    	changeSupport.firePropertyChange("songName", oldValue, songName);
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
    	String oldValue = this.duration;
        this.duration = duration;
        
        changeSupport.firePropertyChange("duration", oldValue, duration);
    }

    public int getQuality() {
        return quality;
    }
    public void setQuality(int quality) {
    	int oldValue = this.quality;
        this.quality = quality;
        
        changeSupport.firePropertyChange("quality", oldValue, quality);
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
    	String oldValue = this.size;
        this.size = size;
        
        changeSupport.firePropertyChange("size", oldValue, size);
    }
    
    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
    	int oldValue = this.progress;
        this.progress = progress;
        
        changeSupport.firePropertyChange("progress", oldValue, progress);
    }
    
    public String getFinalLink() {
        return finalLink;
    }
    public void setFinalLink(String finalLink) {
    	String oldValue = this.finalLink;
        this.finalLink = finalLink;
        
        changeSupport.firePropertyChange("finalLink", oldValue, finalLink);
    }
    
    public String toString(){
        return "songName : " + songName + "\n url : " + finalLink;
    }
}
