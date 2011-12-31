package webcrawler;

import java.net.*;
import java.io.*;

/** Performs HTTP requests and enforces request frequency limits so that
 *  servers are not flooded.
 *  
 *   NO LONGER USED SINCE HTMLPARSER DOES THIS.*/
public class PageGrabber {
	public static final int PORT = 80; //all pages are on port 80
	public static final long WAIT = 1000; //wait 1000 ms between requests
    private URL address;
    private long lastRequest;
    
    /** Construct a new page grabber for a given URL. */
    public PageGrabber( URL address ) {
    	this.address = address;
    	this.lastRequest = -WAIT; //permit the first immediately
    }
    
    /** Set the address of the page to be fetched. */
    public void setAddress( URL newAddress ) {
    	address = newAddress;
    }
    
    /** Send an HTTP request and return the result. */
    public String grab() throws IOException {
    	//Wait if the last request was too recent
    	while( (System.currentTimeMillis() - lastRequest) < WAIT );
    	
    	String result = "";
    	
    	Socket client = new Socket( address.getHost(), PORT);
        System.out.println("Connected to " + address.getHost() + ":" + PORT);
        
        BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream()));
        OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
        
        //Send the request
        out.write("GET " + address.getFile() + " HTTP/1.0\r\n\n");
        out.flush();
        System.out.println("Sent request for " + address.getFile() );
        
        //Read in the resulting data
        String input;
        
        while( ( input = in.readLine()) != null )
        	result += input + "\n";
        
        client.close();
        System.out.println("Done.");
        
        lastRequest = System.currentTimeMillis();
        
    	return result;
    }
    
    /* Test method: provide the URL as the first argument */
    public static void main( String args[] ) throws IOException, MalformedURLException {
    	PageGrabber page = new PageGrabber( new URL( args[0] ) );
    	
    	System.out.println( "\n\nOutput:\n" + page.grab() );
    	//Test the delay..
    	//System.out.println( "\n\nOutput:\n" + page.grab() );
    }
}
