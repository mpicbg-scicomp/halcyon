package halcyon.view;

import halcyon.model.node.HalcyonNodeInterface;
import halcyon.model.node.HalcyonNodeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.dockfx.DockNode;
import org.dockfx.demo.DockFX;

/**
 * Dockable Window for Halcyon Node
 */
public class HalcyonNodeDockable extends DockNode
{
	/** the current node */
	private HalcyonNodeInterface node;

	private HalcyonNodeListener listener = () -> {

	};

	public HalcyonNodeDockable(HalcyonNodeInterface node)
	{
		super(node.getPanel(),
					node.getName(),
					new ImageView(new Image(DockFX.class.getResource("docknode.png")
																							.toExternalForm())));

		// getDockTitleBar().setVisible( false );

		if (isVisible() && getNode() != null)
			getNode().removeListener(listener);

		this.node = node;

		this.setTitle(node == null ? "" : node.getName());

		if (isVisible() && node != null)
			node.addListener(listener);
	}

	public void setNode(HalcyonNodeInterface node)
	{
		if (isVisible() && getNode() != null)
			getNode().removeListener(listener);

		this.node = node;

		this.setContents(node.getPanel());

		this.setTitle(node == null ? "" : node.getName());

		if (isVisible() && node != null)
			node.addListener(listener);
	}

	public HalcyonNodeInterface getNode()
	{
		return node;
	}
}
