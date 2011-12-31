package webcrawler;

import java.net.*;
import java.util.*;
import org.htmlparser.Parser;
import org.htmlparser.NodeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.Tag;

/** Given the URL of an HTML document, extracts unique local links useful for crawling purposes.
 *  Built using HTML Parser, http://htmlparser.sourceforge.net/ 
 *  
 *  This class enforces request delays as well, preventing servers from being flooded with requests. */
public class LinkExtractor {
	public static final long WAIT = 1000; //wait 1000 ms between requests
	private URL context;
	private long lastRequest;
	
	/** Create a link extractor for a given host.  It is important to establish
	 *  the host so that relative links can be interpreted. */
	public LinkExtractor( URL host ) {
		context = host;
		lastRequest = -WAIT;
	}
	
	/** Return a list of local pages linked to from an HTML document. */
    public Vector<URL> extractLocalLinks( String url ) {
    	//Enforce the request delay
    	while( (System.currentTimeMillis() - lastRequest) < WAIT );
    	
    	Vector<URL> result = new Vector<URL>();
    	
    	NodeFilter filter = new NodeClassFilter (LinkTag.class);
    	
    	try {
    	    Parser parser = new Parser ( url );
    	    NodeList list = parser.extractAllNodesThatMatch(filter);
    	
    	for (int i = 0; i < list.size (); i++) {
    		Tag link = (Tag)list.elementAt(i);
    		
    		try {
    		    URL where = new URL( context, link.getAttribute("href") );

    		    //iff unique local link...
    		    if( !result.contains( where ) && where.getHost().equals( context.getHost() ) ) {
    		        result.add( where );
    		    }
    		}
    	    catch( MalformedURLException e ) {
    	    	System.err.println("Warning: rejecting " + link.getAttribute("href") );
    	    }
    	}
    	}
    	catch( ParserException ex ) { System.out.println(ex); return null;}
    	
    	lastRequest = System.currentTimeMillis();

    	return result;
    }
    /* Test code. */
    public static void main( String args[] ) throws ParserException, MalformedURLException {
    	LinkExtractor l = new LinkExtractor( new URL( "http://torch.cs.dal.ca:65400" ));
    	
    	Vector<URL> result = l.extractLocalLinks( "http://torch.cs.dal.ca:65400/index.html" );
    	
    	System.out.println( result );
    }
}
