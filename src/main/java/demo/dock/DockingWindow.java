package demo.dock;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.RectGradientPainter;
import bibliothek.gui.DockController;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.StackDockStation;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.menu.*;
import bibliothek.gui.dock.common.theme.ThemeMap;
import bibliothek.gui.dock.facile.menu.RootMenuPiece;
import bibliothek.gui.dock.facile.menu.SubmenuPiece;
import bibliothek.gui.dock.station.stack.tab.DefaultTabContentFilter;
import bibliothek.gui.dock.support.lookandfeel.ComponentCollector;
import bibliothek.gui.dock.support.menu.SeparatingMenuPiece;

import model.HalcyonNodeRepository;
import view.ViewManager;

import javax.swing.*;

/**
 * Docking Window
 */
public class DockingWindow implements ComponentCollector
{
	/** the icon of the application */
	private ImageIcon icon;

	private CControl control;

	/** the applications main frame */
	private JFrame frame;

	private ViewManager view;

	/**
	 * Creates a new core, containing global resources.
	 */
	public DockingWindow(){
			icon = new ImageIcon( DockingWindow.class.getResource( "/icon.png" ));
	}

	/**
	 * Starts this application.
	 */
	public void startup( ){

		buildContent();

		buildMenu();

		frame.setVisible( true );
	}

	/**
	 * Builds the menubar and adds it to {@link #frame}
	 */
	private void buildMenu(){

		RootMenuPiece settings = new RootMenuPiece( "View", false );
		settings.add( new SingleCDockableListMenuPiece( control ));
		settings.add( new SeparatingMenuPiece( new CLayoutChoiceMenuPiece( control, false ), true, false, false ));

		RootMenuPiece layout = new RootMenuPiece( "Layout", false );
		layout.add( new SubmenuPiece( "LookAndFeel", true, new CLookAndFeelMenuPiece( control )));
		layout.add( new SubmenuPiece( "Layout", true, new CThemeMenuPiece( control )));
		layout.add( CPreferenceMenuPiece.setup( control ));

		JMenuBar menubar = new JMenuBar();
		menubar.add( settings.getMenu() );
		menubar.add( layout.getMenu() );

		frame.setJMenuBar( menubar );
	}


	/**
	 * Creates the main frame and all {@link bibliothek.gui.DockStation}s.
	 */
	private void buildContent(){
		frame = new JFrame();
		frame.setTitle( "Hacyon" );
		frame.setIconImage( icon.getImage() );

		DockController.disableCoreWarning();
		control = new CControl( frame );

		ThemeMap themes = control.getThemes();
		themes.select( ThemeMap.KEY_ECLIPSE_THEME );

		control.putProperty( EclipseTheme.TAB_PAINTER, RectGradientPainter.FACTORY );
		control.putProperty( EclipseTheme.PAINT_ICONS_WHEN_DESELECTED, true );
		control.putProperty( StackDockStation.TAB_CONTENT_FILTER,
				new DefaultTabContentFilter( DefaultTabContentFilter.Behavior.ALL ) );

		frame.getContentPane().add( control.getContentArea() );

		HalcyonNodeRepository nodes = new HalcyonNodeRepository();
		view = new ViewManager( control, nodes );

		view.getWorkingArea().setVisible( true );

		frame.setBounds( 20, 20, 800, 600 );
		frame.setTitle( "Halcyon" );
		frame.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
		frame.addWindowListener( new WindowAdapter(){
			@Override
			public void windowClosing( WindowEvent e ){
				shutdown();
			}
		});
	}

	/**
	 * Closes the graphical user interface and frees resources.
	 */
	public void shutdown(){
		frame.dispose();
		control.destroy();
	}

	public static void main( String[] args ) {
		DockingWindow core = new DockingWindow();
		core.startup( );
	}

	@Override public Collection<Component> listComponents()
	{
		java.util.List<Component> list = new ArrayList<Component>();
		list.add( frame );
		for( Dockable d : control.getController().getRegister().listDockables() )
			list.add( d.getComponent() );
		return list;
	}
}
