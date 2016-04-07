package halcyon.model.node;

import halcyon.view.TreePanel;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

/**
 * HalcyonNode Type enumeration
 */
public interface HalcyonNodeType
{
	// Camera, Laser, LightSheet, Stage, FilterWheel, AdaptiveOptics, Other
	String name();

	Node getIcon();

	default Node getIcon( String pPath )
	{
		return getIconPath( this.getClass().getResourceAsStream( pPath ) );
	}

	static Node getIconPath( InputStream resourceStream )
	{
		return new ImageView( new Image( resourceStream ) );
	}

}
