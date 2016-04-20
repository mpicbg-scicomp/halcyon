package halcyon;

import javafx.scene.layout.BorderPane;
import org.dockfx.DockPane;

import halcyon.model.list.HalcyonNodeRepository;
import halcyon.model.list.ObservableCollection;
import halcyon.model.node.HalcyonNodeInterface;
import halcyon.view.ViewManager;
import halcyon.window.console.ConsoleInterface;
import halcyon.window.control.ControlWindowBase;
import halcyon.window.toolbar.ToolbarInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.dockfx.DockPane;

import java.util.Arrays;

/**
 * FxFrame support JavaFX based on docking framework.
 */
public class HalcyonFrame<T> extends Application
{
	final private HalcyonNodeRepository nodes;

	final private ObservableCollection<ConsoleInterface> consoleWindows = new ObservableCollection<>();

	final private ObservableCollection<ToolbarInterface> toolbarWindows = new ObservableCollection<>();

	final private ControlWindowBase controlWindow;

	final private Menu mToolbarMenu = new Menu( "Toolbar" );
	final private Menu mConsoleMenu = new Menu( "Console" );
	final private Menu mDeviceMenu = new Menu( "Device" );

	final private MenuBar mMenuBar = new MenuBar( mToolbarMenu, mConsoleMenu, mDeviceMenu );

	public ViewManager getViewManager()
	{
		return view;
	}

	private ViewManager view;

	private Stage mPrimaryStage;

	public HalcyonFrame(ControlWindowBase controlWindow)
	{
		this.nodes = new HalcyonNodeRepository();
		this.controlWindow = controlWindow;
		this.controlWindow.setNodes(nodes);
	}

	public void addNode(HalcyonNodeInterface node)
	{
		nodes.add(node);
	}

	public void addToolbar(ToolbarInterface toolbar)
	{
		toolbarWindows.add( toolbar );
	}

	public void addConsole(ConsoleInterface console)
	{
		consoleWindows.add(console);
	}

	@Override
	public void start(Stage pPrimaryStage)
	{
		mPrimaryStage = pPrimaryStage;
		mPrimaryStage.setTitle("Halcyon");

		// create a dock pane that will manage our dock nodes and handle the layout
		DockPane dockPane = new DockPane();

		view = new ViewManager(	dockPane,
														controlWindow,
														nodes,
														consoleWindows,
														toolbarWindows,
														mMenuBar);
		this.controlWindow.setViewManager(view);

		BorderPane lBorderPane = new BorderPane();
		lBorderPane.setTop( mMenuBar );
		lBorderPane.setCenter( dockPane );
		Scene lScene = new Scene( lBorderPane, 800, 600 );

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
