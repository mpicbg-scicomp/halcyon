package halcyon.view.demo;

import halcyon.model.node.HalcyonNodeType;
import javafx.scene.Node;

/**
 * Example of HalcyonNodeType
 */
public enum HalcyonNodeTypeExample implements HalcyonNodeType
{
	ONE, TWO, THREE;

	@Override
	public Node getIcon()
	{
		return getIcon(ResourcesUtil.getString(name().toLowerCase() + ".icon"));
	}
}
