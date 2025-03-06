package models;


public class Main {
    public static void main(String[] args) {

        // objets tests
        Song test1 = new Song(5, "espresso", 2025, "Sabrina Carpenter", "Pop", 170 );
        Album test2 = new Album(6, "Short n sweet", 2024, "Sabrina Carpenter", "Island Record", 12);
        Podcast test3 = new Podcast(7, "Anything Goes", 2025, "Emma Chamberlain", "how to stop being a hater", 100);
        
        
        String[] newSongTest = {"song", "8", "vampire", "2024", "Olivia Rodrigo", "Pop", "150"};
        System.out.println(MusicItemFactory.createFromCSV(newSongTest));

        // tests MusicItems
        System.out.println(test1.getInfo());
        System.out.println(test2.getInfo());
        System.out.println(test3.getInfo());
        
        
            
    }
}
