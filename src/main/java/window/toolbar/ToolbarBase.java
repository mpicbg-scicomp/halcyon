package window.toolbar;

import javafx.scene.Node;
import window.control.ControlWindowBase;

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
