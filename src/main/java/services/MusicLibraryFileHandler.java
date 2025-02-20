package services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import models.MusicItem;
import models.MusicItemFactory;
import ui.Message;

/**
 *
 * MusicLibraryFileHandler handles the persistence of the music library by saving and
 * loading music items to and from a CSV file.
 * 
 * This class allows for:
 * - Saving a list of MusicItem objects to a CSV file.
 * - Loading MusicItem objects from a CSV file into a list.
 * 
 * Each MusicItem subclass is responsible for its own CSV conversion.
 *                                                                                                                     
 * @author Francois Major
 * @version 1.0
 * @since 2025-01-31
 *
 * © 2025 POOphonia. All rights reserved.
*/

public class MusicLibraryFileHandler {

    /** Default file path where the music library is stored. */
    private static final String DEFAULT_FILE = "POOphonia";

    /**
     * Save a list of MusicItem objects to a CSV file.
     * 
     * @param items The list of music items to be saved.
     * @param filePath The name of the file (without extension). If empty, the default file is used.
    */
    public static void saveLibrary( List<MusicItem> items, String libraryName ) {
	// If no filePath is specified, use the default CSV file
	String filePath = "data/";
	if( libraryName.equals( "" ) )
	    libraryName = DEFAULT_FILE;
	filePath += libraryName + ".csv"; // Ensure the file is saved in the "data" directory
	// Attempt to write the music library to a file
        try( PrintWriter writer = new PrintWriter( new FileWriter( filePath ) ) ) {
            for( MusicItem item : items ) {
                writer.println( item.toCSV() ); // Each MusicItem handles its own CSV conversion
            }
            Message.send( "Library saved successfully to " + libraryName + "." );
        } catch( IOException e ) {
            Message.send( "Error saving library: " + libraryName );
        }
    }

    /**
     * Load a list of music items from a CSV file.
     * 
     * @param filePath The name of the file (without extension). If empty, the default file is used.
     * @return A list of MusicItem instances loaded from the file.
    */
    public static List<MusicItem> loadLibrary( String libraryName ) {
	// If no filePath is specified, use the default CSV file
	String filePath = "data/";
	if( libraryName.equals( "" ) )
	    libraryName = DEFAULT_FILE;
	filePath += libraryName + ".csv"; // Ensure the file is saved in the "data" directory
        List<MusicItem> items = new ArrayList<>();
        File file = new File( filePath );

	// If the file does not exist, return an empty list and log a message
        if( !file.exists() ) {
	    if( libraryName == DEFAULT_FILE )
		Message.send( "No default library " + libraryName + " found; Starting fresh." );
	    else
		Message.send( "No library " + libraryName + " found." );
            return items;
        }

	// Attempt to read the music library from the file
        try( BufferedReader reader = new BufferedReader( new FileReader( filePath ) ) ) {
            String line;
	    int id = 1;
            while( ( line = reader.readLine() ) != null ) {
                String[] parts = line.split( "," ); // Split CSV line into fields
		MusicItem item = MusicItemFactory.createFromCSV( parts ); // Convert CSV data to a MusicItem
                if( item != null ) {
                    items.add( item ); // Add valid item to the list
                }
            }
            Message.send( "Library in file " + libraryName + " loaded successfully." );
        } catch( IOException e ) {
            Message.send( "Error loading library: " + e.getMessage() );
        }
        return items;
    }
}
