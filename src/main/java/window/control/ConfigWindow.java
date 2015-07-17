package window.control;

import model.node.HalcyonNodeInterface;
import model.list.HalcyonNodeRepository;
import model.list.HalcyonNodeRepositoryListener;
import view.ViewManager;

import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.BorderLayout;
import java.util.HashMap;

/**
 * Device Config Window
 */
public class ConfigWindow extends ControlWindowBase
{
	final private HalcyonNodeRepository nodes;

	/** the visual representation of the hierarchy-tree */
	private DeviceTree devices;

	private HashMap<HalcyonNodeInterface, DefaultMutableTreeNode> nodeMap = new HashMap<>();

	public ConfigWindow( final ViewManager manager )
	{
		super( "ConfigDockable" );

		nodes = manager.getNodes();

		setTitleText( "Config" );
		setCloseable( false );
		setMinimizable( false );
		setMaximizable( false );

		devices = new DeviceTree( manager );

		setLayout( new BorderLayout() );
		add( new JScrollPane( devices ), BorderLayout.CENTER );

		addSeparator();

		nodes.addListener( new HalcyonNodeRepositoryListener()
		{
			public void nodeAdded( HalcyonNodeInterface node )
			{
				devices.addNode( node );
			}

			public void nodeRemoved( HalcyonNodeInterface node )
			{
				devices.removeNode( node );
			}
		} );
	}
}
