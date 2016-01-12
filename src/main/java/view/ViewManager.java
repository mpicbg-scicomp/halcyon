package view;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import model.node.HalcyonNode;
import model.node.HalcyonNodeInterface;
import model.list.HalcyonNodeRepository;
import model.list.HalcyonNodeRepositoryListener;
import model.list.ObservableCollection;
import model.list.ObservableCollectionListener;

import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import org.dockfx.demo.DockFX;

import window.console.ConsoleInterface;
import window.control.ConfigWindow;
import window.control.ControlWindowBase;
import window.toolbar.ToolbarInterface;

/**
 * ViewManager class for managing Windows
 */
public class ViewManager
{
	private final List<HalcyonNodeDockable> pages = new LinkedList<>();

	/** a set of {@link HalcyonNode}s */
	private final HalcyonNodeRepository nodes;

	private final DockPane dockPane;

	private final TabPane toolbarTabs = new TabPane();

	private final TabPane consoleTabs = new TabPane();

	private final TabPane deviceTabs = new TabPane();

	public ViewManager( DockPane dockPane, HalcyonNodeRepository nodes,
			ObservableCollection<ConsoleInterface> consoles,
			ObservableCollection<ToolbarInterface> toolbars)
	{
		this.dockPane = dockPane;
		this.nodes = nodes;

		dockPane.setPrefSize( 800, 600 );

		final ConfigWindow configWindow = new ConfigWindow( this );
		configWindow.setPrefSize( 200, 300 );
		configWindow.dock( dockPane, DockPos.LEFT );


		Image consoleDockImage = new Image(DockFX.class.getResource("docknode.png").toExternalForm());
		DockNode consoleTabsDock = new DockNode(consoleTabs, "Consoles", new ImageView(consoleDockImage));
		consoleTabsDock.setPrefSize( 600, 200 );
		consoleTabsDock.dock(dockPane, DockPos.RIGHT, configWindow);


		Image deviceDockImage = new Image(DockFX.class.getResource("docknode.png").toExternalForm());
		DockNode deviceTabsDock = new DockNode(deviceTabs, "Devices", new ImageView(deviceDockImage));
		deviceTabsDock.setPrefSize( 600, 400 );
		deviceTabsDock.dock(dockPane, DockPos.TOP, consoleTabsDock);


		Image toolbarDockImage = new Image(DockFX.class.getResource("docknode.png").toExternalForm());
		DockNode toolbarTabsDock = new DockNode(toolbarTabs, "Tools", new ImageView(toolbarDockImage));
		toolbarTabsDock.setPrefSize( 200, 300 );
		toolbarTabsDock.dock(dockPane, DockPos.TOP, configWindow);

		SplitPane split = (SplitPane) dockPane.getChildren().get( 0 );
		split.setDividerPositions( 0.3 );

		toolbars.addListener(new ObservableCollectionListener<ToolbarInterface>()
		{
			@Override
			public void itemAdded( ToolbarInterface item)
			{
//				((ControlWindowBase)item).dock( dockPane, DockPos.TOP );
				ControlWindowBase node = (ControlWindowBase) item;
				toolbarTabs.getTabs().add( new Tab(node.getTitle(), node));
			}

			@Override
			public void itemRemoved( ToolbarInterface item)
			{

			}
		} );

		consoles.addListener( new ObservableCollectionListener<ConsoleInterface>()
		{
			@Override public void itemAdded( ConsoleInterface item )
			{
//				((ControlWindowBase)item).dock( dockPane, DockPos.BOTTOM );
				ControlWindowBase node = (ControlWindowBase) item;
				consoleTabs.getTabs().add( new Tab(node.getTitle(), node));
			}

			@Override public void itemRemoved( ConsoleInterface item )
			{

			}
		} );

		nodes.addListener( new HalcyonNodeRepositoryListener(){
			@Override
			public void nodeAdded( HalcyonNodeInterface node ) { open( node );}
			@Override
			public void nodeRemoved( HalcyonNodeInterface node ){
				closeAll( node );
			}
		});
	}

	public HalcyonNodeRepository getNodes()
	{
		return nodes;
	}

	public void open( HalcyonNodeInterface node ){

		for(final HalcyonNodeDockable n: pages)
		{
			if(n.getNode() == node) return;
		}

		final HalcyonNodeDockable page = new HalcyonNodeDockable( node );
		Tab newTab = new Tab(page.getTitle(), page);
		newTab.setOnClosed( event -> pages.remove( page ) );

		deviceTabs.getTabs().add(newTab);
		pages.add( page );
//		page.dock( dockPane, DockPos.RIGHT );
	}

	public void closeAll( HalcyonNodeInterface node ){
		for( final HalcyonNodeDockable page : pages.toArray( new HalcyonNodeDockable[ pages.size() ] )){
			if( page.getNode()  == node ){
				page.setVisible( false );
			}
		}
	}
}
