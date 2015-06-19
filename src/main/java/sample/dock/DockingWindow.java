package sample.dock;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.RectGradientPainter;

import bibliothek.gui.dock.ScreenDockStation;
import bibliothek.gui.dock.StackDockStation;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.event.CVetoFocusListener;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.layout.FullLockConflictResolver;
import bibliothek.gui.dock.common.menu.CLookAndFeelMenuPiece;
import bibliothek.gui.dock.common.menu.CThemeMenuPiece;
import bibliothek.gui.dock.common.theme.ThemeMap;
import bibliothek.gui.dock.facile.menu.RootMenuPiece;
import bibliothek.gui.dock.station.screen.window.WindowConfiguration;
import bibliothek.gui.dock.station.stack.tab.DefaultTabContentFilter;

/**
 * Docking Window sample with JavaFX chart
 */
public class DockingWindow
{
	/** the icon of the application */
	private ImageIcon icon;

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

		final JFrame frame = new JFrame( "Docking Chart" );
		frame.setIconImage( icon.getImage() );
		final CControl control = new CControl( frame );
		control.putProperty( CControl.RESIZE_LOCK_CONFLICT_RESOLVER, new FullLockConflictResolver() );

		// TODO: menu items are replaced by necessary structure
		RootMenuPiece laf = new RootMenuPiece( "Look And Feel", false, new CLookAndFeelMenuPiece( control, null ));
		RootMenuPiece theme = new RootMenuPiece( "Theme", false, new CThemeMenuPiece( control ));
		JMenuBar bar = new JMenuBar();
		bar.add( laf.getMenu() );
		bar.add( theme.getMenu() );
		frame.setJMenuBar( bar );

		control.addMultipleDockableFactory( "chartFrame", ChartFrame.FACTORY );
		frame.add( control.getContentArea() );

		control.addVetoFocusListener( new CVetoFocusListener(){
			public boolean willLoseFocus( CDockable dockable ){
				if( dockable instanceof ChartFrame ){
					if( !((ChartFrame)dockable).isFocusLostAllowed() )
						return false;
				}

				return true;
			}
			public boolean willGainFocus( CDockable dockable ){
				return true;
			}
		});

		CGrid grid = new CGrid( control );
		for( int i = 0; i < 3; i++ ){
			for( int j = 0; j < 2; j++ ){
				ChartFrame f = new ChartFrame();
				control.addDockable( f );
				grid.add( i, j, 1, 1, f );
			}
		}

		ThemeMap themes = control.getThemes();
		themes.select(ThemeMap.KEY_ECLIPSE_THEME);

		control.putProperty( EclipseTheme.TAB_PAINTER, RectGradientPainter.FACTORY );
		control.putProperty( EclipseTheme.PAINT_ICONS_WHEN_DESELECTED, true );
		control.putProperty( StackDockStation.TAB_CONTENT_FILTER, new DefaultTabContentFilter( DefaultTabContentFilter.Behavior.ALL) );
		control.putProperty( ScreenDockStation.WINDOW_CONFIGURATION, ( station, dockable ) -> {
			WindowConfiguration config = new WindowConfiguration();
			config.setMoveOnTitleGrab( true );
			return config;
		} );

		control.getContentArea().deploy( grid );

		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		frame.setBounds( 20, 20, 500, 500 );

		frame.addWindowListener( new WindowAdapter(){
			@Override
			public void windowClosing( WindowEvent e ) {
				System.exit( 0 );
			}
		});

		frame.setVisible( true );
	}

	public static void main( String[] args ) {
		DockingWindow core = new DockingWindow();
		core.startup( );
	}
}
