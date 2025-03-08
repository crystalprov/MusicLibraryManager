package services;

import models.MusicItemFactory;
import models.Podcast;
import models.Song;
import models.Album;
import models.MusicItem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CommandProcessor {

    private static MusicLibrary musicLibrary = new MusicLibrary(); 

    public static void processCommands(MusicLibrary library) {
        if (library != null) {
            musicLibrary = library;
        }
        System.out.println("***** POOphonia: Welcome! *****");
        System.out.println("Library in file POOphonia loaded successfully.");
        System.out.println("Sourcing commands...");

        CommandProcessor processor = new CommandProcessor();
        processor.readCommands("data/commands.txt");
    }
    
    public void readCommands(String fileName) {  
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                executeCommand(line.trim());
            } 
        } catch (IOException error) {
            System.out.println("File reading error: " + fileName + " (" + error.getMessage() + ")"); 
        }   
    }
     
    public void executeCommand(String commandLine) {
        if (commandLine.isEmpty() || commandLine.startsWith("#")) {
            return; 
        }
        
        String[] parts = commandLine.split(" ", 2);
        String command = parts[0].toUpperCase();
        String commandInfos = (parts.length > 1) ? parts[1] : "";
    
        switch (command) {
            case "ADD":
                addOperator(commandInfos);
                break;
            case "LIST":
                listOperator();
                break;
            case "SEARCH":
                searchOperator(commandInfos);
                break;
            case "PLAY":
                playOperator(commandInfos);
                break;
            case "REMOVE":
                removeOperator(commandInfos);
                break;
            case "CLEAR":
                clearOperator();
                break;
            case "EXIT":
                exitOperator();
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
    }

    public static String format(MusicItem item) {
        if (item instanceof Song) {
            Song song = (Song) item;
            return "Song of " + song.getReleaseYear() + " " + song.getTitle() + 
                   " by " + song.getArtist();
        } else if (item instanceof Album) {
            Album album = (Album) item;
            return "Album " + album.getTitle() + " of " + album.getReleaseYear() + 
                   " with " + album.getNumberOfTracks() + " tracks by " + album.getArtist();
        } else if (item instanceof Podcast) {
            Podcast podcast = (Podcast) item;
            return "Podcast " + podcast.getTitle() + " episode " + podcast.getEpisodeNumber() +
                   " of " + podcast.getReleaseYear() + " on " + podcast.getTopic() + 
                   " by " + podcast.getHost();
        } else {
            return "Unknown music item";
        }
    }

    public void addOperator(String commandInfos) {
        String[] parts = commandInfos.split(",");

        if (parts.length < 3) {
            System.out.println("Invalid format for ADD command.");
            return;
        }

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        MusicItem item = MusicItemFactory.createFromCSV(parts);
        
        if (item != null) {
            musicLibrary.addItem(item);
            System.out.println(format(item) + " added to the library successfully.");
            musicLibrary.save();
        } else {
            System.out.println("Error creating music item from provided information.");
        }
    }
    
    public void listOperator() {
        if (musicLibrary.getItems().isEmpty()) {
            System.out.println("Library is empty.");
            return;
        }

        System.out.println("Sourcing test1...");
        System.out.println("Library:");
        for (MusicItem item : musicLibrary.getItems()) {
            System.out.println(item.toString());
        }
    }
    
    public void searchOperator(String request) {
        if (request.isEmpty()) {
            System.out.println("Search request cannot be empty.");
            return;
        }
        
        boolean found = false;
        for (MusicItem item : musicLibrary.getItems()) {
            if (item.getTitle().equalsIgnoreCase(request) || 
                String.valueOf(item.getId()).equals(request) || 
                item.toString().toLowerCase().contains(request.toLowerCase())) {
                System.out.println(format(item) + " is ready to PLAY");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("SEARCH " + request + " failed; no such item.");
        }
    }
    
    public void playOperator(String title) {
        if (title.isEmpty()) {
            System.out.println("Play request cannot be empty.");
            return;
        }

        MusicItem itemToPlay = null;
        for (MusicItem item : musicLibrary.getItems()) {
            if (item.getTitle().equalsIgnoreCase(title) || String.valueOf(item.getId()).equals(title)) {
                itemToPlay = item;
                break;
            }
        }

        if (itemToPlay != null) {
            System.out.println("Playing " + format(itemToPlay) + ".");
        } else {
            System.out.println("PLAY item: " + title + " failed; no such item.");
        }
    }
    
    public void removeOperator(String title) {
        if (title.isEmpty()) {
            System.out.println("Remove request cannot be empty.");
            return;
        }

        MusicItem itemToRemove = null;
        for (MusicItem item : musicLibrary.getItems()) {
            if (item.getTitle().equalsIgnoreCase(title) || String.valueOf(item.getId()).equals(title)) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove != null) {
            musicLibrary.removeItem(itemToRemove.getId());
            System.out.println("Removed " + format(itemToRemove) + " successfully.");
            musicLibrary.save();
        } else {
            System.out.println("REMOVE item " + title + " failed; no such item.");
        }
    }
    
    public void clearOperator() {
        musicLibrary.clearAllItems();
        System.out.println("Music library has been cleared successfully.");
        musicLibrary.save();
    }
    
    public void exitOperator() {
        musicLibrary.save();
        System.out.println("***** POOphonia: Goodbye! *****");
        System.exit(0);
    }
}
