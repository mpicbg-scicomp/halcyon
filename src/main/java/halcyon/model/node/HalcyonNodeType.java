package halcyon.model.node;

import halcyon.view.TreePanel;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
		return getIconPath( pPath );
	}

	static Node getIconPath( String pIconPath )
	{
		return new ImageView( new Image( TreePanel.class.getResourceAsStream( pIconPath ) ) );
	}

}
