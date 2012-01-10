package be.jcdo.mp3searchengines.track;

import be.jcdo.mp3searchengines.enums.HostType;


public class TrackMrtz extends Track{
    //Basic
    private String artist;
    private String title;

    private int mrtzID;
    private String searchQuery;
    private String sessionID;
    
    public TrackMrtz(String artist, String title, String duration, int quality, String size, String link, int mrtzID, String sessionID, String searchQuery) {
        super(artist + " - " + title, duration, quality, size, HostType.MRTZ, link);
        
        this.artist = artist;
        this.title = title;
        this.searchQuery = searchQuery;
        this.mrtzID = mrtzID;
        this.sessionID = sessionID;
    }



	/******************** Getters and setters ********************/
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
    	String oldValue = this.artist;
        this.artist = artist;
        
        changeSupport.firePropertyChange("artist", oldValue, artist);
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
    	String oldValue = this.title;
        this.title = title;
        
        changeSupport.firePropertyChange("title", oldValue, title);
    }
    
    public int getMrtzID() {
        return mrtzID;
    }
    public void setMrtzID(int mrtzID) {
    	int oldValue = this.mrtzID;
        this.mrtzID = mrtzID;
        
        changeSupport.firePropertyChange("mrtzID", oldValue, mrtzID);
    }
    
    public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		String oldValue = this.sessionID;
		this.sessionID = sessionID;
		
		changeSupport.firePropertyChange("sessionID", oldValue, sessionID);
	}
    
    public String getSearchQuery() {
        return searchQuery;
    }
    public void setSearchQuery(String searchQuery) {
    	String oldValue = this.searchQuery;
        this.searchQuery = searchQuery;
        
        changeSupport.firePropertyChange("searchQuery", oldValue, searchQuery);
    }
}
