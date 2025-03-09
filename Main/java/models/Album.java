package models;

public class Album extends MusicItem {
    // Fields
    private String artist;
    private int numberOfTracks;
    private String label;

    // Getters
    public String getArtist(){return artist;}
    public int getNumberOfTracks(){return numberOfTracks;}
    public String getLabel(){return label;}

    // Setters
    public void setArtist(String artist){this.artist = artist;}
    public void setNumberOfTracks(int numberOfTracks){this.numberOfTracks = numberOfTracks;}
    public void setLabel(String label){this.label = label;}
    
// album,2,Thriller,1982,Michael Jackson,Epic Records,9

    // Constructor
    public Album(int id, String title, int releaseYear, String artist, String label, int numberOfTracks){
        super(id, title, releaseYear);
        this.artist = artist;
        this.numberOfTracks = numberOfTracks;
        this.label = label;
    }

    // A IMPLEMENTÉ OBLIGATOIREMENT

    @Override
    public String getInfo() {
        return "Album [ID=" + getId() + ", Title=" + getTitle() + ", Release Year=" + getReleaseYear() + ", Artist=" + artist + ", Tracks=" + getNumberOfTracks() + ", Label=" + label + "]";
    }
    // album,2,Thriller,1982,Michael Jackson,Epic Records,9
    public String toCSV() {
        return "album," + getId() + "," + getTitle() + "," + getReleaseYear() + "," + artist + "," + label + "," + numberOfTracks;
    }
}
