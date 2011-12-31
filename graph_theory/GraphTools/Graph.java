package GraphTools;

/**
 * Minimalist Graph implementation
 */

import java.io.*;
import java.util.Vector;

public class Graph {
	private int matrix[][];
	
	/* Create a new graph of the given size */
	public Graph( int size ) {
		matrix = new int[size][size];
	}
	
	/* Create a new graph from an adjacency matrix */
	public Graph( int[][] adjacency ) {
		matrix = adjacency;
	}
	
	/* Create a new graph from a GRA file */
	public Graph( String file ) throws IOException {
		BufferedReader graphFile = new BufferedReader( new FileReader( file ) );
		
		int size = Integer.parseInt( graphFile.readLine() );
		matrix = new int[size][size];
		//This is a very inefficient but easy approach
		for( int i = 0; i < size; i++ ) {
			String line = graphFile.readLine();

			for( int j = 0; j < size; j++ )
				matrix[i][j] = Integer.parseInt( line.charAt( j * 2 ) + "" ); //factor in the spaces
		}
	}
	
	/* Change a value in the adjacency matrix */
	public void edge( int v1, int v2, int adjacent ) {
		matrix[v1][v2] = adjacent;
	}
	
	/* Return a value in the adjacency matrix */
	public int adjacency( int v1, int v2 ) {
		return matrix[v1][v2];
	}
	/* Get the adjacency matrix */
	public int[][] getMatrix() {
		return matrix;  //passed by value, not by reference
	}
	/** Return the number of vertices in this graph. */
	public int size() {
		return matrix.length;
	}
	
	/** Return a potential bipartition of the given graph. */
    public Bipartition getBipartition() {
    	    Vector x = new Vector();
    	    Vector y = new Vector();
    	    
    	    //Use a BFS traversal beginning at the first node in the edge matrix
    	    //For every other node v, if d(u,v) is even, add it to X.  Otherwise, add it to Y.
    	    Vector q = new Vector(); //active vertices
    	    Vector s = new Vector(); //visited vertices
    	    
    	    if( size() != 0 ) {  //add the first vertex
    	        q.add( new Integer( 0 ));
    	        q.add( new Integer( 0 )); //distance from start vertex is zero
    	    }
    	    
    	    //Traverse the graph while vertices remain
    	    while( !q.isEmpty() ) {
    	    	    //take out the most recent node
    	    	    int cur = ((Integer)q.remove(0)).intValue();
    	    	    int curDist = ((Integer)q.remove(0)).intValue();
    	    	    
    	    	    if( curDist % 2 == 0 ) 
    	    	    	    x.add( new Integer( cur ));  //even
    	    	    else 
    	    	    	    y.add( new Integer( cur ));  //odd
    	    	    
    	    	    s.add( new Integer( cur ));  //this vertex has now been visited
    	    	    
    	    	    //Add all neighbours of the current vertex that have not yet been visited
    	    	    for( int i = 0; i < size(); i++ ) {
    	    	    	    //add a vertex if it is adjacent to the current vertex and has not been visited
    	    	    	    if( !s.contains( new Integer( i ) ) && matrix[i][cur] == 1 ) {
    	    	    	    	    q.add( new Integer( i ) );
    	    	    	    	    q.add( new Integer( curDist + 1 ) );  //one farther away from the beginning
    	    	    	    }
    	    	    }
    	    }
    	    
    	    Bipartition b = new Bipartition( x, y );
    	    return b;
    }
    /** Check to see that the two sets of the given bipartition are in fact independent,
     *  meaning that no vertex is adjacent to another in the same set. */
    public boolean isBipartite( Bipartition b ) {
    	   //Check each edge to see if both ends fall within one set
    	   for( int i = 0; i < matrix.length; i++ )
    	   	   for( int j = 0; j < matrix.length; j++ ) {
    	   	   	   if( (b.x.contains( new Integer( i ) ) && b.x.contains( new Integer( j ) )) || 
    	   	   	       (b.y.contains( new Integer( i ) ) && b.y.contains( new Integer( j ) )) ) {
    	   	   	   	   return false;
    	   	   	   }
    	   	   	   	   
    	   	   }
    	   //All endpoints go from one set to the other
    	   return true;
    }
    
    /** Return an odd cycle if this graph has one.  Otherwise, return null. */
    public Vector getOddCycle( Bipartition b ) {
    	    //For each vertex, traverse the graph until an endpoint is reached or until a cycle is found.
        //Keep track of the path to the original vertex and the distance.  If the distance is odd, print out the
    	    //odd cycle.
    	
    	    Vector path = new Vector();
    	
    	    for( int i = 0; i < matrix.length; i++ ) {
    	    	    Vector q = new Vector(); //active vertices
        	    Vector s = new Vector(); //visited vertices
        	    
        	    	q.add( new Integer( i ));
    	        	q.add( new Integer( i )); //parent is node i
    	        	q.add( new Integer( 0 )); //distance from i is zero
    	        	
             //Traverse the graph while vertices remain
    	    	    while( !q.isEmpty() ) {
    	    	    	    //take out the most recent node
    	    	    	    int cur = ((Integer)q.remove(0)).intValue();
    	    	    	    int curPar = ((Integer)q.remove(0)).intValue();
    	    	    	    int curDist = ((Integer)q.remove(0)).intValue();
    	    	    	    
    	    	    	    s.add( new Integer( cur ));  //this vertex has now been visited
    	    	    	    s.add( new Integer( curPar ));
    	    	    	    
    	    	    	    //Add all neighbours of the current vertex that have not yet been visited
    	    	    	    //If we find vertex i again, we have found a cycle.
    	    	    	    //If the distance from i to i is odd, we have an odd cycle
    	    	    	    for( int j = 0; j < size(); j++ ) {
    	    	    	    	    //add a vertex if it is adjacent to the current vertex and has not been visited
    	    	    	    	    if( i == j && matrix[i][j] == 1 && curDist % 2 == 0 ) { //odd cycle
    	    	    	    	    	    //note that the distance is to i's parent, so if it's even then the
    	    	    	    	        //distance from i to i is odd.
    	    	    	    	    	
    	    	    	    	    	    //Build the path by taking out successive parents from S.
    	    	    	    	    	    path.add( new Integer( i ) ); //ends at i
    	    	    	    	    	    path.add( new Integer( cur ) ); //next is cur
    	    	    	    	    	    //Move backwards through the parents
    	    	    	    	    	    for( int k = 0; k < curDist; k++ ) {
    	    	    	    	    	    	    path.add( new Integer( curPar ) );
    	    	    	    	    	    	    int next = s.indexOf( new Integer( curPar ) );
    	    	    	    	    	    	    
    	    	    	    	    	    	    curPar = ((Integer)s.get( next + 1 )).intValue();
    	    	    	    	    	    }
    	    	    	    	    	    //we are done
    	    	    	    	    	    return path;
    	    	    	    	    }
    	    	    	    	    else if( !s.contains( new Integer( i ) ) && matrix[i][j ] == 1 ) {
    	    	    	    	    	    q.add( new Integer( i ) );
    	    	    	    	    	    q.add( new Integer( cur ) ); //cur is the parent
    	    	    	    	    	    q.add( new Integer( curDist + 1 ) );  //one farther away from the beginning
    	    	    	    	    }
    	    	    	    }
    	    	    }
    	    }
    	
    	    return path;
    }
    
	/* String representation */
	public String toString() {
		String s = matrix.length + "\n";
		
		for( int i = 0; i < matrix.length; i++ ) {
			for( int j = 0; j < matrix.length; j++ )
				s += matrix[i][j] + " ";
			s += "\n";
		}
		
		return s;
	}
}