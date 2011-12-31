package ActivePrototype;

/**
 * Created on March 28, 2006
 * @author mwelsman
 *
 * This is the interface which allows the Hand class to modify both containers ("Memo") and the
 * personal areas.  Those two classes must implement the methods defined here.
 * 
 * The Hand classes will have to check if the "hand" is over an object by calling contains(x,y), where
 * [x,y] is the current position of the hand.  If that comes back as true, the Hand then calls methods
 * like rotate(degree) on that Manipulatable object to change it.
 * 
 * Note that the Hand class should refer to objects as Manipulatable, NOT Memo or PersonalSpace.
 * 
 * We may not need variables as floats but I defined them this way because we can go from float to int
 * but not the other way around.  If the extra precision is needed it's there.
 */
public interface Manipulatable {
    public void translate( float dx, float dy );
    public void rotate( float degrees );
    public void scale( float left, float right, float top, float bottom ); //this seems to be how the hand works
    public boolean contains( float x, float y );
}
