package Interface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import Graphics.Real.*;

/**
 * Created on Feb 12, 2006
 * @author mwelsman
 *
 * This class defines the toolbar window, which is a major part of the GUI of the graphics
 * package.
 */
public class Toolbar extends JFrame implements  	WorldListener, ActionListener,
                          					 	TreeSelectionListener {
	public static final int COLOUR_BOX_WIDTH = 60;
	public static final int COLOUR_BOX_HEIGHT = 60;
	
    private ColorBox lineBox, fillBox;   //for colour selection
    private JButton group, ungroup, delete;
    private JLabel line, fill, curpos;   //labels
	//private DataTree treePane;           //a view of the data structure
    private RealWorld world;
    private JTree tree;

	/** Construct a new toolbar. */
	public Toolbar( RealWorld world ) {
		super();
		this.world = world;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(  (int)screenSize.getHeight(), 0 );
		
		this.setSize( (int)screenSize.getWidth() - (int)screenSize.getHeight(),
				      (int)screenSize.getHeight() );
		
		BorderLayout mainLayout = new BorderLayout( 0, 10 ); //small vertical gap
		
		JPanel topPanel = new JPanel( new BorderLayout() );
		JPanel bottomPanel = new JPanel( new BorderLayout() );
		
		getContentPane().setLayout( new BorderLayout( 0, 10 ) );
		
		group = new JButton("Group");
		ungroup = new JButton("Ungroup");
		delete = new JButton("Delete");
		
		group.addActionListener( this );
		ungroup.addActionListener( this );
		delete.addActionListener( this );
		
		lineBox = new ColorBox( COLOUR_BOX_WIDTH, COLOUR_BOX_HEIGHT, Color.WHITE, this );
		fillBox = new ColorBox( COLOUR_BOX_WIDTH, COLOUR_BOX_HEIGHT, Color.WHITE, this );
		
		line = new JLabel("Line colour");
		fill = new JLabel("Fill colour");
		
		JPanel linePane = new JPanel( );
		linePane.add( line );
		linePane.add( lineBox );
		
		JPanel fillPane = new JPanel();
		linePane.add( fill );
		linePane.add( fillBox );
		
		curpos = new JLabel("Current position: " + world.getCurrentPosition() );

		//topPanel.setLayout( new BoxLayout( topPanel, BoxLayout.PAGE_AXIS) );
		topPanel.add( curpos, BorderLayout.NORTH );
		topPanel.add( linePane, BorderLayout.CENTER );
		topPanel.add( fillPane, BorderLayout.SOUTH );
		
		getContentPane().add( topPanel, BorderLayout.NORTH );
		//Build the tree object
		world.addWorldListener( this );
        	DefaultTreeModel treeModel = new DefaultTreeModel( buildTree( world.getContents() ));
       
        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode( TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION );
        tree.addTreeSelectionListener( this );
        tree.setShowsRootHandles(true);
        
        	//Embed the tree object in a scrolling pane so it can be scrolled.
        	JScrollPane scrollPane = new JScrollPane(tree);
		bottomPanel.add( scrollPane, BorderLayout.CENTER );
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add( group );
		buttonPanel.add( ungroup );
		buttonPanel.add( delete );
		bottomPanel.add( buttonPanel, BorderLayout.SOUTH );
        
        	getContentPane().add( bottomPanel, BorderLayout.CENTER );
		
		show();
	}
	/** Build the tree to be displayed. */
	private DefaultMutableTreeNode buildTree( Object o ) {
		DefaultMutableTreeNode result = new DefaultMutableTreeNode( o );
		
		//Add some children if this isn't a leaf
		if( o instanceof Group ) {
			Group children = (Group)o;
			//Recurse
			for( int i = 0; i < children.size(); i++ )
				result.add( buildTree( children.get(i) ) );
		}
		return result;
	}
	/** Build the tree to be displayed */
	public void worldChanged( int changeType ) {
		if( changeType == RealWorld.CONTENTS_CHANGED ) {
			//Rebuild the tree
			DefaultTreeModel treeModel = new DefaultTreeModel( buildTree( world.getContents() ));
	        tree.setModel( treeModel );
	        
	        //Preserve selections?
		}
		else if( changeType == RealWorld.FILL_COL_CHANGED )
			fillBox.setBackground( world.getFillColour() );
		else if( changeType == RealWorld.LINE_COL_CHANGED )
			lineBox.setBackground( world.getLineColour() );
		else if( changeType == RealWorld.CURPOS_CHANGED )
			curpos.setText( "Current position: " + world.getCurrentPosition() );
	}
    
    public void colourUpdate( ColorBox box, Color value ) {
    	   if( box == fillBox )
    	   	   world.setFillColour( value );
    	   else if( box == lineBox )
    	   	   world.setLineColour( value );
    }
    /** Button presses. */
    public void actionPerformed( ActionEvent e ) {
    	   if( e.getSource() == group ) {  //Group the currently selected items
    	   	   TreePath[] paths = tree.getSelectionPaths();
    	   	   
    	   	   if( paths == null || paths.length == 0 ) return;
    	   	   
    	   	   Group g = new Group();  //the new group to be formed
    	   	   
    	   	   for( int i = 0; i < paths.length; i++ ) {
    	   	   	   DefaultMutableTreeNode node = 
    	   	   	   	        (DefaultMutableTreeNode)paths[i].getLastPathComponent();
    	   	   	   Object o = node.getUserObject();
    	   	   	   g.add( o );
    	   	   	   world.removeObject( o );
    	   	   }
    	   	   
    	   	   //Now we must figure out where to add G.
    	   	   DefaultMutableTreeNode node = 
	   	   	        (DefaultMutableTreeNode)paths[0].getLastPathComponent();
    	   	   
    	   	   DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
    	   	   Group parentGroup = (Group)parent.getUserObject();
    	   	   
    	   	   parentGroup.add( g );
    	   	   world.triggerRefresh();
    	   }
       //Move each currently selected item up one level
    	   else if( e.getSource() == ungroup ) {
    	   	   TreePath[] paths = tree.getSelectionPaths();

    	   	   if( paths == null || paths.length == 0 ) return;
    	   	   
 	   	   for( int i = 0; i < paths.length; i++ ) {
 	   	       DefaultMutableTreeNode node = 
 	   	       	        (DefaultMutableTreeNode)paths[i].getLastPathComponent();   
 	   	       
 	   	       DefaultMutableTreeNode parent = 
       	   	            (DefaultMutableTreeNode)node.getParent();
 	   	       
 	   	       if( parent != null ) { //there is a parent
 	   	       	   DefaultMutableTreeNode grandParent = 
   	        	                   (DefaultMutableTreeNode)parent.getParent();
 	   	       	   //Need the level above
 	   	       	   if( grandParent != null ) {
 	   	       	        Group gParent = (Group)grandParent.getUserObject();
 	   	       	        //Move the user object up one level
 	   	       	        world.removeObject( node.getUserObject() ); //trigger update
 	   	       	        gParent.add( node.getUserObject() );
 	   	       	   }
 	   	       }
 	   	   }
 	   	   world.triggerRefresh();
    	   }
    	   else if( e.getSource() == delete ) {
    	   	   TreePath[] paths = tree.getSelectionPaths();
    	   	   
    	   	   if( paths == null ) return;

  	   	   for( int i = 0; i < paths.length; i++ ) {
  	   	       Object cur = ((DefaultMutableTreeNode)paths[i].getLastPathComponent())
			                         .getUserObject();
  	   	       world.removeObject( cur );
  	   	   }
    	   }
    }
    /** Update the world selection to reflect the selection in the JTree. */
    public void valueChanged( TreeSelectionEvent e ) {
    	    TreePath[] paths = tree.getSelectionPaths();
    	    
    	    if( paths == null ) {  //nothing selected
			world.setSelectedObjects( new Group() );
			return;
    	    }
    	    
    	    Group selection = new Group();
	   	for( int i = 0; i < paths.length; i++ ) {
	   	    DefaultMutableTreeNode cur = 
	   	    	        (DefaultMutableTreeNode)paths[i].getLastPathComponent();
	   	    selection.add( cur.getUserObject() );
	   	}
	   	world.setSelectedObjects( selection );
    }
}
class ColorBox extends JButton implements ActionListener {
	private Toolbar parent;
	/** Build a new colour box. */
	public ColorBox( int width, int height, Color init, Toolbar parent ) {
		super();
		this.parent = parent;
		setBackground( init );
		setPreferredSize( new Dimension( width, height ) );

		addActionListener( this );
	}
	/** Show a colour picker when the user clicks. */
	public void actionPerformed( ActionEvent e ) {
		Color c = JColorChooser.showDialog( new JFrame(), "Please choose a colour", getBackground() );
		parent.colourUpdate( this, c );
		setBackground( c );
	}
	/** Override the paint to draw the current colour */
	public void paint( Graphics g ) {
		g.setColor( getBackground() );
		g.fillRect( 0, 0, getWidth(), getHeight() );
	}
}