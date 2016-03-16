package window.control;

import javafx.scene.Node;
import model.list.HalcyonNodeRepository;

import org.dockfx.DockNode;

import view.ViewManager;

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
