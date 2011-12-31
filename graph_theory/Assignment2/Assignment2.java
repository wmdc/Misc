package Assignment2;

import java.util.Vector;
import GraphTools.*;

/**
 * @author mwelsman
 */
public class Assignment2 {
	/** Takes the name of a GRA file as an argument.
	 *  Determines whether or not the given graph is bipartite.
	 *  If it is, prints out a bipartition.  Otherwise, prints out an odd cycle. */
	public static void main( String args[] ) throws Exception {
		//First, look for an odd cycle.  If there is an odd cycle then the graph is
		//not bipartite and we are done.
		Graph g = new Graph( args[0] );
		
		System.out.println( "Loaded graph " + args[0] + ": " );
		System.out.println( g );
		
		Bipartition b = g.getBipartition();
		
		if( g.isBipartite( b ) ) {
			System.out.println( "This graph does not have an odd cycle." );
		    	System.out.println( "Bipartition: " + b );
		}
		else {
			System.out.println( "This graph is not bipartite." );
			System.out.println( "Odd cycle: " + g.getOddCycle( b ) );
		}
	}
}

