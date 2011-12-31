/*
 * Created on Sep 21, 2006 for CSCI 4115
 *
 * by Michael Welsman-Dinelle, B00202389
 */
package Assignment1;

import GraphTools.Graph;

public class Assignment1 {
	/* Takes a graph in GRA format and three arguments i, j, and k in integer form.
	 * Finds the number of walks of length k from i to j (indexed from 0) using Theorem 4.6. */
    public static void main( String args[] ) throws Exception {
    	    if( args.length < 4 )
    	    	    System.out.println("Need 4 arguments: GRA file name, i, j, and k.");
    	    
    	    //Read in the graph
    	    Graph g = new Graph( args[0] );
    	    System.out.println("Original graph:" );
    	    System.out.println( g );  //just to make sure that this is working
    	    
    	    int i = Integer.parseInt( args[ 1 ] );
    	    int j = Integer.parseInt( args[ 2 ] );
    	    int k = Integer.parseInt( args[ 3 ] );
    	    
    	    //Print out the number of walks
    	    System.out.println( "There are " + numWalks( g, i, j, k ) + " walks of length " + k +
    	    		" from node " + i + " to node " + j + "." );
    }
    /* Find the number of walks of length k from node i to node j in the graph g.
     * This is done by using Theorem 4.6., i.e. by finding A^K[i][j], where A is the
     * adjacency matrix. */
    public static int numWalks( Graph g, int i, int j, int k ) {
    	    int right[][] = g.getMatrix();
    	    int left[][] = right;
    	    int product[][] = new int[right.length][right.length];
    	    
    	    /* Multiply the matrix with itself K times.
    	     * In practice, right multiply the current product with the original matrix iteratively.
    	     * Straightforward (and somewhat slow) O(n^3) approach */
    	    for( int step = 0; step < k - 1; step++ ) {
    	    	    //Right-multiply our current product by the original matrix once more
    	    	    product = new int[right.length][right.length]; //re-initialize the product
    	    	    for( int x = 0; x < right.length; x++ ) {
    	    	    	    for( int y = 0; y < right.length; y++ ) {
    	    	    	    	    //For each cell, add up the product of each item in column X with the corresponding
    	    	    	    	    //item in row Y
    	    	    	    	    int sum = 0;
    	    	    	    	    for( int a = 0; a < right.length; a++ )
    	    	    	    	    	    sum += left[a][y] * right[x][a];
    	    	    	    	    	product[x][y] = sum;
    	    	    	    }
    	    	    }
    	    	    System.out.println( "G ^ " + (int)(step + 2 ) + ":" );
    	    	    System.out.println( new Graph( product ));
    	    	    
    	    	    left = product;  //the product will be multiplied 
    	    }
    	    
    	    return product[i][j];
    }
}
