package models;

public class Song extends MusicItem {
    // Fields
    private String artist;
    private String genre;
    private int duration; // en secondes

    // Getters
    public String getArtist() {return artist;}
    public String getGenre() {return genre;}
    public int getDuration() {return duration;}

    // Setters
    public void setArtist(String artist){this.artist = artist;}
    public void setGenre(String genre){this.genre = genre;}
    public void setDuration(int duration){this.duration = duration;}

    // constructor
    public Song(int id, String title, int releaseYear, String artist, String genre, int duration) {
        super(id, title, releaseYear);
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
    }

    // METHODES A IMPLÉMENTÉ OBLIGATOIREMENT
    @Override
    public String getInfo() {
        return "Song [ID=" + getId() + ", Title=" + getTitle() + ", Release Year=" + getReleaseYear() + ", Artist=" + artist + ", Genre=" + genre + ", Duration=" + duration + "s]";
    }
    @Override
    public String toCSV() {
        // convertir sous forme de text pour fichier csv
        return "song," + getId() + "," + getTitle() + "," + getReleaseYear() + "," + artist + "," + genre + "," + duration; 
    }
    
}
