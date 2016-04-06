package halcyon.window.control;

import halcyon.model.list.HalcyonNodeRepository;
import halcyon.view.ViewManager;
import javafx.scene.Node;

import org.dockfx.DockNode;

/**
 * Basic Control type Window for tools, which will be overridden by inherited
 * class
 */
public abstract class ControlWindowBase extends DockNode implements
																												ControlWindowInterface
{
	protected HalcyonNodeRepository nodes;

	public ControlWindowBase(Node n)
	{
		super(n);
	}

	public void setViewManager(ViewManager manager)
	{

	}

	public void setNodes(HalcyonNodeRepository nodes)
	{
		this.nodes = nodes;
	}
}
