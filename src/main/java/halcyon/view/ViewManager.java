package halcyon.view;

import halcyon.model.list.HalcyonNodeRepository;
import halcyon.model.list.HalcyonNodeRepositoryListener;
import halcyon.model.list.ObservableCollection;
import halcyon.model.list.ObservableCollectionListener;
import halcyon.model.node.HalcyonNode;
import halcyon.model.node.HalcyonNodeInterface;
import halcyon.model.node.HalcyonSwingNode;
import halcyon.window.console.ConsoleInterface;
import halcyon.window.console.StdOutputCaptureConsole;
import halcyon.window.control.ControlWindowBase;
import halcyon.window.toolbar.MicroscopeStartStopToolbar;
import halcyon.window.toolbar.ToolbarInterface;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;

import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;

/**
 * ViewManager class for managing Windows
 */
public class ViewManager
{
	private final List<HalcyonNodeDockable> pages = new LinkedList<>();

	/** a set of {@link HalcyonNode}s */
	private final HalcyonNodeRepository nodes;

	private final DockPane dockPane;

	private final ControlWindowBase controlWindow;

	private final StdOutputCaptureConsole console;

	private final MicroscopeStartStopToolbar toolbar;

	private DockNode deviceTabsDock;

	private final MenuBar mMenuBar;

	public ViewManager( DockPane dockPane,
			ControlWindowBase config,
			HalcyonNodeRepository nodes,
			ObservableCollection< ConsoleInterface > consoles,
			ObservableCollection< ToolbarInterface > toolbars, MenuBar pMenuBar )
	{
		this.dockPane = dockPane;
		this.nodes = nodes;

		this.dockPane.setPrefSize(800, 600);
		this.mMenuBar = pMenuBar;

		controlWindow = config;
		controlWindow.setPrefSize(200, 300);
		controlWindow.dock(this.dockPane, DockPos.LEFT);

		console = new StdOutputCaptureConsole();
		console.setPrefSize(600, 200);
		console.dock(this.dockPane, DockPos.RIGHT, controlWindow);
		addMenuItem( "Console", console );
		consoles.add(console);

		// Image deviceDockImage = new
		// Image(DockFX.class.getResource("docknode.png").toExternalForm());
		// deviceTabsDock = new DockNode(new VBox(), "Device", new
		// ImageView(deviceDockImage));
		// deviceTabsDock.setPrefSize( 600, 400 );
		// deviceTabsDock.dock( this.dockPane, DockPos.TOP, console );

		toolbar = new MicroscopeStartStopToolbar();
		toolbar.setPrefSize(200, 300);
		toolbar.dock(this.dockPane, DockPos.TOP, controlWindow);
		addMenuItem( "Toolbar", toolbar );
		toolbars.add(toolbar);

		SplitPane split = (SplitPane) dockPane.getChildren().get(0);
		split.setDividerPositions( 0.3 );

		consoles.addListener(new ObservableCollectionListener<ConsoleInterface>()
		{
			@Override
			public void itemAdded(ConsoleInterface item)
			{
				ControlWindowBase lControlWindowBase = (ControlWindowBase) item;

				addMenuItem( "Console", lControlWindowBase );
			}

			@Override
			public void itemRemoved(ConsoleInterface item)
			{

			}
		});

		toolbars.addListener(new ObservableCollectionListener<ToolbarInterface>()
		{
			@Override
			public void itemAdded(ToolbarInterface item)
			{
				ControlWindowBase lControlWindowBase = (ControlWindowBase) item;

				addMenuItem( "Toolbar", lControlWindowBase );
			}

			@Override
			public void itemRemoved(ToolbarInterface item)
			{

			}
		});

		nodes.addListener(new HalcyonNodeRepositoryListener()
		{
			@Override
			public void nodeAdded(HalcyonNodeInterface node)
			{
				open(node);
			}

			@Override
			public void nodeRemoved(HalcyonNodeInterface node)
			{
				close(node);
			}
		});
	}

	private void addMenuItem( String pMenuGroupName, ControlWindowBase pControlWindowBase )
	{
		mMenuBar.getMenus().stream()
				.filter( c -> c.getText().equals( pMenuGroupName ) )
				.findFirst()
				.ifPresent( c -> {
					CheckMenuItem lMenuItem = new CheckMenuItem( pControlWindowBase.getTitle() );

					lMenuItem.setSelected( !pControlWindowBase.isClosed() );
					pControlWindowBase.closedProperty().addListener( new ChangeListener< Boolean >()
					{
						@Override public void changed( ObservableValue< ? extends Boolean > observable, Boolean oldValue, Boolean newValue )
						{
							lMenuItem.setSelected( !newValue );
						}
					} );

					lMenuItem.setOnAction( new EventHandler< ActionEvent >()
					{
						@Override public void handle( ActionEvent event )
						{
							if ( pControlWindowBase.isClosed() )
							{
								pControlWindowBase.dock( dockPane, pControlWindowBase.getLastDockPos(), pControlWindowBase.getLastDockSibling() );
							}
							else
							{
								lMenuItem.setSelected( true );
							}
						}
					} );

					c.getItems().add( lMenuItem );
				});

		if( !pControlWindowBase.equals( console ) && !pControlWindowBase.equals( toolbar ) )
		{
			if( pMenuGroupName.equals( "Console" ) )
				pControlWindowBase.dock( dockPane, DockPos.CENTER, console );
			else if( pMenuGroupName.equals( "Toolbar" ) )
				pControlWindowBase.dock( dockPane, DockPos.CENTER, toolbar );
			else
				pControlWindowBase.dock( dockPane, DockPos.CENTER, deviceTabsDock );
		}
	}

	public HalcyonNodeRepository getNodes()
	{
		return nodes;
	}

	public void open(HalcyonNodeInterface node)
	{
		if (node instanceof HalcyonSwingNode)
		{
			HalcyonSwingNode lHalcyonSwingNode = (HalcyonSwingNode) node;
			lHalcyonSwingNode.setVisible(true);
			return;
		}

		for (final HalcyonNodeDockable n : pages)
		{
			if (n.isDocked())
			{
				deviceTabsDock = n;
				break;
			}
		}

		for (final HalcyonNodeDockable n : pages)
		{
			if (n.getNode() == node)
			{

				if (n.isDocked())
					return;
				else
				{
					if (deviceTabsDock.isDocked())
					{
						n.dock(dockPane, DockPos.CENTER, deviceTabsDock);
					}
					else
					{
						deviceTabsDock = n;
						n.dock(this.dockPane, DockPos.TOP, console);
					}
					return;
				}
			}
		}

		final HalcyonNodeDockable page = new HalcyonNodeDockable(node);
		if (pages.size() == 0)
		{
			deviceTabsDock = page;
			page.dock(this.dockPane, DockPos.TOP, console);
		}
		else
		{
			page.dock(dockPane, DockPos.CENTER, deviceTabsDock);
		}
		pages.add(page);
	}

	public void hide(HalcyonNodeInterface node)
	{
		if (node instanceof HalcyonSwingNode)
		{
			HalcyonSwingNode lHalcyonSwingNode = (HalcyonSwingNode) node;
			lHalcyonSwingNode.setVisible(false);
			return;
		}

		for (final HalcyonNodeDockable page : pages.toArray(new HalcyonNodeDockable[pages.size()]))
		{
			if (page.getNode() == node)
			{
				page.setVisible(false);
			}
		}
	}

	public void close(HalcyonNodeInterface node)
	{
		if (node instanceof HalcyonSwingNode)
		{
			HalcyonSwingNode lHalcyonSwingNode = (HalcyonSwingNode) node;
			lHalcyonSwingNode.close();
			return;
		}

		for (final HalcyonNodeDockable page : pages.toArray(new HalcyonNodeDockable[pages.size()]))
		{
			if (page.getNode() == node)
			{
				page.close();
			}
		}
	}

	public boolean isVisible()
	{
		return dockPane.isVisible();
	}
}
