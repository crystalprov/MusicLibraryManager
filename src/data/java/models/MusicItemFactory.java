package models;

/**
 * MusicItemFactory is a class for creating MusicItem objects from CSV data.
 * This class provides a static method to generate instances of different
 * music items, such as Song, Album, and Podcast, based on a CSV string.
 *
 * @author Francois Major
 * @version 1.0
 * @since 2025-01-31
 * 
 * © 2025 POOphonia. All rights reserved.
 */

public class MusicItemFactory {

    /**
     * Create a MusicItem instance from a CSV-formatted string array.
     *
     * @param parts a string array representing the CSV fields of a music item.
     *              - parts[0]: The type of the music item (song, album, podcast)
     *              - parts[1]: The ID (integer)
     *              - parts[2]: The title (string)
     *              - parts[3]: The release year (integer)
     *              - Additional fields depend on the item type:
     *                  * Song: artist, genre, duration
     *                  * Album: artist, label, numberOfTracks
     *                  * Podcast: host, topic, episodeNumber
     * @return a MusicItem instance (Song, Album, or Podcast) if valid, otherwise null.
     */
    public static MusicItem createFromCSV( String[] parts ) {
    // Variables declaration
        int id = Integer.parseInt(parts[1]); 
        String title = parts[2]; 
        int releaseYear = Integer.parseInt(parts[3]);
	/**
	 * Each music item type (song, album, podcast) requires exactly **7 fields** in the CSV format.
	 * If fewer than 7 fields are provided, the data is incomplete, and we return null.
	 */
        if( parts.length != 7 ) return null;

	// We have 7 parts, process each MusicItem types
	//   MusicItem type is in parts[0]
        switch( parts[0].toLowerCase() ) {
	case "song":
	    return new Song( id, title, releaseYear, parts[4], parts[5], Integer.parseInt(parts[6]));
	    
	case "album":
	    return new Album( id, title, releaseYear, parts[4], parts[5], Integer.parseInt(parts[6]) );
	    
	case "podcast":
	    return new Podcast( id, title, releaseYear, parts[4], parts[5], Integer.parseInt(parts[6]));
	    
	default: return null; // if the type is not recognized, return null
        }
    }
}