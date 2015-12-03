package window;

import javafx.embed.swing.JFXPanel;
import model.node.HalcyonNode;
import model.node.HalcyonNodeInterface;
import model.list.HalcyonNodeRepository;
import model.list.HalcyonNodeRepositoryListener;
import model.node.HalcyonNodeType;
import model.provider.JFXPanelProvider;
import view.ViewManager;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import window.control.ControlWindowBase;
import window.util.Resources;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;

/**
 * Device Config JavaFX Window
 */
public class FxConfigWindow extends ControlWindowBase implements JFXPanelProvider
{
	final private HashMap<String, TreeItem<TreeNode>> subNodes = new HashMap<>();
	final private HalcyonNodeRepository nodes;
	JFXPanel fxPanel;

	private final Node rootIcon = new ImageView(
			new Image( getClass().getResourceAsStream( Resources.getString( "root.icon" ) ) )
	);

	public FxConfigWindow( final ViewManager manager )
	{
		super( "FxConfigDockable" );

		Platform.setImplicitExit( false );

		nodes = manager.getNodes();

		setTitleText( "Config" );
		setCloseable( false );
		setMinimizable( false );
		setMaximizable( false );

		fxPanel = new JFXPanel();
		getContentPane().add( fxPanel );

		TreeItem<TreeNode> rootItem = new TreeItem<>( new TreeNode( "Microscopy" ), rootIcon );
		rootItem.setExpanded( true );

		for (HalcyonNodeType type : HalcyonNodeType.values())
		{
			String iconName = Resources.getString( type.name().toLowerCase() + ".icon" );
			Node icon = new ImageView( new Image( getClass().getResourceAsStream( iconName )));

			TreeItem<TreeNode> node = new TreeItem<>( new TreeNode( type.name() ), icon );
			node.setExpanded( true );
			subNodes.put( type.name(), node);
			rootItem.getChildren().add( node );
		}

		nodes.addListener( new HalcyonNodeRepositoryListener()
		{
			@Override public void nodeAdded( HalcyonNodeInterface node )
			{
				TreeItem<TreeNode> item = new TreeItem<>( new TreeNode( node.getName(), node ) );
				subNodes.get(node.getType().name()).getChildren().add( item );
			}

			@Override public void nodeRemoved( HalcyonNodeInterface node )
			{
				subNodes.get(node.getType().name()).getChildren().remove( node.getName() );
			}
		} );

		TreeView<TreeNode> tree = new TreeView<>( rootItem );

		tree.setOnMouseClicked( event -> {
			if (event.getClickCount() == 2)
			{
				TreeItem<TreeNode> item = tree.getSelectionModel().getSelectedItem();
				TreeNode node = item.getValue();
				if (node.getNode() != null)
					manager.open( node.getNode() );
			}
		} );

		Platform.runLater( () -> start( tree ) );
	}

	@Override public JFXPanel getJFXPanel()
	{
		return fxPanel;
	}

	public void start( TreeView<TreeNode> tree )
	{
		StackPane root = new StackPane();
		root.getChildren().add( tree );

		fxPanel.setScene( new Scene( root, 300, 250 ) );
	}

	private class TreeNode
	{
		private String name;

		private HalcyonNodeInterface node;

		public TreeNode( String name )
		{
			this.name = name;
		}

		public TreeNode( String name, HalcyonNodeInterface node )
		{
			this.name = name;
			this.node = node;
		}

		public String getName()
		{
			return name;
		}

		public void setName( String name )
		{
			this.name = name;
		}

		public HalcyonNodeInterface getNode()
		{
			return node;
		}

		public void setNode( HalcyonNode node )
		{
			this.node = node;
		}

		@Override
		public String toString()
		{
			return this.name;
		}
	}
}
