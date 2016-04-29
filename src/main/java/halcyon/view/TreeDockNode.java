package halcyon.view;

import halcyon.model.list.HalcyonNodeRepository;
import javafx.scene.Node;

import org.dockfx.DockNode;

/**
 * Basic Control type Window for tools, which will be overridden by inherited
 * class
 */
public class TreeDockNode extends DockNode
{
	protected HalcyonNodeRepository nodes;

	public TreeDockNode(Node n)
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
