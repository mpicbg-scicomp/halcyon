package halcyon.view.demo;

import halcyon.model.node.HalcyonNodeType;
import javafx.scene.Node;

/**
 * Example of HalcyonNodeType
 */
public enum DemoHalcyonNodeType implements HalcyonNodeType
{
	ONE, TWO, THREE;

	@Override
	public Node getIcon()
	{
		return getIcon( DemoResourcesUtil.getString( name().toLowerCase() + ".icon" ));
	}
}
