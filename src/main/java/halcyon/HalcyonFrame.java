package halcyon;

import halcyon.controller.ViewManager;
import halcyon.model.collection.HalcyonNodeRepository;
import halcyon.model.collection.ObservableCollection;
import halcyon.model.node.HalcyonNodeInterface;
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

	final private double mWindowWidth, mWindowHeight;
	final private String mWindowtitle;
	final private String mAppIconPath;
	final private HalcyonNodeRepository mNodes;

	final private ObservableCollection<DockNode> mConsoleDockNodes = new ObservableCollection<>();
	final private ObservableCollection<DockNode> mToolBarDockNodes = new ObservableCollection<>();

	private TreePanel mTreePanel;

	private ViewManager mViewManager;

	private Stage mPrimaryStage;

	/**
	 * Gets the ViewManager.
	 * 
	 * @return the ViewManager
	 */
	public ViewManager getViewManager()
	{
		return mViewManager;
	}

	/**
	 * Instantiates a new Halcyon frame.
	 * 
	 * @param pWindowTitle
	 *          window title
	 * @param pAppIconPath
	 *          the app icon path
	 * @param pWindowWidth
	 *          the window width
	 * @param pWindowHeight
	 *          the window height
	 */

	public HalcyonFrame(String pWindowTitle,
											String pAppIconPath,
											int pWindowWidth,
											int pWindowHeight)
	{
		mWindowtitle = pWindowTitle;
		mAppIconPath = pAppIconPath;
		mWindowWidth = pWindowWidth;
		mWindowHeight = pWindowHeight;
		mNodes = new HalcyonNodeRepository();
	}

	/**
	 * Instantiates a new Halcyon frame.
	 * 
	 * @param pWindowTitle
	 *          window title
	 * @param pWindowWidth
	 *          the window width
	 * @param pWindowHeight
	 *          the window height
	 */
	public HalcyonFrame(String pWindowTitle,
											int pWindowWidth,
											int pWindowHeight)
	{
		this(pWindowTitle, null, pWindowWidth, pWindowHeight);
	}

	/**
	 * Sets tree panel.
	 * 
	 * @param pTreePanel
	 *          the Tree Panel
	 */
	public void setTreePanel(TreePanel pTreePanel)
	{
		mTreePanel = pTreePanel;
		mTreePanel.setNodes(mNodes);
	}

	/**
	 * Add a Halcyon node.
	 * 
	 * @param node
	 *          the node
	 */
	public void addNode(HalcyonNodeInterface node)
	{
		mNodes.add(node);
	}

	/**
	 * Add a toolbar.
	 * 
	 * @param toolbar
	 *          the toolbar
	 */
	public void addToolbar(DockNode toolbar)
	{
		mToolBarDockNodes.add(toolbar);
	}

	/**
	 * Add a console.
	 * 
	 * @param console
	 *          the console
	 */
	public void addConsole(DockNode console)
	{
		mConsoleDockNodes.add(console);
	}

	/**
	 * Starts JavaFX application.
	 * 
	 * @param pPrimaryStage
	 *          the p primary stage
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

		// create a dock pane that will manage our dock mNodes and handle the layout
		DockPane lDockPane = new DockPane();

		Menu lToolbarMenu = new Menu("Toolbar");
		Menu lConsoleMenu = new Menu("Console");

		Menu lViewMenu = new Menu("View");
		MenuBar lMenuBar = new MenuBar(lViewMenu);
		lViewMenu.getItems().addAll(lToolbarMenu, lConsoleMenu);

		mViewManager = new ViewManager(	lDockPane,
																		mTreePanel,
																		mNodes,
																		mConsoleDockNodes,
																		mToolBarDockNodes,
																		lViewMenu,
																		mAppIconPath);

		mTreePanel.setViewManager(mViewManager);

		BorderPane lBorderPane = new BorderPane();
		lBorderPane.setTop(lMenuBar);
		lBorderPane.setCenter(lDockPane);
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
			stage.setTitle(mWindowtitle);
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
	 * 
	 * @return the boolean
	 */
	public boolean isVisible()
	{
		return mPrimaryStage == null ? false : mPrimaryStage.isShowing();
	}
}
