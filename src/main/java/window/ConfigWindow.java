package window;

import bibliothek.gui.dock.common.action.CButton;
import model.HalcyonNode;
import model.HalcyonNodeInterface;
import model.HalcyonNodeRepository;
import model.HalcyonNodeRepositoryListener;
import model.HalcyonNodeType;
import view.ViewManager;

import javax.imageio.ImageIO;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;

/**
 * Device Config Window
 */
public class ConfigWindow extends ControlWindowBase
{
	final private HalcyonNodeRepository nodes;

	/** the visual representation of the hierarchy-tree */
	private JTree tree;

	private DefaultTreeModel model;
	private DefaultMutableTreeNode root;
	private DefaultMutableTreeNode camera;
	private DefaultMutableTreeNode laser;
	private DefaultMutableTreeNode stage;

	private Icon rootIcon;
	private Icon cameraIcon;
	private Icon laserIcon;
	private Icon stageIcon;

	private CButton camearaNew;

	private CButton laserNew;

	private HashMap<HalcyonNodeInterface, DefaultMutableTreeNode> nodeMap = new HashMap<>();

	public ConfigWindow( final ViewManager manager )
	{
		super( "ConfigDockable" );

		nodes = manager.getNodes();

		setTitleText( "Config" );
		setCloseable( false );
		setMinimizable( false );
		setMaximizable( false );

		try
		{
			rootIcon = new ImageIcon( ImageIO.read( getClass().getResourceAsStream( "/microscope_16.png" ) ) );
			cameraIcon = new ImageIcon( ImageIO.read( getClass().getResourceAsStream( "/camera_16.png" ) ) );
			laserIcon = new ImageIcon( ImageIO.read( getClass().getResourceAsStream( "/laser_16.png" ) ) );
			stageIcon = new ImageIcon( ImageIO.read( getClass().getResourceAsStream( "/stage_16.png" ) ) );
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}

		tree = new JTree()
		{
			@Override
			public void updateUI()
			{
				super.updateUI();
				setCellRenderer( new Renderer() );
			}
		};

		tree.addMouseListener( new MouseAdapter()
		{
			@Override public void mousePressed( MouseEvent e )
			{
				int selRow = tree.getRowForLocation( e.getX(), e.getY() );
				TreePath selPath = tree.getPathForLocation( e.getX(), e.getY() );
				if (selRow != -1)
				{
					if (e.getClickCount() == 2)
					{
						DefaultMutableTreeNode selectedNode =
								((DefaultMutableTreeNode) selPath.getLastPathComponent());

						if (selectedNode.getUserObject() instanceof HalcyonNode)
						{
							manager.open( (HalcyonNode) selectedNode.getUserObject() );
						}
					}
				}

				super.mousePressed( e );
			}
		} );

		setLayout( new BorderLayout() );
		add( new JScrollPane( tree ), BorderLayout.CENTER );

		root = new DefaultMutableTreeNode( new DeviceNode( "Devices", rootIcon ) );
		camera = new DefaultMutableTreeNode( new DeviceNode( "Cameras", cameraIcon ) );
		laser = new DefaultMutableTreeNode( new DeviceNode( "Lasers", laserIcon ) );
		stage = new DefaultMutableTreeNode( new DeviceNode( "Stages", stageIcon ) );

		TreePath rootPath = new TreePath( root );
		root.add( camera );
		root.add( laser );
		root.add( stage );

		model = new DefaultTreeModel( root );
		tree.setModel( model );

		camearaNew = new CButton()
		{
			@Override
			protected void action()
			{
				String name = askForName();
				if (name != null)
				{
					HalcyonNode n = new HalcyonNode( name, HalcyonNodeType.Camera );

					nodes.add( n );
				}
			}
		};
		camearaNew.setText( "New camera" );
		camearaNew.setTooltip( "Creates a new camera" );
		camearaNew.setIcon( cameraIcon );

		laserNew = new CButton()
		{
			@Override
			protected void action()
			{
				String name = askForName();
				if (name != null)
				{
					nodes.add( new HalcyonNode( name, HalcyonNodeType.Laser ) );
				}
			}
		};
		laserNew.setText( "New laser" );
		laserNew.setTooltip( "Creates a new laser" );
		laserNew.setIcon( laserIcon );

		addAction( camearaNew );
		addAction( laserNew );
		addSeparator();

		nodes.addListener( new HalcyonNodeRepositoryListener()
		{
			public void nodeAdded( HalcyonNodeInterface node )
			{
				DefaultMutableTreeNode nodeNew = new DefaultMutableTreeNode( node );
				nodeMap.put( node, nodeNew );

				switch (node.getType())
				{
					case Camera:
						camera.add( nodeNew );
						model.reload( camera );
						tree.expandPath( rootPath.pathByAddingChild( camera ) );
						break;
					case Laser:
						laser.add( nodeNew );
						model.reload( laser );
						tree.expandPath( rootPath.pathByAddingChild( laser ) );
						break;
					case Stage:
						stage.add( nodeNew );
						model.reload( stage );
						tree.expandPath( rootPath.pathByAddingChild( stage ) );
				}
			}

			public void nodeRemoved( HalcyonNodeInterface node )
			{
				DefaultMutableTreeNode nodeDeleted = nodeMap.get( node );

				switch (node.getType())
				{
					case Camera:
						camera.remove( nodeDeleted );
						break;
					case Laser:
						laser.remove( nodeDeleted );
						break;
					case Stage:
						stage.remove( nodeDeleted );
						break;
				}

				nodeMap.remove( node );
			}
		} );
	}

	private class Renderer extends DefaultTreeCellRenderer
	{
		@Override
		public Component getTreeCellRendererComponent( JTree tree,
				Object value, boolean sel, boolean expanded, boolean leaf,
				int row, boolean hasFocus )
		{

			Object user = ((DefaultMutableTreeNode) value).getUserObject();
			if (user instanceof DeviceNode)
			{
				DeviceNode node = (DeviceNode) user;
				super.getTreeCellRendererComponent( tree, node.getName(), sel, expanded, leaf, row, hasFocus );

				setIcon( node.getIcon() );
			} else
				super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus );

			return this;
		}
	}

	public String askForName()
	{
		String name = null;
		String message = "Please choose a name for the new device";
		while (true)
		{
			name = JOptionPane.showInputDialog(
					getContentPane(),
					message,
					"New device",
					JOptionPane.QUESTION_MESSAGE );

			if (name == null)
				return null;

			name = name.trim();

			if (nodes.getNode( name ) != null)
			{
				message = "There exists already a node with the name \"" + name + "\"\n" +
						"Please choose another name";
			} else if (!name.matches( ".*([a-zA-Z]|[0-9])+.*" ))
			{
				message = "The name must contain at least one letter or digit";
			} else
				return name;
		}
	}

	public class DeviceNode
	{
		private String name;

		private Icon icon;

		public DeviceNode( String name, Icon icon )
		{
			this.name = name;
			this.icon = icon;
		}

		public String getName()
		{
			return name;
		}

		public Icon getIcon()
		{
			return icon;
		}
	}
}
