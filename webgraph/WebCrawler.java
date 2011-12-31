package webcrawler;

import java.net.*;
import java.util.*;

/** Web Crawler used to build a graph representing a website.
 *  for CSCI 4173, Assignment 4 (Bonus).
 *  
 *  Uses the open source library HTMLParser.
 *  
 *  This was mostly tested on my CSCI 4173 page, http://torch.cs.dal.ca:65400/index.html
 *  It works well on pages with only regular links.  Anchors and URLs with parameters, etc.
 *  are ignored when possible.
 *  
 *  @author Michael Welsman-Dinelle */
public class WebCrawler {
	private LinkExtractor extractor;
	private URL context;
	private Vector<String> nodeList;
	private Vector<PageLink> edgeList;
	
	/** Construct a crawler for a given website. */
    public WebCrawler( URL where ) {
    	context = where;
    	extractor = new LinkExtractor( where );
    	edgeList = new Vector<PageLink>();
    	nodeList = new Vector<String>();
    }
    
    /** Crawl through the children of this root, building a graph structure representing
     *  the given website (nodeList).  Make sure that loops are included but do not 
     *  follow them. */
    public void crawl() {
    	System.out.println( "Crawling " + context );
    	Vector<String> crawlList = new Vector<String>(); //nodes with children to visit
    	
    	crawlList.add( context.toExternalForm() ); //the starting point
    	
    	while( !crawlList.isEmpty() ) {
    		String cur = crawlList.remove( 0 );
            nodeList.add( cur ); //consider this node visited
    		
    	    Vector<URL> links = extractor.extractLocalLinks( cur );
    	    
    	    System.out.println( "Visiting " + cur + ".  Found links: " + links );
    	    
    	    while( links != null && !links.isEmpty() ) {
    	    	URL next = links.remove( 0 );
    	    	
    	    	String s = next.toExternalForm();
    	    	
    	    	PageLink link = new PageLink( cur, s );
    	    	
                //Add to the web graph if this link is new
		    	if( !edgeList.contains( link ) ) {
		    		edgeList.add( link );
		    	}
		    	//Crawl to this page if we haven't visited it yet
		    	if( !nodeList.contains( s ) && !crawlList.contains( s ) ) {
		    		crawlList.add( s );
		    	}
    	    }
    	}
    }
    /** Return the edge list of this site. */
    public Vector<PageLink> edgeList() {
    	return new Vector<PageLink>( edgeList );
    }
    /** Return the node list of this site. */
    public Vector<String> nodeList() {
    	return new Vector<String>( nodeList );
    }
    /** Get the adjacency matrix for this site. */
    public int[][] adjacencyMatrix() {
    	int[][] m = new int[ nodeList.size() ][ nodeList.size() ];
    	
    	for( int i = 0; i < edgeList.size(); i++ ) {
    		PageLink link = edgeList.get(i);
    		
    		m[ nodeList.indexOf( link.source ) ][ nodeList.indexOf( link.destination ) ]++;
    	}
    	
    	return m;
    }
    
    /* Runs the web crawler.  Argument 0 is the URL to be searched. */
    public static void main( String args[] ) throws MalformedURLException {
    	WebCrawler crawler = new WebCrawler( new URL( args[0] ) );
    	
    	crawler.crawl();
    	
    	//Edge/vertex representation
    	System.out.println( "Edge list: " );
    	System.out.println( crawler.edgeList() );
    	System.out.println();
    	System.out.println( "Vertex list: " );
    	System.out.println( crawler.nodeList() );
    	System.out.println();
    	
    	//Adjacency Matrix representation
    	//Note that a node is considered to be a distance of 1 from itself if
    	//an explicit link exists and 0 otherwise.
    	//All other 0 values correspond to a distance of infinity.
    	System.out.println( "Adjacency matrix: " );
    	int[][] m = crawler.adjacencyMatrix();
    	CrawlerMatrix.printMatrix( m );
    	System.out.println();
    	
    	//Apply the Floyd-Warshall algorithm to get the shorest paths
    	System.out.println( "Shortest Path matrix: " );
    	int[][] s = CrawlerMatrix.shortestPaths( m );
    	CrawlerMatrix.printMatrix( s );
    	System.out.println();
    	
    	//Generate the converted distance matrix (see instructions for Assignment 4)
    	System.out.println( "Converted distance matrix: " );
    	int[][] c = CrawlerMatrix.toConvertedDistance( s );
    	CrawlerMatrix.printMatrix( c );
    	System.out.println();
    	
    	//Generate the CID value
    	System.out.print( "CID: " );
    	CrawlerMatrix.printVector( CrawlerMatrix.sumColumns( c ) );
    	System.out.println();
    	
        //Generate the COD value
    	System.out.print( "COD: " );
    	CrawlerMatrix.printVector( CrawlerMatrix.sumRows( c ) );
    	System.out.println();
    	System.out.println();
    	
    	//Generate the compactness
    	System.out.println( "Compactness (Cp): " + CrawlerMatrix.findCompactness( c ));
    }
}
