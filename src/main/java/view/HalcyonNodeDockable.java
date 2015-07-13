package view;

import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockableFactory;
import bibliothek.gui.dock.common.event.CDockableAdapter;
import bibliothek.gui.dock.common.intern.CDockable;

import model.HalcyonNode;
import model.HalcyonNodeListener;

/**
 * Dockable Window for Halcyon Node
 */
public class HalcyonNodeDockable extends DefaultMultipleCDockable
{
	/** the current node */
	private HalcyonNode node;

	private HalcyonNodeListener listener = () -> {

	};

	public HalcyonNodeDockable( MultipleCDockableFactory<HalcyonNodeDockable,?> factory)
	{
		super( factory );

		setTitleText( "Page" );
		setCloseable( true );
		setMinimizable( true );
		setMaximizable( true );
		setExternalizable( false );
		setRemoveOnClose( true );

		addCDockableStateListener( new CDockableAdapter(){
			@Override
			public void visibilityChanged( CDockable dockable ){
				HalcyonNode node = getNode();
				if( node != null ){
					if( isVisible() ){
						node.addListener( listener );
						listener.nodeChanged();
					}
					else{
						node.removeListener( listener );
					}
				}
			}
		});
	}

	public void setNode( HalcyonNode node ){
		if( isVisible() && getNode() != null )
			getNode().removeListener( listener );

		this.node = node;

		if( node.getPanel() != null)
		{
			getContentPane().removeAll();
			getContentPane().add( node.getPanel() );
		}

		setTitleText( node == null ? "" : node.getName() );

		if( isVisible() && node != null )
			node.addListener( listener );
	}

	public HalcyonNode getNode() {
		return node;
	}
}
