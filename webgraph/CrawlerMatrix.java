package webcrawler;

/** Utility functions involving web crawler matrices. */
public class CrawlerMatrix {
	/** Takes an adjacency matrix and returns a shortest path matrix.
	 *  This is the Floyd-Warshall algorithm. */
    public static int[][] shortestPaths( int[][] a ) {
    	int[][] r = new int[ a.length ][ a.length ];
    	
    	for( int i = 0; i < a.length; i++ ) {
    		for( int j = 0; j < a.length; j++ ) {
    			for( int k = 0; k < a.length; k++ ) {
    				r[j][k] = Math.min( a[j][k], a[j][i] + a[i][k] );
    			}
    		}
    	}
    	
    	return r;
    }
    /** Generate a converted distance matrix. */
    public static int[][] toConvertedDistance( int[][] m ) {
    	int[][] c = new int[ m.length ][ m.length ];
    	//Swap in the "n" values
    	for( int i = 0; i < m.length; i++ ) {
    		for( int j = 0; j < m.length; j++ ) {
    			if( m[i][j] == 0 )
    				c[i][j] = m.length;
    			else
    		        c[i][j] = m[i][j];
    		}
    	}
    	//Make sure the distance from a node to itself is 0.
    	for( int i = 0; i < m.length; i++ )
    		c[i][i] = 0;
    	
    	return c;
    }
    
    /** Calculate the sums of the columns of a matrix (CID values). */
    public static int[] sumColumns( int[][] m ) {
    	int[] s = new int[ m.length ];
    	//Swap in the "n" values
    	for( int i = 0; i < m.length; i++ ) {
    		int sum = 0;
    		for( int j = 0; j < m.length; j++ ) {
    			sum += m[i][j];
    		}
    		s[i] = sum;
    	}
    	return s;
    }
    
    /** Calculate the sums of the rows of a matrix (COD values). */
    public static int[] sumRows( int[][] m ) {
    	int[] s = new int[ m.length ];
    	//Swap in the "n" values
    	for( int i = 0; i < m.length; i++ ) {
    		int sum = 0;
    		for( int j = 0; j < m.length; j++ ) {
    			sum += m[j][i];
    		}
    		s[i] = sum;
    	}
    	return s;
    }
    /** Calculate compactness from a converted distance matrix.
     *  See "Structural Analysis of Hypertexts". */
    public static double findCompactness( int[][] c ) {
    	//1. Find the maximum value C in the matrix and the sum of entries.
    	int C = 0;
    	int sum = 0;
    	
    	for( int i = 0; i < c.length; i++ ) {
    		for( int j = 0; j < c.length; j++ ) {
    			sum += c[i][j];
    			if( c[i][j] > C )
    				C = c[i][j];
    		}
    	}
    	
    	//2. Calculate n^2 - n, used in both MAX and MIN calculation
    	int n2n = c.length * c.length - c.length;
    	
    	//3. Calculate max and min
    	int max = n2n * C;
    	int min = n2n;
    	
    	//4. Calculate and return Cp, the compactness.
    	//   This is given by the equation:
    	//   Cp = ( Max - sum of entries in c[][] ) / ( Max - Min )
    	return ( max - sum ) / (double)( max - min );
    }
    
    /** Print a one-dimensional array */
    public static void printVector( int[] v ) {
    	System.out.print("| ");
    	for( int i = 0; i < v.length; i++ )
    		System.out.print( v[i] + " " );
    	System.out.print("|");
    }
    
    /** Print a square 2 dimensional matrix s */
    public static void printMatrix( int[][] s ) {
    	for( int i = 0; i < s.length; i++ ) {
    		System.out.print("| ");
    		for( int j = 0; j < s.length; j++ ) {
    			System.out.print( s[i][j] + " ");
    		}
    		System.out.println("|");
    	}
    }
}
