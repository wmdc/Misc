package webcrawler;

/** Simple data structure representing a link in a web graph. */
public class PageLink {
    public String source, destination;
    
    public PageLink( String source, String destination ) {
    	this.source = source;
    	this.destination = destination;
    }
    
    public boolean equals( Object o ) {
    	if( !( o instanceof PageLink ) )
    		return false;
    	
    	PageLink p = (PageLink)o;
    	
    	return p.source.equals( source ) && p.destination.equals( destination );
    }
    
    public String toString() {
    	return "[" + source + ", " + destination + "]";
    }
}