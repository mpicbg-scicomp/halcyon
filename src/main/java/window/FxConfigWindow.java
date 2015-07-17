package window;

import javafx.embed.swing.JFXPanel;
import model.node.HalcyonNode;
import model.node.HalcyonNodeInterface;
import model.list.HalcyonNodeRepository;
import model.list.HalcyonNodeRepositoryListener;
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

/**
 * Device Config JavaFX Window
 */
public class FxConfigWindow extends ControlWindowBase implements JFXPanelProvider
{
	final private HalcyonNodeRepository nodes;
	JFXPanel fxPanel;

	private final Node rootIcon = new ImageView(
			new Image( getClass().getResourceAsStream( "/microscope_16.png" ) )
	);

	private final Node cameraIcon = new ImageView(
			new Image( getClass().getResourceAsStream( "/camera_16.png" ) )
	);

	private final Node laserIcon = new ImageView(
			new Image( getClass().getResourceAsStream( "/laser_16.png" ) )
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

		TreeItem<TreeNode> camera = new TreeItem<>( new TreeNode( "Camera" ), cameraIcon );
		camera.setExpanded( true );
		TreeItem<TreeNode> laser = new TreeItem<>( new TreeNode( "Laser" ), laserIcon );
		laser.setExpanded( true );

		rootItem.getChildren().add( camera );
		rootItem.getChildren().add( laser );

		nodes.addListener( new HalcyonNodeRepositoryListener()
		{
			@Override public void nodeAdded( HalcyonNodeInterface node )
			{
				TreeItem<TreeNode> item = new TreeItem<>( new TreeNode( node.getName(), node ) );

				switch (node.getType())
				{
					case Camera:
						camera.getChildren().add( item );
						break;
					case Laser:
						laser.getChildren().add( item );
						break;
				}
			}

			@Override public void nodeRemoved( HalcyonNodeInterface node )
			{
				switch (node.getType())
				{
					case Camera:
						camera.getChildren().remove( node.getName() );
						break;
					case Laser:
						laser.getChildren().remove( node.getName() );
						break;
				}
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
