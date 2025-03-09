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
    
        // Check if command has at least 7 parts
        if (parts.length < 7) {
            System.out.println("Invalid ADD command: " + commandInfos);
            return;
        }
    
        // Spaces handling
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
    
        // Create item
        MusicItem item = MusicItemFactory.createFromCSV(parts);
        
        if (item == null) {
            System.out.println("Error creating music item from provided information.");
            return;
        }
        
        // Checking if ID is valid and unique
        if (item.getId() <= 0) {
            System.out.println("Invalid music ID: " + item.getId());
            return;
        }
        if (musicLibrary.searchByArtistandTitle(String.valueOf(item.getId())) != null) {
            System.out.println("ADD item failed; ID already used.");
            return;
        }
    
        // Release year validation
        if (item.getReleaseYear() < 1850 || item.getReleaseYear() > 2025) {
            System.out.println("Invalid release year: " + item.getReleaseYear());
            return;
        }
    
        // Check if items are valid
        if (item instanceof Song) {
            Song song = (Song) item;
            if (song.getDuration() < 1 || song.getDuration() > 36000) {
                System.out.println("Invalid duration: " + song.getDuration());
                return;
            }
        } else if (item instanceof Album) {
            Album album = (Album) item;
            if (album.getNumberOfTracks() < 1 || album.getNumberOfTracks() > 100) {
                System.out.println("Invalid number of tracks: " + album.getNumberOfTracks());
                return;
            }
        } else if (item instanceof Podcast) {
            Podcast podcast = (Podcast) item;
            if (podcast.getEpisodeNumber() < 1 || podcast.getEpisodeNumber() > 500) {
                System.out.println("Invalid episode number: " + podcast.getEpisodeNumber());
                return;
            }
        }
    
        // Checking if same title
        for (MusicItem existingItem : musicLibrary.getItems()) {
            if (existingItem.getTitle().equalsIgnoreCase(item.getTitle())) {
                System.out.println("ADD item failed; item already in library.");
                return;
            }
        }
    
        // ADDING item
        musicLibrary.addItem(item);
        System.out.println(format(item) + " added to the library successfully.");
        musicLibrary.save();
    }

    public void listOperator() {
        if (musicLibrary.getItems().isEmpty()) {
            System.out.println("Library is empty.");
            return;
        }

        
        System.out.println("Library:");
        for (MusicItem item : musicLibrary.getItems()) {
            System.out.println(item.toString());
        }
    }

    public void searchOperator(String request) {

        if (request == null || request.trim().isEmpty()) {
            System.out.println("Invalid SEARCH command: " + request);
            return;
        }
        
        MusicItem foundItem;
        request = request.trim();
        

        // if number, search by ID
        if (request.matches("\\d+")) {
            int requestInt = Integer.parseInt(request);
            foundItem = musicLibrary.searchById(requestInt);            
        } else {
            // Search by artist / title
            foundItem = musicLibrary.searchByArtistandTitle(request);
            System.out.println("DEBUG " + request);
        }

        if (foundItem != null) {
            System.out.println(format(foundItem) + " is ready to PLAY");
        } else {
            System.out.println("SEARCH item ID " + request + " failed; no such item.");
        }
    } 
 
    public void playOperator(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Invalid PLAY command: " + title);
            return;
        }

        title = title.trim();
        MusicItem itemToPlay = null;

        // Search by id if number
        if (title.matches("\\d+")) {
            itemToPlay = musicLibrary.searchById(Integer.parseInt(title));
        } else {
            // Search by artist / title
            itemToPlay = musicLibrary.searchByArtistandTitle(title);
        }

        // check if item has been found
        if (itemToPlay != null) {
            if (musicLibrary.getCurrentlyPlaying() != null && musicLibrary.getCurrentlyPlaying().equals(itemToPlay)) {
                System.out.println(format(itemToPlay) + " is already playing.");
            } else {
                if (musicLibrary.getCurrentlyPlaying() != null) {
                    System.out.println("Stopping " + musicLibrary.getCurrentlyPlaying().getTitle() + ".");
                    musicLibrary.getCurrentlyPlaying().stop();
                }
                itemToPlay.play();
                musicLibrary.setCurrentlyPlaying(itemToPlay);
                System.out.println("Playing " + format(itemToPlay) + ".");
            }
        } else {
            System.out.println("PLAY item: " + title + " failed; no such item.");
        }
    }
    
    public void removeOperator(String title) {
        if (title == null || title.isEmpty()) {
            System.out.println("Invalid REMOVE command: " + title);
            return;
        }

        title = title.trim();
        MusicItem itemToRemove = null;

        // Search by id if number
        if (title.matches("\\d+")) {  
            itemToRemove = musicLibrary.searchById(Integer.parseInt(title));
        } else {
            // Search by artist / title
            itemToRemove = musicLibrary.searchByArtistandTitle(title);
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
    }
    
    public void exitOperator() {
        musicLibrary.save();
        System.out.println("Exiting program...");
    }
}
