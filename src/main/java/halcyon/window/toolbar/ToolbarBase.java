package halcyon.window.toolbar;

import halcyon.window.control.ControlWindowBase;
import javafx.scene.Node;

/**
 * Toolbar base class
 */
public abstract class ToolbarBase extends ControlWindowBase	implements
																														ToolbarInterface
{
	public ToolbarBase(Node node)
	{
		super(node);
	}
}
