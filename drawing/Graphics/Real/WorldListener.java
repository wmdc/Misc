package Graphics.Real;

/**
 * Created on Apr 8, 2006
 * @author mwelsman
 *
 * Allows an object to be updated when the world somehow changes.
 */
public interface WorldListener {
    public void worldChanged( int changeType );
}
