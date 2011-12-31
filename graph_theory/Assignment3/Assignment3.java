package Assignment3;

import GraphTools.*;

/**
 * @author Michael Welsman-Dinelle
 *
 * Computes the number of spanning trees of a given graph G.
 * GRA file must be provided as the first argument.
 * 
 * This implementation assumes that every edge in G is bidirectional,
 * but works with loops and multiple edges with the same endpoints.
 */
public class Assignment3 {
    public static void main( String args[] ) throws Exception {
    	    //Build a graph from the given GRA file.
    	    //This graph uses edge/vertex lists rather than the adjacency matrix.
    	    ListGraph g = new ListGraph( new Graph( args[0] ) );
    	    
    	    System.out.println( g );
    	    
    	    System.out.println("The number of spanning trees is: " + numSpan( g ) );
    }
    /* Function that uses Theorem 10.2 */
    public static int numSpan( ListGraph g ) {
    	   //the recursion terminates in the trivial case where there is one edge and one spanning tree
    	   if( g.getNonLoopEdgeCount() == 1 ) return 1;
    	   else {
    	   	   //The number of non-loop edges is greater than 1, so remove one and recurse
    	       //using theorem 10.2
    	       ListGraph g1 = new ListGraph( g );
    	       ListGraph g2 = new ListGraph( g );
    	       
    	       //Need to find an edge that is not a loop
    	       Edge cur = null;
    	       
    	       for( int i = 0; i < g.getEdgeSet().size(); i++ ) {
    	           cur = (Edge)g.getEdgeSet().get(i);
    	           if( !cur.isLoop() ) break; //we have found the edge we want
    	       }
    	       
    	       if( cur.isLoop() ) return 0; //only loop edges, so there are no spanning trees
    	       
    	       g1.contract( cur ); //contract an edge e
    	       g2.removeEdge( cur ); //remove an edge e
    	       
    	       //Uses the principle t(G) = t(G-e) + t(G.e), where e is not a loop
    	       return numSpan( g1 ) + numSpan( g2 );
    	   }
    }
}
