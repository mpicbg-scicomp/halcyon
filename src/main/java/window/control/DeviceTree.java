package window.control;

import model.node.HalcyonNode;
import model.node.HalcyonNodeInterface;
import model.node.HalcyonNodeType;
import view.ViewManager;
import window.util.Resources;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;

/**
 * Device Tree for managing all the devices
 */
public class DeviceTree extends JTree
{
	final private HashMap<String, DefaultMutableTreeNode> nodes = new HashMap<>();
	final private HashMap<HalcyonNodeInterface, DefaultMutableTreeNode> nodeMap = new HashMap<>();
	private DefaultMutableTreeNode root;
	private DefaultTreeModel model;
	private TreePath rootPath;

	public DeviceTree( final ViewManager manager )
	{
		this.addMouseListener( new MouseAdapter()
		{
			@Override public void mousePressed( MouseEvent e )
			{
				int selRow = getRowForLocation( e.getX(), e.getY() );
				TreePath selPath = getPathForLocation( e.getX(), e.getY() );
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

		initTree();
	}

	private void initTree()
	{
		try
		{
			// root icon is not part of enum, so that we add root icon first here
			String iconName = Resources.getString( "root.icon" );
			ImageIcon icon = new ImageIcon( ImageIO.read( getClass().getResourceAsStream( iconName ) ) );
			DefaultMutableTreeNode node = new DefaultMutableTreeNode( new DeviceNode( "Devices", icon ) );
			nodes.put( "Root", node );
			root = node;
			rootPath = new TreePath( root );
			model = new DefaultTreeModel( node );
			setModel( model );

			for (HalcyonNodeType type : HalcyonNodeType.values())
			{
				iconName = Resources.getString( type.name().toLowerCase() + ".icon" );
				icon = new ImageIcon( ImageIO.read( getClass().getResourceAsStream( iconName ) ) );
				node = new DefaultMutableTreeNode( new DeviceNode( type.name(), icon ) );
				nodes.put( type.name(), node );
				root.add(node);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void addNode( HalcyonNodeInterface node )
	{
		DefaultMutableTreeNode nodeNew = new DefaultMutableTreeNode( node );
		nodeMap.put( node, nodeNew );

		DefaultMutableTreeNode parent = nodes.get( node.getType().name() );
		parent.add( nodeNew );
		model.reload( parent );

		expandPath( rootPath.pathByAddingChild( parent ) );
	}

	public void removeNode( HalcyonNodeInterface node )
	{
		DefaultMutableTreeNode nodeDeleted = nodeMap.get( node );

		DefaultMutableTreeNode parent = nodes.get( node.getType().name() );

		parent.remove( nodeDeleted );
		model.reload( parent );

		nodeMap.remove( node );
	}

	@Override
	public void updateUI()
	{
		super.updateUI();
		setCellRenderer( new Renderer() );
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
}
