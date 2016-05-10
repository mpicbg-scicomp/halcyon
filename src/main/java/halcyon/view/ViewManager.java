package halcyon.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;

import halcyon.model.list.HalcyonNodeRepository;
import halcyon.model.list.HalcyonNodeRepositoryListener;
import halcyon.model.list.ObservableCollection;
import halcyon.model.list.ObservableCollectionListener;
import halcyon.model.node.HalcyonExternalNode;
import halcyon.model.node.HalcyonNode;
import halcyon.model.node.HalcyonNodeInterface;
import halcyon.model.node.HalcyonSwingNode;
import halcyon.window.console.ConsoleInterface;
import halcyon.window.console.StdOutputCaptureConsole;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;

/**
 * ViewManager class for managing Windows
 */
public class ViewManager
{
	private final List<HalcyonNodeDockable> pages = new LinkedList<>();

	private final HashMap<HalcyonNodeInterface, Stage> externalNodeMap = new HashMap<>();

	/** a set of {@link HalcyonNode}s */
	private final HalcyonNodeRepository mNodes;

	private final DockPane pDockPane;

	private final TreeDockNode mTreePanel;

	private final StdOutputCaptureConsole mStdOutputCaptureConsole;

	private final MenuBar mMenuBar;

	public ViewManager(	DockPane pDockPane,
											TreeDockNode pTreePanel,
											HalcyonNodeRepository nodes,
											ObservableCollection<DockNode> pConsoles,
											ObservableCollection<DockNode> pToolbars,
											MenuBar pMenuBar)
	{
		this.pDockPane = pDockPane;
		this.mNodes = nodes;

		this.pDockPane.setPrefSize(800, 600);
		this.mMenuBar = pMenuBar;

		mTreePanel = pTreePanel;
		mTreePanel.setPrefSize(200, 300);
		mTreePanel.setClosable( false );
		mTreePanel.dock(this.pDockPane, DockPos.LEFT);

		mStdOutputCaptureConsole = new StdOutputCaptureConsole();
		mStdOutputCaptureConsole.setPrefSize(600, 200);
		mStdOutputCaptureConsole.setClosable( false );
		pConsoles.add(mStdOutputCaptureConsole);


		/*
		if (!pControlWindowBase.equals(console))
		{
			if (pMenuGroupName.equals("Console"))
				pControlWindowBase.dock(dockPane, DockPos.CENTER, console);
			else
				pControlWindowBase.dock(dockPane,
																DockPos.CENTER,
																deviceTabsDock);
		}
		/**/

		addMenuItem("Console", mStdOutputCaptureConsole);

		dockNodes(pDockPane, DockPos.RIGHT, mTreePanel, pConsoles);
		dockNodes(pDockPane, DockPos.TOP, mTreePanel, pToolbars);


		// Image deviceDockImage = new
		// Image(DockFX.class.getResource("docknode.png").toExternalForm());
		// deviceTabsDock = new DockNode(new VBox(), "Device", new
		// ImageView(deviceDockImage));
		// deviceTabsDock.setPrefSize( 600, 400 );
		// deviceTabsDock.dock( this.dockPane, DockPos.TOP, console );

		SplitPane split = (SplitPane) pDockPane.getChildren().get(0);
		split.setDividerPositions(0.3);

		pConsoles.addListener(new ObservableCollectionListener<DockNode>()
		{
			@Override
			public void itemAdded(DockNode item)
			{
				addMenuItem("Console", item);
			}

			@Override
			public void itemRemoved(DockNode item)
			{

			}
		});

		pToolbars.addListener(new ObservableCollectionListener<DockNode>()
		{
			@Override
			public void itemAdded(DockNode item)
			{
				addMenuItem("Toolbar", item);
			}

			@Override
			public void itemRemoved(DockNode item)
			{

			}
		});

		nodes.addListener(new HalcyonNodeRepositoryListener()
		{
			@Override
			public void nodeAdded(HalcyonNodeInterface node)
			{
				// open(node);
			}

			@Override
			public void nodeRemoved(HalcyonNodeInterface node)
			{
				close(node);
			}
		});
	}

	private void dockNodes(	DockPane pDockPane,
													DockPos pPosition,
													DockNode pSibling,
													ObservableCollection<DockNode> pToolbars)
	{

		int i = 0;
		DockNode lFirstDockNode = null;
		for (DockNode lDockNode : pToolbars.getList())
		{
			if (i == 0)
			{
				lFirstDockNode = lDockNode;
				lDockNode.dock(pDockPane, pPosition, pSibling);
			}
			else
				lDockNode.dock(pDockPane, DockPos.CENTER, lFirstDockNode);
			i++;
		}
	}

	private void addMenuItem(	String pMenuGroupName,
														DockNode pControlWindowBase)
	{
		mMenuBar.getMenus()
						.stream()
						.filter(c -> c.getText().equals(pMenuGroupName))
						.findFirst()
						.ifPresent(c -> {
							CheckMenuItem lMenuItem = new CheckMenuItem(pControlWindowBase.getTitle());

							lMenuItem.setSelected(!pControlWindowBase.isClosed());
							pControlWindowBase.closedProperty()
																.addListener(new ChangeListener<Boolean>()
																{
																	@Override
																	public void changed(ObservableValue<? extends Boolean> observable,
																											Boolean oldValue,
																											Boolean newValue)
																	{
																		lMenuItem.setSelected(!newValue);
																	}
																});

							lMenuItem.setOnAction(new EventHandler<ActionEvent>()
							{
								@Override
								public void handle(ActionEvent event)
								{
									if (pControlWindowBase.isClosed())
									{
										pControlWindowBase.dock(pDockPane,
																						pControlWindowBase.getLastDockPos(),
																						pControlWindowBase.getLastDockSibling());
									}
									else
									{
										lMenuItem.setSelected(true);
									}
								}
							});

							c.getItems().add(lMenuItem);
						});

	}

	public HalcyonNodeRepository getNodes()
	{
		return mNodes;
	}

	public void open(HalcyonNodeInterface node)
	{
		if( externalNodeMap.containsKey( node ) ) return;

		if (node instanceof HalcyonSwingNode)
		{
			HalcyonSwingNode lHalcyonSwingNode = (HalcyonSwingNode) node;
			if (!lHalcyonSwingNode.isDockable())
			{
				lHalcyonSwingNode.setVisible(true);
				return;
			}
		}
		else if (node instanceof HalcyonExternalNode)
		{
			HalcyonExternalNode lHalcyonExternalNode = (HalcyonExternalNode) node;
			lHalcyonExternalNode.setVisible(true);
			return;
		}

		// If users want to focus the opened dock, then focus and return
		for (final HalcyonNodeDockable n : pages)
		{
			if (n.getNode() == node && n.isDocked())
			{
				n.focus();
				return;
			}
		}

		DockNode deviceTabsDock = null;
		// Checking which dock window is docked
		for (final HalcyonNodeDockable n : pages)
		{
			if (n.isDocked())
			{
				deviceTabsDock = n;
				break;
			}
		}

		// Otherwise, we will create new HalcyonNode
		final HalcyonNodeDockable page = new HalcyonNodeDockable(node);

		if( deviceTabsDock != null )
		{
			page.dock(pDockPane, DockPos.CENTER, deviceTabsDock);
		}
		else
		{
			page.dock(pDockPane, DockPos.TOP, mStdOutputCaptureConsole);
		}

		pages.add(page);
	}

	public void hide(HalcyonNodeInterface node)
	{
		if (node instanceof HalcyonSwingNode)
		{
			HalcyonSwingNode lHalcyonSwingNode = (HalcyonSwingNode) node;
			if (!lHalcyonSwingNode.isDockable())
			{
				lHalcyonSwingNode.setVisible(false);
				return;
			}
		}
		else if (node instanceof HalcyonExternalNode)
		{
			HalcyonExternalNode lHalcyonExternalNode = (HalcyonExternalNode) node;
			lHalcyonExternalNode.setVisible(false);
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
			if (!lHalcyonSwingNode.isDockable())
			{
				lHalcyonSwingNode.close();
				return;
			}
		}
		else if (node instanceof HalcyonExternalNode)
		{
			HalcyonExternalNode lHalcyonExternalNode = (HalcyonExternalNode) node;
			// Close() makes the application hangs. Use setVisible(false) instead.
			// lHalcyonExternalNode.close();
			lHalcyonExternalNode.setVisible(false);
			return;
		}
		else if ( externalNodeMap.containsKey( node ) )
		{
			externalNodeMap.get( node ).close();
			externalNodeMap.remove( node );
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
		return pDockPane.isVisible();
	}

	public void makeIndenpendentWindow(HalcyonNodeInterface node)
	{
		if( node instanceof HalcyonExternalNode) return;

		for (final HalcyonNodeDockable page : pages.toArray(new HalcyonNodeDockable[pages.size()]))
		{
			if (page.getNode() == node)
			{
				page.close();
				pages.remove( page );
			}
		}

		if( !externalNodeMap.containsKey( node ) )
		{

			BorderPane lBorderPane = new BorderPane();
			final Node lPanel = node.getPanel();
			lBorderPane.setCenter( lPanel );
			Scene lScene = new Scene( lBorderPane );

			Stage lStage = new Stage();
			lStage.setTitle( node.getName() );
			lStage.setScene( lScene );
			lStage.show();

			externalNodeMap.put( node, lStage );

			lStage.setOnCloseRequest( new EventHandler< WindowEvent >()
			{
				@Override public void handle( WindowEvent event )
				{
					externalNodeMap.remove( node );
				}
			} );
		}
	}
}
