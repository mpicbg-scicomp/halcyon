package view;

import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockableFactory;
import bibliothek.gui.dock.common.event.CDockableAdapter;
import bibliothek.gui.dock.common.intern.CDockable;

import model.HalcyonNodeInterface;
import model.HalcyonNodeListener;
import model.JFXPanelProvider;
import model.JPanelProvider;

/**
 * Dockable Window for Halcyon Node
 */
public class HalcyonNodeDockable extends DefaultMultipleCDockable
{
	/** the current node */
	private HalcyonNodeInterface node;

	private HalcyonNodeListener listener = () -> {

	};

	public HalcyonNodeDockable( MultipleCDockableFactory<HalcyonNodeDockable,?> factory)
	{
		super( factory );

		setTitleText( "Page" );
		setCloseable( true );
		setMinimizable( true );
		setMaximizable( true );
		setExternalizable( true );
		setRemoveOnClose( true );

		addCDockableStateListener( new CDockableAdapter(){
			@Override
			public void visibilityChanged( CDockable dockable ){
				HalcyonNodeInterface node = getNode();
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

	public void setNode( HalcyonNodeInterface node ){
		if( isVisible() && getNode() != null )
			getNode().removeListener( listener );

		this.node = node;

		if( node instanceof JPanelProvider )
		{
			getContentPane().removeAll();
			getContentPane().add( ((JPanelProvider) node).getJPanel() );
		}
		else if( node instanceof JFXPanelProvider )
		{
			getContentPane().removeAll();
			getContentPane().add( ((JFXPanelProvider) node).getJFXPanel() );
		}

		setTitleText( node == null ? "" : node.getName() );

		if( isVisible() && node != null )
			node.addListener( listener );
	}

	public HalcyonNodeInterface getNode() {
		return node;
	}
}
