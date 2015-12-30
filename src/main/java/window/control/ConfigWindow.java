package window.control;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.node.HalcyonNode;
import model.node.HalcyonNodeInterface;
import model.list.HalcyonNodeRepository;
import model.list.HalcyonNodeRepositoryListener;
import model.node.HalcyonNodeType;
import view.ViewManager;
import window.util.Resources;

import java.util.HashMap;

/**
 * Device Config Window
 */
public class ConfigWindow extends ControlWindowBase
{
	final private HashMap<String, TreeItem<TreeNode>> subNodes = new HashMap<>();
	final private HalcyonNodeRepository nodes;
	JFXPanel fxPanel;

	private final Node rootIcon = new ImageView(
			new Image( getClass().getResourceAsStream( Resources.getString( "root.icon" ) ) )
	);

	public ConfigWindow( final ViewManager manager )
	{
		super( new VBox() );
		setTitle( "Config" );

		Platform.setImplicitExit( false );

		nodes = manager.getNodes();

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

		setContents( tree );

//		Platform.runLater( () -> start( tree ) );
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
