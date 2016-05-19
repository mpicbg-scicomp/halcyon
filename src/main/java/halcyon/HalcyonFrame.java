package halcyon;

import halcyon.model.collection.HalcyonNodeRepository;
import halcyon.model.collection.ObservableCollection;
import halcyon.model.node.HalcyonNodeInterface;
import halcyon.controller.ViewManager;
import halcyon.view.TreePanel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.dockfx.DockNode;
import org.dockfx.DockPane;

/**
 * FxFrame support JavaFX based on docking framework.
 */
public class HalcyonFrame extends Application
{
	
	private double mWindowWidth,mWindowHeight;
	
	final private HalcyonNodeRepository nodes;

	final private ObservableCollection<DockNode> mConsoleDockNodes = new ObservableCollection<>();
	final private ObservableCollection<DockNode> mToolBarDockNodes = new ObservableCollection<>();

  	private TreePanel controlWindow;

	final private Menu mViewMenu = new Menu("View");
	final private MenuBar mMenuBar = new MenuBar( mViewMenu );

	private ViewManager view;

	private Stage mPrimaryStage;

	/**
	 * Gets the ViewManager.
	 * @return the ViewManager
	 */
	public ViewManager getViewManager()
	{
		return view;
	}

	/**
	 * Instantiates a new Halcyon frame.
	 * @param pWindowWidth the p window width
	 * @param pWindowHeight the p window height
	 */
	public HalcyonFrame(int pWindowWidth, int pWindowHeight)
	{
		mWindowWidth = pWindowWidth;
		mWindowHeight = pWindowHeight;
		this.nodes = new HalcyonNodeRepository();
	}

	/**
	 * Sets tree panel.
	 * @param controlWindow the control window
	 */
	public void setTreePanel( TreePanel controlWindow )
	{
		this.controlWindow = controlWindow;
		this.controlWindow.setNodes(nodes);
	}

	/**
	 * Add a Halcyon node.
	 * @param node the node
	 */
	public void addNode(HalcyonNodeInterface node)
	{
		nodes.add(node);
	}

	/**
	 * Add a toolbar.
	 * @param toolbar the toolbar
	 */
	public void addToolbar(DockNode toolbar)
	{
		mToolBarDockNodes.add(toolbar);
	}

	/**
	 * Add a console.
	 * @param console the console
	 */
	public void addConsole(DockNode console)
	{
		mConsoleDockNodes.add(console);
	}

	/**
	 * Starts JavaFX application.
	 * @param pPrimaryStage the p primary stage
	 */
	@Override
	public void start(Stage pPrimaryStage)
	{

		if (Platform.isFxApplicationThread())
			internalStart(pPrimaryStage);
		else
			Platform.runLater(() -> {
				internalStart(pPrimaryStage);
			});

	}

	private void internalStart(Stage pPrimaryStage)
	{
		mPrimaryStage = pPrimaryStage;
		mPrimaryStage.setTitle("Halcyon");

		// create a dock pane that will manage our dock nodes and handle the layout
		DockPane dockPane = new DockPane();

		Menu lToolbarMenu = new Menu("Toolbar");
		Menu lConsoleMenu = new Menu("Console");
		Menu lDeviceMenu = new Menu("Device");
		mViewMenu.getItems().addAll( lToolbarMenu, lConsoleMenu, lDeviceMenu );

		view = new ViewManager(	dockPane,
														controlWindow,
														nodes,
														mConsoleDockNodes,
														mToolBarDockNodes,
														mViewMenu);
		this.controlWindow.setViewManager(view);

		BorderPane lBorderPane = new BorderPane();
		lBorderPane.setTop(mMenuBar);
		lBorderPane.setCenter(dockPane);
		Scene lScene = new Scene(lBorderPane, mWindowWidth, mWindowHeight);

		mPrimaryStage.setScene(lScene);
		mPrimaryStage.sizeToScene();

		mPrimaryStage.show();

		// System.out.println(lScene.getWindow());

		// test the look and feel with both Caspian and Modena
		Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);

		// initialize the default styles for the dock pane and undocked nodes using
		// the DockFX
		// library's internal Default.css stylesheet
		// unlike other custom control libraries this allows the user to override
		// them globally
		// using the style manager just as they can with internal JavaFX controls
		// this must be called after the primary stage is shown
		// https://bugs.openjdk.java.net/browse/JDK-8132900
		DockPane.initializeDefaultUserAgentStylesheet();
	}

	/**
	 * External starts from Java swing environment.
	 */
	public void externalStart()
	{
		HalcyonFrame lThis = this;
		Platform.runLater(() -> {
			Stage stage = new Stage();
			try
			{
				lThis.start(stage);
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		});

	}

	/**
	 * External stops.
	 */
	public void externalStop()
	{
		HalcyonFrame lThis = this;
		Platform.runLater(() -> {
			try
			{
				lThis.stop();
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		});
	}

	/**
	 * Is visible boolean.
	 * @return the boolean
	 */
	public boolean isVisible()
	{
		return mPrimaryStage == null ? false : mPrimaryStage.isShowing();
	}
}
