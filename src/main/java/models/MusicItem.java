package models;

public abstract class MusicItem {
    // Fields
    private int id;
    private String title;
    private int releaseYear;
    private boolean isPlaying;

    // Getters
    public int getId() {return id;}
    public String getTitle() {return title;}
    public int getReleaseYear() {return releaseYear;}
    public boolean getIsPlaying() {return isPlaying;}

    // Setters 
    public void setId(int id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setReleaseYear(int releaseYear) {this.releaseYear = releaseYear;}
    public void setIsPlaying(boolean isPlaying) {this.isPlaying = isPlaying;}

    // Constructor
    public MusicItem(int id, String title, int releaseYear) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.isPlaying = false; // ???
    }
    // Methods 
    public void play() {
        isPlaying = true;
    }

    public void pause() {
        isPlaying = false;
    }

    public void stop() {
        isPlaying = false;

    }
    @Override
    public String toString() {
        return getInfo();
    }
    public abstract String getInfo();
    public abstract String toCSV();

}    
