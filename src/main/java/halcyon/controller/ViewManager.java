package halcyon.controller;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.dockfx.ContentHolder;
import org.dockfx.DelayOpenHandler;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;

import halcyon.model.collection.HalcyonNodeRepository;
import halcyon.model.collection.HalcyonNodeRepositoryListener;
import halcyon.model.collection.ObservableCollection;
import halcyon.model.collection.ObservableCollectionListener;
import halcyon.model.node.HalcyonGroupNode;
import halcyon.model.node.HalcyonNode;
import halcyon.model.node.HalcyonNodeInterface;
import halcyon.model.node.HalcyonOtherNode;
import halcyon.view.HalcyonPanel;
import halcyon.view.TreePanel;
import halcyon.view.console.StdOutputCaptureConsole;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.SwingUtilities;

/**
 * ViewManager is a controller class to manage HalcyonNodes and GUI.
 */
public class ViewManager
{
	private final List<HalcyonPanel> mPages = new LinkedList<>();

	private final HashMap<HalcyonNodeInterface, Stage> mExternalNodeMap = new HashMap<>();

	private final HashMap<String, ObservableCollection<DockNode>> mControlNodeMap = new HashMap<>();

	/** a set of {@link HalcyonNode}s */
	private final HalcyonNodeRepository mNodes;

	private final DockPane mDockPane;

	private final TreePanel mTreePanel;

	private final StdOutputCaptureConsole mStdOutputCaptureConsole;

	private final Menu mViewMenu;

	private final String mAppIconPath;

	private final HashSet<HalcyonOtherNode> mOtherNodes = new HashSet<>();

	/**
	 * Instantiates a new ViewManager.
	 * 
	 * @param pDockPane
	 *          the DockPane
	 * @param pTreePanel
	 *          the TreePanel
	 * @param nodes
	 *          the HalcyonNodes
	 * @param pConsoles
	 *          the ConsolePanel collection
	 * @param pToolbars
	 *          the ToobalPanel collection
	 * @param pViewMenu
	 *          the ViewMenu
	 * @param pAppIconPath
	 *          the application icon path
	 */
	public ViewManager(	DockPane pDockPane,
											TreePanel pTreePanel,
											HalcyonNodeRepository nodes,
											ObservableCollection<DockNode> pConsoles,
											ObservableCollection<DockNode> pToolbars,
											Menu pViewMenu,
											String pAppIconPath)
	{
		this.mDockPane = pDockPane;
		this.mNodes = nodes;

		this.mDockPane.setPrefSize(800, 600);
		this.mViewMenu = pViewMenu;

		mTreePanel = pTreePanel;
		mTreePanel.setPrefSize(200, 300);
		mTreePanel.setClosable(false);
		mTreePanel.dock(this.mDockPane, DockPos.LEFT);

		mAppIconPath = pAppIconPath;

		mStdOutputCaptureConsole = new StdOutputCaptureConsole();
		mStdOutputCaptureConsole.setPrefSize(600, 200);
		mStdOutputCaptureConsole.setClosable(false);
		pConsoles.add(mStdOutputCaptureConsole);

		mControlNodeMap.put("Console", new ObservableCollection<>());
		mControlNodeMap.put("Toolbar", new ObservableCollection<>());

		dockNodes("Console", DockPos.RIGHT, pConsoles);
		dockNodes("Toolbar", DockPos.TOP, pToolbars);

		SplitPane split = (SplitPane) pDockPane.getChildren().get(0);
		split.setDividerPositions(0.3);

		pConsoles.addListener(new ObservableCollectionListener<DockNode>()
		{
			@Override
			public void itemAdded(DockNode item)
			{
				dockNode("Console", item, DockPos.RIGHT);
				addViewMenuItem("Console", item);
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
				dockNode("Toolbar", item, DockPos.TOP);
				addViewMenuItem("Toolbar", item);
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

	private void dockNode(String pMenu,
												DockNode item,
												DockPos pDefaultPosition)
	{
		if (mControlNodeMap.get(pMenu).getCount() == 0)
			item.dock(mDockPane, pDefaultPosition, mTreePanel);
		else
			item.dock(mDockPane, DockPos.CENTER, mControlNodeMap.get(pMenu)
																													.get(0));

		mControlNodeMap.get(pMenu).add(item);
	}

	private void dockNodes(	String pMenu,
													DockPos pPosition,
													ObservableCollection<DockNode> pControlNodes)
	{
		for (DockNode lDockNode : pControlNodes.getList())
		{
			dockNode(pMenu, lDockNode, pPosition);
			addViewMenuItem(pMenu, lDockNode);
		}

		if (pControlNodes.getCount() > 0)
			pControlNodes.get(0).focus();
	}

	private void addViewMenuItem(	String pMenuGroupName,
																DockNode pControlNode)
	{
		mViewMenu.getItems()
							.stream()
							.filter(c -> c.getText().equals(pMenuGroupName))
							.findFirst()
							.ifPresent(c -> {
								CheckMenuItem lMenuItem = new CheckMenuItem(pControlNode.getTitle());

								lMenuItem.setSelected(!pControlNode.isClosed());
								pControlNode.closedProperty()
														.addListener(new ChangeListener<Boolean>()
														{
															@Override
															public void changed(ObservableValue<? extends Boolean> observable,
																									Boolean oldValue,
																									Boolean newValue)
															{
																if (newValue)
																	mControlNodeMap.get(pMenuGroupName)
																									.remove(pControlNode);
																lMenuItem.setSelected(!newValue);
															}
														});

								lMenuItem.setOnAction(new EventHandler<ActionEvent>()
								{
									@Override
									public void handle(ActionEvent event)
									{
										if (pControlNode.isClosed())
										{
											DockPos lDefaultPos = DockPos.RIGHT;

											if (pMenuGroupName.equals("Console"))
												lDefaultPos = DockPos.RIGHT;
											else if (pMenuGroupName.equals("Toolbar"))
												lDefaultPos = DockPos.TOP;

											dockNode(	pMenuGroupName,
																pControlNode,
																lDefaultPos);
										}
										else if (!pControlNode.isClosed())
										{
											pControlNode.close();
										}
										// else
										// {
										// lMenuItem.setSelected(true);
										// }
									}
								});

								((Menu) c).getItems().add(lMenuItem);
							});

	}

	/**
	 * Gets the Halcyons.
	 * 
	 * @return the nodes
	 */
	public HalcyonNodeRepository getNodes()
	{
		return mNodes;
	}

	/**
	 * Open the HalcyonNode.
	 * 
	 * @param node
	 *          the node
	 */
	public void open(HalcyonNodeInterface node)
	{
		if (mExternalNodeMap.containsKey(node))
		{
			mExternalNodeMap.get(node).requestFocus();
			return;
		}

		if (node instanceof HalcyonOtherNode)
		{
			// System.out.println("Other");
			HalcyonOtherNode lHalcyonExternalNode = (HalcyonOtherNode) node;
			lHalcyonExternalNode.setVisible(true);
			mOtherNodes.add( lHalcyonExternalNode );
			return;
		}

		// If users want to focus the opened dock, then focus and return
		for (final HalcyonPanel n : mPages)
		{
			if (n.getNode() == node && n.isDocked())
			{
				n.focus();
				return;
			}
		}

		DockNode deviceTabsDock = null;
		// Checking which dock window is docked
		for (final HalcyonPanel n : mPages)
		{
			if (n.isDocked())
			{
				deviceTabsDock = n;
				break;
			}
		}

		// Otherwise, we will create new HalcyonNode
		halcyonGroupNodes.forEach(c -> c.removeNode(node));

		final HalcyonPanel page = new HalcyonPanel(node);

		if (deviceTabsDock != null)
		{
			page.dock(mDockPane, DockPos.CENTER, deviceTabsDock);
		}
		else
		{
			page.dock(mDockPane, DockPos.TOP, mStdOutputCaptureConsole);
		}

		mPages.add(page);
	}

	/**
	 * Restore the HalcyonNode.
	 * 
	 * @param node
	 *          the node
	 */
	public DockNode restore(HalcyonNodeInterface node)
	{
		DockNode deviceTabsDock = null;
		// Checking which dock window is docked
		for (final HalcyonPanel n : mPages)
		{
			if (n.isDocked())
			{
				deviceTabsDock = n;
				break;
			}
		}

		halcyonGroupNodes.forEach(c -> c.removeNode(node));

		final HalcyonPanel page = new HalcyonPanel(node);

		if (deviceTabsDock != null)
		{
			page.dock(mDockPane, DockPos.CENTER, deviceTabsDock);
		}
		else
		{
			page.dock(mDockPane, DockPos.TOP, mStdOutputCaptureConsole);
		}

		return page;
	}

	/**
	 * Hide the HalcyonNode.
	 * 
	 * @param node
	 *          the node
	 */
	public void hide(HalcyonNodeInterface node)
	{
		if (node instanceof HalcyonOtherNode)
		{
			HalcyonOtherNode lHalcyonExternalNode = (HalcyonOtherNode) node;
			lHalcyonExternalNode.setVisible(false);
			return;
		}

		for (final HalcyonPanel page : mPages.toArray(new HalcyonPanel[mPages.size()]))
		{
			if (page.getNode() == node)
			{
				page.setVisible(false);
			}
		}
	}

	/**
	 * Close the HalcyonNode.
	 * 
	 * @param node
	 *          the node
	 */
	public void close(HalcyonNodeInterface node)
	{

		if (node instanceof HalcyonOtherNode)
		{
			HalcyonOtherNode lHalcyonExternalNode = (HalcyonOtherNode) node;
			// Close() makes the application hangs. Use setVisible(false) instead.
			// lHalcyonExternalNode.close();
			lHalcyonExternalNode.setVisible(false);
			mOtherNodes.remove( lHalcyonExternalNode );
			return;
		}
		else if (mExternalNodeMap.containsKey(node))
		{
			mExternalNodeMap.get(node).close();
			mExternalNodeMap.remove(node);
			return;
		}

		for (final HalcyonPanel page : mPages.toArray(new HalcyonPanel[mPages.size()]))
		{
			if (page.getNode() == node)
			{
				page.close();
			}
		}
	}

	/**
	 * Is visible or not.
	 * 
	 * @return the boolean
	 */
	public boolean isVisible()
	{
		return mDockPane.isVisible();
	}

	HashSet<HalcyonGroupNode> halcyonGroupNodes = new HashSet<>();

	/**
	 * Make an independent window.
	 * 
	 * @param node
	 *          the node
	 */
	public void makeIndependentWindow(HalcyonNodeInterface node)
	{
		if (node instanceof HalcyonOtherNode)
		{
			open(node);
			return;
		}

		if (node instanceof HalcyonGroupNode)
		{
			halcyonGroupNodes.add((HalcyonGroupNode) node);
		}

		for (final HalcyonPanel page : mPages.toArray(new HalcyonPanel[mPages.size()]))
		{
			if (page.getNode() == node)
			{
				page.close();
				mPages.remove(page);
			}
		}

		if (!mExternalNodeMap.containsKey(node))
		{
			final Scene scene = mDockPane.getScene();

			BorderPane lBorderPane = new BorderPane();
			final Node lPanel = node.getPanel();
			lBorderPane.setCenter(lPanel);
			Scene lScene = new Scene(lBorderPane);

			Stage lStage = new Stage();

			if (mAppIconPath != null)
				lStage.getIcons()
							.add(new Image(getClass().getResourceAsStream(mAppIconPath)));
			lStage.setTitle(node.getName());
			lStage.setScene(lScene);
			lStage.setX(scene.getWindow().getX());
			lStage.setY(scene.getWindow().getY());
			lStage.show();

			mExternalNodeMap.put(node, lStage);

			if (node instanceof HalcyonGroupNode)
			{
				((HalcyonGroupNode) node).getNodeList()
																	.addListener(new ListChangeListener<Node>()
																	{
																		@Override
																		public void onChanged(Change<? extends Node> c)
																		{
																			if (c.getList().size() == 0)
																			{
																				lStage.close();
																				mExternalNodeMap.remove(node);
																				halcyonGroupNodes.removeIf(t -> t.equals(node));
																			}
																			else
																			{
																				lStage.sizeToScene();
																			}
																		}
																	});
			}

			lStage.setOnCloseRequest(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(WindowEvent event)
				{
					mExternalNodeMap.remove(node);
					halcyonGroupNodes.removeIf(c -> c.equals(node));
				}
			});
		}
	}

	private Object loadCollection(String fileName) {
		XMLDecoder e = null;
		try {
			e = new XMLDecoder(
					new BufferedInputStream(
							new FileInputStream(fileName)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		Object collection = e.readObject();

		e.close();

		return collection;
	}

	private void storeCollection(String fileName, Object collection) {
		XMLEncoder e = null;
		try {
			e = new XMLEncoder(
					new BufferedOutputStream(
							new FileOutputStream(fileName)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		e.writeObject(collection);

		e.close();
	}

	/**
	 * Store other nodes preference including size/position.
	 * @param filePath the file path
	 */
	public void storeOtherNodePreference( String filePath )
	{
		HashMap<String, ContentHolder > map = new HashMap<>();

		for(HalcyonOtherNode n : mOtherNodes)
		{
			// Create ContentHolder and save
			ContentHolder otherNode = new ContentHolder(n.getName(), ContentHolder.Type.FloatingNode);
			otherNode.addProperty( "Title", n.getName() );
			otherNode.addProperty( "Size", n.getSize() );
			otherNode.addProperty( "Position", n.getPosition() );

			map.put( n.getName(), otherNode );
		}

		storeCollection( filePath, map );
	}

	/**
	 * Load other nodes preference including size/position.
	 * @param filePath the file path
	 */
	public void loadOtherNodePreference( String filePath )
	{
		HashMap<String, ContentHolder > map = (HashMap<String, ContentHolder>) loadCollection( filePath );

		for(String key : map.keySet())
		{
			// Restore the position and size
			ContentHolder otherNode = map.get( key );
			String title = otherNode.getProperties().getProperty( "Title" );

			HalcyonNodeInterface n = mNodes.getNode( title );

			if( null != n && n instanceof HalcyonOtherNode )
			{
				HalcyonOtherNode lOtherNode = (HalcyonOtherNode) n;
				open( lOtherNode );
				SwingUtilities.invokeLater( () -> {
					lOtherNode.setSize( (Integer[]) otherNode.getProperties().get( "Size" ) );
					lOtherNode.setPosition( (Integer[]) otherNode.getProperties().get( "Position") );
				} );
			}
		}
	}
}
