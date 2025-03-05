package services;
import java.util.ArrayList;

import models.MusicItem;
import ui.POOphonia;
public class MusicLibrary {
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
    public MusicLibrary(ArrayList<MusicItem> items){
        this.items = items;
    }

    // Method to search a MusicItem with the id.
    public MusicItem searchById(int id) { 
        for (MusicItem item : items){
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

    // Retirer des éléments de musique présents dans la librairie.
    public void removeItem(int id){
        items.remove(id); 
    }

    // Lister les éléments de musique présents dans la librairie.
    public void listAllItems(){
        System.out.println(items); 
    }

    // jouer l'élément musical id
    public void playItem(int id){
        MusicItem item = searchById(id);
        if (item != null) {
            if (currentlyPlaying != null){
                System.out.println("Stopping " + currentlyPlaying.getTitle());
                currentlyPlaying.stop();
            }
            item.play();  // play item with ID given.
            currentlyPlaying = item;
            System.out.println("Lecture of " + item.getTitle());
        } else {
            System.out.println("The ID " + id + " is not associed with any items.");
        } 

        
    }
    public void pauseItem(){
        // Mettre sur pause 
        if (currentlyPlaying != null) {
            System.out.println("Pausing " + currentlyPlaying.getTitle());
            currentlyPlaying.pause();  // keeping the item in memory to replay later

        } else {
            System.out.println("No element is playing right now.");
        }
    }

    public void stopItem( ){
        // Arreter l'élément musical qui joue
    }   // Not keeping the item in memory to play later.
    
    public void clearAllItems(){
        // Vider la liste des élém musicaux présent dans la librairie.
    }

}

