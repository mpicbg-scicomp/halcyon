package view;

import model.node.HalcyonNodeInterface;
import model.node.HalcyonNodeListener;
import org.dockfx.DockNode;

/**
 * Dockable Window for Halcyon Node
 */
public class HalcyonNodeDockable extends DockNode
{
	/** the current node */
	private HalcyonNodeInterface node;

	private HalcyonNodeListener listener = () -> {

	};

	public HalcyonNodeDockable( HalcyonNodeInterface node )
	{
		super( node.getPanel() );
		getDockTitleBar().setVisible( false );

		if( isVisible() && getNode() != null )
			getNode().removeListener( listener );

		this.node = node;

		this.setTitle( node == null ? "" : node.getName() );

		if( isVisible() && node != null )
			node.addListener( listener );
	}

	public void setNode( HalcyonNodeInterface node ){
		if( isVisible() && getNode() != null )
			getNode().removeListener( listener );

		this.node = node;

		this.setContents( node.getPanel() );

		this.setTitle( node == null ? "" : node.getName() );

		if( isVisible() && node != null )
			node.addListener( listener );
	}

	public HalcyonNodeInterface getNode() {
		return node;
	}
}
