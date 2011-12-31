package GraphTools;

/**
 * @author mwelsman
 *
 * Represents a single labelled vertex.
 */
public class Vertex {
    private String label;
    
    /* Construct a vertex with a label. */
    public Vertex( String l ) {
    	    label = l;
    }
    /* Get the label for this vertex. */
    public String getLabel() {
    	    return label;
    }
    /* Return true if this vertex's label equals that of the vertex provided as an argument.
     * Two vertices with the same label are considered equal even if the objects are not the
     * same. */
    public boolean equals( Object v ) {
    	    if( !( v instanceof Vertex ) ) return false;
    	    
    	    return label.equals( ((Vertex)v).getLabel() );
    }
    /* String representation */
    public String toString() {
    	    return label;
    }
}