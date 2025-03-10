package services;
import java.util.ArrayList;
import models.MusicItemFactory;
import models.Podcast;
import models.Song;
import models.Album;
import models.MusicItem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import ui.Message;

public class CommandProcessor {
    private static ArrayList<String> sourcingFiles = new ArrayList<>();
    // Create object to keep items (songs, podcasts, albums)
    private static MusicLibrary musicLibrary = new MusicLibrary();


    public static void processCommands(MusicLibrary library) {
        if (library != null) {
            musicLibrary = library;
        }
        Message.send("Library in file POOphonia loaded successfully.");

        CommandProcessor processor = new CommandProcessor();
        processor.readCommands("data/commands.txt");
    }
    
    public void readCommands(String fileName) {  
        // Opening + reading file
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                executeCommand(line.trim());
            } 
        // If can't find file
        } catch (IOException error) {
            Message.send("Sourcing " + fileName + " failed; file not found."); 
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
            case "SOURCE":
                sourceOperator(commandInfos);
                break;
            case "LOAD":
                loadOperator(commandInfos);
                break;
            case "SAVE":
                saveOperator(commandInfos);
                break;
            case "PAUSE":
                pauseOperator();
                break;
            case "STOP":
                stopOperator();
                break;

            default:
                // Invalid commands is entered.
                Message.send("Unknown command: " + command);
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
            Message.send("Invalid ADD command: " + commandInfos);
            return;
        }
    
        // Spaces handling
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
    
        // Create item
        MusicItem item = MusicItemFactory.createFromCSV(parts);
        
        if (item == null) {
            Message.send("Error creating music item from provided information.");
            return;
        }
        
        // Checking if ID is valid and unique
        if (item.getId() <= 0) {
            Message.send("Invalid music ID: " + item.getId());
            return;
        }
        if (musicLibrary.searchByArtistandTitle(String.valueOf(item.getId())) != null) {
            Message.send("ADD item failed; ID already used.");
            return;
        }
    
        // Release year validation
        if (item.getReleaseYear() < 1850 || item.getReleaseYear() > 2025) {
            Message.send("Invalid release year: " + item.getReleaseYear());
            return;
        }
    
        // Check if items are valid
        if (item instanceof Song) {
            Song song = (Song) item;
            if (song.getDuration() < 1 || song.getDuration() > 36000) {
                Message.send("Invalid duration: " + song.getDuration());
                return;
            }
        } else if (item instanceof Album) {
            Album album = (Album) item;
            if (album.getNumberOfTracks() < 1 || album.getNumberOfTracks() > 100) {
                Message.send("Invalid number of tracks: " + album.getNumberOfTracks());
                return;
            }
        } else if (item instanceof Podcast) {
            Podcast podcast = (Podcast) item;
            if (podcast.getEpisodeNumber() < 1 || podcast.getEpisodeNumber() > 500) {
                Message.send("Invalid episode number: " + podcast.getEpisodeNumber());
                return;
            }
        }
    
        // Checking if same title
        for (MusicItem existingItem : musicLibrary.getItems()) {
            if (existingItem.getTitle().equalsIgnoreCase(item.getTitle())) {
                Message.send("ADD item failed; item already in library.");
                return;
            }
        }
    
        // ADDING item
        musicLibrary.addItem(item);
        Message.send(format(item) + " added to the library successfully.");
        musicLibrary.save();
    }

    // Listing items in current library
    public void listOperator() {
        if (musicLibrary.getItems().isEmpty()) {
            Message.send("Library is empty.");
            return;
        }

        
        Message.send("Library:");
        for (MusicItem item : musicLibrary.getItems()) {
            Message.send(item.toString());
        }
    }

    public void searchOperator(String request) {

        if (request == null || request.trim().isEmpty()) {
            Message.send("Invalid SEARCH command: " + request);
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
        }

        if (foundItem != null) {
            Message.send(format(foundItem) + " is ready to PLAY");
        } else {
            Message.send("SEARCH item ID " + request + " failed; no such item.");
        }
    } 
 
    public void playOperator(String title) {
        if (title == null || title.trim().isEmpty()) {
            Message.send("Invalid PLAY command: " + title);
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
                Message.send(format(itemToPlay) + " is already playing.");
            } else {
                if (musicLibrary.getCurrentlyPlaying() != null) {
                    Message.send("Stopping " + musicLibrary.getCurrentlyPlaying().getTitle() + ".");
                    musicLibrary.getCurrentlyPlaying().stop();
                }
                itemToPlay.play();
                musicLibrary.setCurrentlyPlaying(itemToPlay);
                Message.send("Playing " + format(itemToPlay) + ".");
            }
        } else {
            Message.send("PLAY item: " + title + " failed; no such item.");
        }
    }
    
    public void removeOperator(String title) {
        if (title == null || title.isEmpty()) {
            Message.send("Invalid REMOVE command: " + title);
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
            Message.send("Removed " + format(itemToRemove) + " successfully.");
            musicLibrary.save();
        } else {
            Message.send("REMOVE item " + title + " failed; no such item.");
        }
    }
    
    public void sourceOperator(String fileName) {
        if (fileName.isEmpty()) {
            fileName = "data/commands.txt"; // default
        }
        if (sourcingFiles.contains(fileName)) {
            Message.send("Currently sourcing" + fileName + "; SOURCE ignored.");
            return;
        } 
        Message.send("Sourcing " + fileName + "...");
        
        try {
            sourcingFiles.add(fileName);
            readCommands(fileName);
            
        } catch (Exception error) {
            Message.send("Error reading command file: " + fileName);
        } finally {
            sourcingFiles.remove(fileName);
        }   
    }

    // Load all items from a specific file 
    public void loadOperator(String fileName){
        if (fileName.isEmpty()) {
            fileName = "data/POOphonia.csv"; // default
            Message.send("Loading from default library file.");
        } else { 
            Message.send("Loading from file:" + fileName);
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            musicLibrary.clearAllItems(); // Clear items existing before loading to only have the items in the file.
            
            while ((line = reader.readLine()) != null) {
                MusicItem item = MusicItemFactory.createFromCSV(line.split(","));
                if (item != null) {
                    musicLibrary.addItem(item);
                }
            }
            reader.close();
       
            Message.send("Library loaded successfully from " + fileName);

        } catch (IOException e) {
            Message.send("LOAD " + fileName + " failed; file not found.");
        }
    }

    public void saveOperator(String fileName){
        if (fileName.isEmpty()) {
            fileName = "POOphonia.csv"; // default
            Message.send("Saving to default library file.");
        } else {
            Message.send("Saving to file: " + fileName);
        }
    
        try {
            musicLibrary.save(fileName);
        } catch (Exception error) {
            Message.send("Error saving library to file: " + fileName);
        }
    }

    public void pauseOperator() {
        if (musicLibrary.getCurrentlyPlaying() == null) {
            Message.send("No item to PAUSE.");
            return;
        }
    
        MusicItem currentlyPlaying = musicLibrary.getCurrentlyPlaying();
        if (currentlyPlaying.getIsPlaying()) {
            currentlyPlaying.pause();
            Message.send("Pausing " + CommandProcessor.format(currentlyPlaying) + ".");
        } else {
            Message.send(CommandProcessor.format(currentlyPlaying) + " is already on pause.");
        }
    }

    public void stopOperator() {
        if (musicLibrary.getCurrentlyPlaying() == null) {
            Message.send("No item to STOP.");
            return;
        }
    
        MusicItem currentlyPlaying = musicLibrary.getCurrentlyPlaying();
        currentlyPlaying.stop();
        musicLibrary.setCurrentlyPlaying(null); // Not keeping in memory after stopping
        Message.send("Stopping " + CommandProcessor.format(currentlyPlaying) + ".");
    }
    
    public void clearOperator() {
        musicLibrary.clearAllItems();
        Message.send("Music library has been cleared successfully.");
    }
    
    public void exitOperator() {
        musicLibrary.save();
        Message.send("Exiting program...");
    }
}
