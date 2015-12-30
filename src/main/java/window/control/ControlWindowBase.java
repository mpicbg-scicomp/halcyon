package window.control;

import javafx.scene.Node;
import org.dockfx.DockNode;

/**
 * Basic Control type Window for tools, which will be overridden by inherited
 * class
 */
public abstract class ControlWindowBase extends DockNode implements ControlWindowInterface
{
	public ControlWindowBase( Node n )
	{
		super( n );
	}
}
