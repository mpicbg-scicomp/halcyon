package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Closeable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import model.HalcyonNodeInterface;
import model.HalcyonNodeRepository;
import model.ObservableCollection;
import window.ConsoleInterface;
import window.ToolBarInterface;
import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.RectGradientPainter;
import bibliothek.gui.DockController;
import bibliothek.gui.dock.StackDockStation;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.menu.CLayoutChoiceMenuPiece;
import bibliothek.gui.dock.common.menu.CLookAndFeelMenuPiece;
import bibliothek.gui.dock.common.menu.CPreferenceMenuPiece;
import bibliothek.gui.dock.common.menu.CThemeMenuPiece;
import bibliothek.gui.dock.common.menu.SingleCDockableListMenuPiece;
import bibliothek.gui.dock.common.theme.ThemeMap;
import bibliothek.gui.dock.facile.menu.RootMenuPiece;
import bibliothek.gui.dock.facile.menu.SubmenuPiece;
import bibliothek.gui.dock.station.stack.tab.DefaultTabContentFilter;
import bibliothek.gui.dock.support.menu.SeparatingMenuPiece;

/**
 * Halcyon Main Frame
 */
public class HalcyonFrame extends JFrame implements Closeable
{
	public enum GUIBackend { Swing, JavaFX }

	final private GUIBackend backend;

	/** the icon of the application */
	private final ImageIcon icon;

	private CControl control;


	public ViewManager getViewManager()
	{
		return view;
	}

	private ViewManager view;

	private final HalcyonNodeRepository nodes;

	final private ObservableCollection<ConsoleInterface> consoleWindows = new ObservableCollection<>();

	final private ObservableCollection<ToolBarInterface> toolbarWindows = new ObservableCollection<>();

	public HalcyonFrame(GUIBackend backend)
	{
		this.backend = backend;
		this.nodes = new HalcyonNodeRepository();
		this.icon = new ImageIcon( HalcyonFrame.class.getResource( "/icon.png" ));

		buildContent();
		buildMenu();
	}

	public void addNode( HalcyonNodeInterface node )
	{
		nodes.add( node );
	}

	public void addToolbar(ToolBarInterface toolbar)
	{
		toolbarWindows.add( toolbar );
	}

	public void addConsole( ConsoleInterface console )
	{
		consoleWindows.add( console );
	}

	/**
	 * Builds the menubar and adds it to {@link #frame}
	 */
	private void buildMenu(){

		final RootMenuPiece settings = new RootMenuPiece( "View", false );
		settings.add( new SingleCDockableListMenuPiece( control ));
		settings.add( new SeparatingMenuPiece( new CLayoutChoiceMenuPiece( control, false ), true, false, false ));

		final RootMenuPiece layout = new RootMenuPiece( "Layout", false );
		layout.add( new SubmenuPiece( "LookAndFeel", true, new CLookAndFeelMenuPiece( control )));
		layout.add( new SubmenuPiece( "Layout", true, new CThemeMenuPiece( control )));
		layout.add( CPreferenceMenuPiece.setup( control ));

		final JMenuBar menubar = new JMenuBar();
		menubar.add( settings.getMenu() );
		menubar.add( layout.getMenu() );

		setJMenuBar(menubar);
	}


	/**
	 * Creates the main frame and all {@link bibliothek.gui.DockStation}s.
	 */
	private void buildContent(){
		setTitle("Hacyon");
		setIconImage(icon.getImage());

		DockController.disableCoreWarning();
		control = new CControl(this);

		final ThemeMap themes = control.getThemes();
		themes.select( ThemeMap.KEY_ECLIPSE_THEME );

		control.putProperty( EclipseTheme.TAB_PAINTER, RectGradientPainter.FACTORY );
		control.putProperty( EclipseTheme.PAINT_ICONS_WHEN_DESELECTED, true );
		control.putProperty( StackDockStation.TAB_CONTENT_FILTER,
				new DefaultTabContentFilter( DefaultTabContentFilter.Behavior.ALL ) );

		getContentPane().add(control.getContentArea());

		view = new ViewManager( control, nodes, backend, consoleWindows, toolbarWindows );

		view.getWorkingArea().setVisible( true );

		setBounds(20, 20, 800, 600);
		setTitle("Halcyon");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing( WindowEvent e ){
				close();
			}
		});
	}

	/**
	 * Closes the graphical user interface and frees resources.
	 */
	@Override
	public void close()
	{
		dispose();
		control.destroy();
	}
}
