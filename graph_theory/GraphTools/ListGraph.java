package GraphTools;

import java.util.Vector;

/**
 * @author mwelsman
 *
 * A graph implementation that uses an edge and vertex list.  This can be built
 * from the simpler Graph class which is only based on an adjacency matrix read
 * in from a GRA file.
 */
public class ListGraph {
    private Vector edges;
    private Vector vertices;
    
    /* Construct a ListGraph from existing edge and vertex lists. */
    public ListGraph( Vector e, Vector v ) {
    	    edges = e;
    	    vertices = v;
    }
    /* Construct a ListGraph from a Graph */ 
    public ListGraph( Graph g ) {
    	    edges = new Vector();
    	    vertices = new Vector();
    	    //Add vertices with labels 0...n
    	    for( int i = 0; i < g.size(); i++ ) {
    	    	    vertices.add( new Vertex( i + "" ) );
    	    }
    	    //Visit each entry in the table, adding the number of edges indicated.
    	    for( int i = 0; i < g.size(); i++ )
    	    	    for( int j = 0; j < g.size(); j++ ) {
    	    	    	    for( int k = 0; k < g.adjacency( i, j ); k++ )
    	    	    	    	    edges.add( new Edge( (Vertex)vertices.get( i ), (Vertex)vertices.get( j ) ) );
    	    	    }
    }
    /* Construct a ListGraph from another ListGraph. */
    public ListGraph( ListGraph g ) {
    	    edges = new Vector( g.getEdgeSet() );
    	    vertices = new Vector( g.getVertexSet() );
    }
    
    /* Methods for adding and removing vertices */
    public void addEdge( Edge e ) {
    	    edges.add( e );
    }
    public boolean removeEdge( Edge e ) {
    	    return edges.remove( e );
    }
    public void addVertex( Vertex v ) {
	    vertices.add( v );
    }
    public boolean removeVertex( Vertex v ) {
	    return vertices.remove( v );
    }
    /* Get the edge and vertex sets */
    public Vector getEdgeSet() {
    	    return edges;
    }
    public Vector getVertexSet() {
    	    return vertices;
    }
    /* Get the number of edges */
    public int getEdgeCount() {
    	    return edges.size();
    }
    /* Return the number of edges that are not loops */
    public int getNonLoopEdgeCount() {
    	    int count = 0;
    	    
    	    for( int i = 0; i < edges.size(); i++ ) {
    	    	    Edge cur = (Edge)edges.get(i);
    	    	    if( !cur.isLoop() ) count++;
    	    }
    	    
    	    return count;
    }
    /* Get the number of vertices */
    public int getVertexCount() {
    	    return vertices.size();
    }
    /* Contract the given edge e.  e is removed and its two endpoints are merged to 
     * form a single vertex.  The labelling from one endpoint is used for the new vertex. */
    public void contract( Edge e ) {
    	    //remove the given edge
    	    edges.remove( e );
    	    
    	    //Remove one of  the existing endpoints
    	    if( !e.getV1().equals( e.getV2() ) ) {
    	    	    vertices.remove( e.getV2() );
    	    }
    	    
    	    //Now go through the edge list and update each reference to the removed vertex (v2)
    	    //with the vertex that still exists in the graph (v1).
    	    for( int i = 0; i < edges.size(); i++ ) {
    	    	    Edge cur = (Edge)edges.get(i);
    	    	    
    	    	    if( (cur.getV1()).equals( e.getV2() ))
    	    	    	    cur.setV1( e.getV1() );
    	    	    if( (cur.getV2()).equals( e.getV2() ))
	    	    	    cur.setV2( e.getV1() );
    	    }
    }
    /* String representation */
    public String toString() {
    	    return "V(G) = " + vertices + "\n" + "E(G) = " + edges;
    }
}
