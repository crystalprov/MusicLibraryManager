package ui;

/**
 * Utility class for sending messages to the console.
 * 
 * This class provides a simple static method to print messages, 
 * making it easy to manage console outputs across the application.
 * 
 * @author Francois Major
 * @version 1.0
 * @since 2025-01-31
 * 
 * © 2025 POOphonia. All rights reserved.
 */

public class Message {
    /**
     * Sends a message to the console.
     * 
     * @param s The message to be displayed.
     */
    public static void send( String s ) {
	System.out.println( s );
    }
}
