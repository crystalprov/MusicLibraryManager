package models;

public class Podcast extends MusicItem {
    // Fields
    private String host;
    private int episodeNumber;
    private String topic;

    // Getters
    public String getHost() {return host;}
    public int getEpisodeNumber() {return episodeNumber;}
    public String getTopic() {return topic;}

    // Setters 
    public void setHost(String host) {this.host = host;}
    public void setEpisodeNumber(int episodeNumber) {this.episodeNumber = episodeNumber;}
    public void setTopic(String topic) {this.topic = topic;}

    // Constructor
    // podcast,3,Tech Talk,2023,Jane Doe,AI,42
    public Podcast(int id, String title, int releaseYear, String host, String topic, int episodeNumber){
        super(id, title, releaseYear);
        this.host = host;
        this.episodeNumber = episodeNumber;
        this.topic = topic;
    }

    
    // A IMPLÉMENTÉ OBLIGATOIREMENT 
    @Override
    public String getInfo() {
        return "Podcast [ID=" + getId() + ", Title=" + getTitle() + ", Release Year=" + getReleaseYear() + ", Host=" + host + ", Episode=" + getEpisodeNumber() + ", Topic=" + topic + "]";
    }
   
    @Override
    public String toCSV() {
        return "podcast," + getId() + "," + getTitle() + "," + getReleaseYear() + "," + host + "," + topic + "," + episodeNumber ;
    }
}
