package services;

import models.MusicItemFactory;
import models.MusicItem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CommandProcessor {

    // Initialization of library
    private static MusicLibrary musicLibrary = new MusicLibrary(); 

    // Static method 
    public static void processCommands(MusicLibrary library) {
        if (library != null) {
             musicLibrary = library;
        }
        System.out.println(" CommandProcessor ready ");
    }
    
    public void readCommands(String fileName) {  
        System.out.println("Reading commands from " + fileName);

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
        System.out.println("Command received: " + commandLine);
        
        // Ignore empty lines and lines starting with "#"
        if (commandLine.isEmpty() || commandLine.startsWith("#")) {
            return; 
        }
        
        String[] parts = commandLine.split(" ", 2); // cut in 2 parts
        String command = parts[0].toUpperCase(); // first part of line 
        String commandInfos = (parts.length > 1) ? parts[1] : ""; // second part of line (everything else)

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

    public void addOperator(String commandInfos) {
        String[] parts = commandInfos.split(",");

        if (parts.length != 7) {
            System.out.println("Invalid format. Expected 7 comma-separated values.");
            return;
        }

        // Trim whitespace from parts
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        MusicItem item = MusicItemFactory.createFromCSV(parts);
        
        if (item != null) {
            musicLibrary.addItem(item);
            System.out.println(item + " added");
            musicLibrary.save(); // Save library after adding item
        } else {
            System.out.println("Error creating music item from provided information");
        }
    }
    
    public void listOperator() {
        if (musicLibrary.getItems().isEmpty()) {
            System.out.println("Library is empty");
            return;
        }
        
        if (musicLibrary.getItems().size() != 6) {
            System.out.println("6 items must be displayed.");
        }
        
        System.out.println("Library contents:");
        for (MusicItem item : musicLibrary.getItems()) {
            System.out.println(item);
        }
    }
    
    public void searchOperator(String request) {
        if (request.isEmpty()) {
            System.out.println("Search query cannot be empty");
            return;
        }
        
        boolean found = false;
        for (MusicItem item : musicLibrary.getItems()) {
            if (item.toString().toLowerCase().contains(request.toLowerCase())) {
                System.out.println(item);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No items found matching: " + request);
        }
    }
    
    public void playOperator(String title) {
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty");
            return;
        }
        
        MusicItem itemToPlay = null;
        for (MusicItem item : musicLibrary.getItems()) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                itemToPlay = item;
                break;
            }
        }
        
        if (itemToPlay != null) {
            System.out.println("Playing: " + itemToPlay);
            // Here you would call a method to actually play the item
        } else {
            System.out.println("Item not found: " + title);
        }
    }
    
    public void removeOperator(String title) {
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty");
            return;
        }
        
        MusicItem itemToRemove = null;
        for (MusicItem item : musicLibrary.getItems()) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                itemToRemove = item;
                break;
            }
        }
        
        if (itemToRemove != null) {
            int id = itemToRemove.getId();
            musicLibrary.removeItem(id);
            System.out.println("Removed: " + itemToRemove);
            musicLibrary.save(); // Save library after removing item
        } else {
            System.out.println("Item not found: " + title);
        }
    }
    
    public void clearOperator() {
        musicLibrary.clearAllItems();
        System.out.println("Library cleared");
        musicLibrary.save(); // Save empty library
    }
    
    public void exitOperator() {
        musicLibrary.save(); // Save before exiting
        System.out.println("Exiting application");
        // In a real application, you might want to call System.exit(0) here
    }
}
