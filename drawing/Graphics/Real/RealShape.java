package Graphics.Real;

import java.awt.Color;

/**
 * Created on Apr 4, 2006
 * @author mwelsman
 *
 * Defines the features that every shape must have.
 */
public interface RealShape {
	//Must have colour support
    public Color lineColour();
    public Color fillColour();
    public void setLineColour( Color lineColour );
    public void setFillColour( Color fillColour );
    
    //Must provide support for transformations
    public void scale( double xFactor, double yFactor, RealPoint aboutPoint );
    public void rotate( double theta, RealPoint aboutPoint );
    public void translate( RealPoint delta );
    
    //Must have clipping support (but it doesn't have to do anything)
    public RealShape clip( double xMin, double xMax, double yMin, double yMax );
}
