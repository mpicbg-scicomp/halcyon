package halcyon;

import halcyon.model.list.HalcyonNodeRepository;
import halcyon.model.list.ObservableCollection;
import halcyon.model.node.HalcyonNodeInterface;
import halcyon.view.TreeDockNode;
import halcyon.view.ViewManager;
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

  private TreeDockNode controlWindow;

	final private Menu mToolbarMenu = new Menu("Toolbar");
	final private Menu mConsoleMenu = new Menu("Console");
	final private Menu mDeviceMenu = new Menu("Device");

	final private MenuBar mMenuBar = new MenuBar(	mToolbarMenu,
																								mConsoleMenu,
																								mDeviceMenu);

	private ViewManager view;

	private Stage mPrimaryStage;
	
	public ViewManager getViewManager()
	{
		return view;
	}

	public HalcyonFrame(int pWindowWidth, int pWindowHeight)
	{
		mWindowWidth = pWindowWidth;
		mWindowHeight = pWindowHeight;
		this.nodes = new HalcyonNodeRepository();
	}
	
	public void setTreeDockNode(TreeDockNode controlWindow)
	{
		this.controlWindow = controlWindow;
		this.controlWindow.setNodes(nodes);
	}

	public void addNode(HalcyonNodeInterface node)
	{
		nodes.add(node);
	}

	public void addToolbar(DockNode toolbar)
	{
		mToolBarDockNodes.add(toolbar);
	}

	public void addConsole(DockNode console)
	{
		mConsoleDockNodes.add(console);
	}

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

		view = new ViewManager(	dockPane,
														controlWindow,
														nodes,
														mConsoleDockNodes,
														mToolBarDockNodes,
														mMenuBar);
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

	public boolean isVisible()
	{
		return mPrimaryStage == null ? false : mPrimaryStage.isShowing();
	}
}
