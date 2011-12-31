package GraphTools;

/**
 * @author mwelsman
 *
 * Represents a single, unweighed, bidirectional edge with two endpoints.
 */
public class Edge {
    Vertex v1, v2;
    /* Construct an edge with two endpoints. */
    public Edge( Vertex v1, Vertex v2 ) {
    	    this.v1 = v1;
    	    this.v2 = v2;
    }
    /* Return the endpoints of this edge */
    public Vertex getV1() {
    	    return v1;
    }
    public Vertex getV2() {
	    return v2;
    }
    /* Change the endpoints of this edge */
    public void setV1( Vertex v ) {
	    v1 = v;
    }
    public void setV2( Vertex v ) {
        v2 = v;
    }
    /* Is this edge a loop?  Does v1 = v2?*/
    public boolean isLoop() {
    	    return v1.equals( v2 );
    }
    
    /* Return true if the endpoints of this edge are the same as the endpoints
     * of the given edge. */
    public boolean equals( Object e ) {
    	    if( !(e instanceof Edge) ) return false;
    	    
    	    Edge comp = (Edge)e;
    	    return 	v1.equals( comp.getV1() ) && v2.equals( comp.getV2() ) ||
			  	v2.equals( comp.getV2() ) && v2.equals( comp.getV1() );  //order unimportant
    }
    /* String representation */
    public String toString() {
    	    return "[" + v1 + ", " + v2 + "]";
    }
}
