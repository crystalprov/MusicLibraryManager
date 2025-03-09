package services;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Album;
import models.MusicItem;
import models.Song;

import java.util.Iterator;

// import ui.POOphonia;
public class MusicLibrary {
    // Field
    private MusicItem currentlyPlaying;

    // Initialisation liste
    private ArrayList<MusicItem> items;

    // Getters
    public ArrayList<MusicItem> getItems() {return items;}
    public MusicItem getCurrentlyPlaying() {return currentlyPlaying;}

    // Setters
    public void setItems(ArrayList<MusicItem> items){this.items = items;}
    public void setCurrentlyPlaying(MusicItem currentlyPlaying){this.currentlyPlaying = currentlyPlaying;}

    // Constructor 
    public MusicLibrary() {
        this.items = new ArrayList<>();
    }

    public MusicItem searchByArtistandTitle(String idString) { 
        idString = idString.trim();
        System.out.println("DEBUG : " + idString);
        // Check if only numbers 
        if (idString.matches("\\d+")) { 
            int id = Integer.parseInt(idString); 
            return searchById(id);
            } 

        // If not an id : search by artist and title.
        for (MusicItem item : items) {
            if (item.getTitle().equalsIgnoreCase(idString)) {
                return item;  
            } 
            if (item instanceof Song && ((Song) item).getArtist().equalsIgnoreCase(idString)) {
                return item;  
            }
            if (item instanceof Album && ((Album) item).getArtist().equalsIgnoreCase(idString)) {                
                return item; 
            }
            
        }
        return null; // No item found
    }

    public MusicItem searchById(int id) {   
        for (MusicItem item : items) {
            
            if (item.getId() == id) {
                return item;      
            }
        } 
        return null;
    }

    // Ajouter un élément de musique à la librairie
    public void addItem(MusicItem item){
        items.add(item); 
    }

   
    public void removeItem(int id){
        Iterator<MusicItem> iterator = items.iterator();
    
        while (iterator.hasNext()) {

            MusicItem item = iterator.next();
        
            if (item.getId() == id) {
                iterator.remove();
                // Can't play a removed item.
                if (getCurrentlyPlaying() != null && getCurrentlyPlaying().getId() == id) {
                    setCurrentlyPlaying(null);
    
                }

            return;
        }
    }
    
        System.out.println("REMOVE item ID " + id + " failed; no such item.");
    }
    
    public void listAllItems(){
        for (MusicItem item : items) {
            System.out.println(item);
        }
    }

    public void playItem(int id){
        MusicItem item = searchById(id);

        if (item != null) {
            // Checking if a song is already playing to stop it 
            if (currentlyPlaying != null){
                System.out.println("Stopping " + currentlyPlaying.getTitle());
                currentlyPlaying.stop();
            }
            item.play();  // play item with ID given.
            currentlyPlaying = item;
            System.out.println("Playing> " + CommandProcessor.format(item) + ".");
        } else {
            System.out.println("PLAY item ID " + id + " failed; no such item.");
        }     
    }

    public void pauseItem(){
        if (currentlyPlaying != null) {
            if (!currentlyPlaying.getIsPlaying()) {
                System.out.println(CommandProcessor.format(currentlyPlaying) + " is already on pause.");
            } else {
                System.out.println("Pausing " + currentlyPlaying.getTitle() + ".");
                currentlyPlaying.pause();  
            }
        } else {
            System.out.println("No item to PAUSE.");
        }
    }

    public void stopItem( ){
        if (currentlyPlaying != null) {
            System.out.println("Stopping " + currentlyPlaying.getTitle() + ".");
            currentlyPlaying.stop();
            currentlyPlaying = null; // Not keeping in memory after stopping
        } else {
            System.out.println("No item to STOP.");
        }
    }

    public void clearAllItems(){
        if (!items.isEmpty()) {
            items.clear();
            currentlyPlaying = null; // Stop lecture after clearing.
        } else {
            System.out.println("Music library is already empty.");
        }
    }

    public void save(String filename) {
        if (filename.isEmpty()) {
            filename = "POOphonia.csv";
        }
        try (FileWriter writer = new FileWriter("POOphonia.csv")) {
            for (MusicItem item : getItems()) { 
                writer.write(item.toCSV() + "\n"); 
            }
            System.out.println("Library saved successfully to POOphonia.");
        } catch (IOException e) {
            System.out.println("Saving error: " + e.getMessage());
        }
    }
    public void save() {
        save("data/POOphonia.csv");
    }

    

}

