package view;

import javafx.application.Application;
import javafx.scene.Scene;

import javafx.stage.Stage;
import model.list.HalcyonNodeRepository;
import model.list.ObservableCollection;

import model.node.HalcyonNodeInterface;
import org.dockfx.DockPane;
import window.console.ConsoleInterface;
import window.toolbar.ToolbarInterface;

/**
 * FxFrame support JavaFX based on docking framework.
 */
public class FxFrame extends Application
{
	final private HalcyonNodeRepository nodes;

	final private ObservableCollection<ConsoleInterface> consoleWindows = new ObservableCollection<>();

	final private ObservableCollection<ToolbarInterface> toolbarWindows = new ObservableCollection<>();

	public ViewManager getViewManager()
	{
		return view;
	}

	private ViewManager view;

	public FxFrame()
	{
		this.nodes = new HalcyonNodeRepository();
	}

	public void addNode( HalcyonNodeInterface node )
	{
		nodes.add( node );
	}

	public void addToolbar( ToolbarInterface toolbar)
	{
		toolbarWindows.add( toolbar );
	}

	public void addConsole( ConsoleInterface console )
	{
		consoleWindows.add( console );
	}

	@Override public void start( Stage primaryStage ) throws Exception
	{
		primaryStage.setTitle("Halcyon");

		// create a dock pane that will manage our dock nodes and handle the layout
		DockPane dockPane = new DockPane();

		view = new ViewManager( dockPane, nodes, consoleWindows, toolbarWindows );

		primaryStage.setScene(new Scene(dockPane, 800, 500));
		primaryStage.sizeToScene();

		primaryStage.show();

		// test the look and feel with both Caspian and Modena
		Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);

		// initialize the default styles for the dock pane and undocked nodes using the DockFX
		// library's internal Default.css stylesheet
		// unlike other custom control libraries this allows the user to override them globally
		// using the style manager just as they can with internal JavaFX controls
		// this must be called after the primary stage is shown
		// https://bugs.openjdk.java.net/browse/JDK-8132900
		DockPane.initializeDefaultUserAgentStylesheet();

	}
}
