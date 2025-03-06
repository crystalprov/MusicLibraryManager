import java.util.List;
import models.Album;
import models.MusicItem;
import models.MusicItemFactory;
import models.Podcast;
import models.Song;
import services.MusicLibrary;
import services.MusicLibraryFileHandler;

public class Main {
    public static void main(String[] args) {

        // objets tests
        Song test1 = new Song(5, "espresso", 2025, "Sabrina Carpenter", "Pop", 170 );
        Album test2 = new Album(6, "Short n sweet", 2024, "Sabrina Carpenter", "Island Record", 12);
        Podcast test3 = new Podcast(7, "Anything Goes", 2025, "Emma Chamberlain", "how to stop being a hater", 100);
        
        MusicLibrary musicLibrary = new MusicLibrary();

        List<MusicItem> loadedItems = MusicLibraryFileHandler.loadLibrary("POOphonia");
        for (MusicItem item : loadedItems){
            musicLibrary.addItem(item);
        }

        System.out.println("\n--- Contenu de la bibliothèque musicale après chargement du CSV ---");
        musicLibrary.listAllItems();
        
        musicLibrary.addItem(test1);
        musicLibrary.addItem(test2);
        musicLibrary.addItem(test3);

         // Create a new Song from CSV
        String[] newSongTest = {"song", "8", "Vampire", "2024", "Olivia Rodrigo", "Pop", "150"};
        MusicItem newSong = MusicItemFactory.createFromCSV(newSongTest);

        // Add the newly created song to the library
        if (newSong != null) {
            musicLibrary.addItem(newSong);
        }

        // List all items in the library
        System.out.println("Contenu final _____________");
        musicLibrary.listAllItems();
       
    }
}
