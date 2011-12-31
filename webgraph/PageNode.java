package webcrawler;

import java.util.*;
import java.net.*;

/** Node used by the crawler to build the website graph.
 * 
 *  NO LONGER NEEDED. */
public class PageNode {
	private PageNode parent;
    private Vector<PageNode> children = new Vector<PageNode>();
    private URL address;
    private boolean visited;
    
    /** Construct a node. */
    public PageNode( URL address ) {
    	this.address = address;
    }
    
    /** Construct a node with a parent. */
    public PageNode( URL address, PageNode parent ) {
    	this.address = address;
    	this.parent = parent;
    }
    
    /** Add another child, i.e. a page linked to from this one. */
    public void addChild( PageNode child ) {
    	children.add( child );
    }
    
    /** Get the address of this node. */
    public URL getAddress() {
    	return address;
    }
    
    /** Get the parent of this node. */
    public PageNode getParent() {
    	return parent;
    }
    
    public void setParent( PageNode theParent ) {
    	parent = theParent;
    }
    
    /** Get all ancestors for this node. */
    public Vector<PageNode> getAncestors() {
    	PageNode cur = parent;
    	Vector<PageNode> ancestors = new Vector<PageNode>();
    	
    	while( cur != null ) {
    		ancestors.add( cur );
    		cur = cur.getParent();
    	}
    	
    	return ancestors;
    }
    
    /** Return true iff o is a PageNode that points to the same address. */
    public boolean equals( Object o ) {
    	if( !(o instanceof PageNode ) ) return false;
    	else {
    		PageNode p = (PageNode)o;
    		
    		return p.getAddress().toString().equals( getAddress().toString() );
    	}
    }
    /** Set the node to visited or unvisited. */
    public void setVisited( boolean isVisited ) {
    	visited = isVisited;
    }
    /** Was this node visited? */
    public boolean getVisited() {
    	return visited;
    }
    
    public String toString() {
    	return address.toString();
    }
}