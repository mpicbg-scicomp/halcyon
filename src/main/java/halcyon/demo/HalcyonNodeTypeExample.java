package halcyon.demo;

import halcyon.model.node.HalcyonNodeType;
import javafx.scene.Node;

/**
 * Example of HalcyonNodeType
 */
public enum HalcyonNodeTypeExample implements HalcyonNodeType
{
	ONE
		{
			@Override public Node getIcon()
			{
				return getIcon( ResourcesUtil.getString( name().toLowerCase() + ".icon" ) );
			}
		},
	TWO
		{
			@Override public Node getIcon()
			{
				return getIcon( ResourcesUtil.getString( name().toLowerCase() + ".icon" ) );
			}
		},
	THREE
		{
			@Override public Node getIcon()
			{
				return getIcon( ResourcesUtil.getString( name().toLowerCase() + ".icon" ) );
			}
		}
}
