import services.CommandProcessor;
import services.MusicLibrary;

public class Main {

    public static void main(String[] args) {
    System.out.println("---- TEST ---- ");
    
    // Initialiser la bibliothèque musicale
    MusicLibrary library = new MusicLibrary();

    // Lancer le processeur de commandes
    CommandProcessor.processCommands(library);
    }
}
    

