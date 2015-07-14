package view;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.CWorkingArea;
import bibliothek.gui.dock.common.MultipleCDockableFactory;
import bibliothek.gui.dock.common.MultipleCDockableLayout;
import bibliothek.gui.dock.common.event.CDockableAdapter;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.util.xml.XElement;

import model.HalcyonNode;
import model.HalcyonNodeRepository;
import model.HalcyonNodeRepositoryListener;

import window.ConsoleWindow;
import window.FxConfigWindow;
import window.ToolbarWindow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.LinkedList;
import java.util.List;

/**
 * ViewManager class for managing Windows
 */
public class ViewManager
{
	/** the controller of the whole framework */
	private CControl control;

	private FxConfigWindow configWindow;

	private ToolbarWindow toolbarWindow;

	private ConsoleWindow consoleWindow;

	/** the {@link bibliothek.gui.dock.common.intern.CDockable}s showing some {@link HalcyonNode}s */
	private List<HalcyonNodeDockable> pages = new LinkedList<HalcyonNodeDockable>();

	/** the factory which creates new {@link HalcyonNodeFactory}s */
	private HalcyonNodeFactory pageFactory;

	/** a set of {@link HalcyonNode}s */
	private HalcyonNodeRepository nodes;

	/** the area on which the {@link HalcyonNode}s are shown */
	private CWorkingArea workingArea;

	public ViewManager( CControl control, HalcyonNodeRepository nodes ){
		this.control = control;
		this.nodes = nodes;

		pageFactory = new HalcyonNodeFactory();
		control.addMultipleDockableFactory( "page", pageFactory );

		workingArea = control.createWorkingArea( "halcyon node area" );
		workingArea.setLocation( CLocation.base().normalRectangle( 0, 0, 1, 1 ) );
		workingArea.setVisible( true );

		toolbarWindow = new ToolbarWindow( this );
		control.addDockable( toolbarWindow );
		toolbarWindow.setLocation( CLocation.base().normalWest( 0.3 ).north( 0.5 ) );
		toolbarWindow.setVisible( true );

		configWindow = new FxConfigWindow( this );
		control.addDockable( configWindow );
		configWindow.setLocation( CLocation.base().normalWest( 0.3 ).south( 0.5 ) );
		configWindow.setVisible( true );

		consoleWindow = new ConsoleWindow();
		control.addDockable( consoleWindow );
		consoleWindow.setLocation( CLocation.base().normalEast( 0.7 ).south( 0.3 ) );
		consoleWindow.setVisible( true );

		nodes.addListener( new HalcyonNodeRepositoryListener(){
			public void nodeAdded( HalcyonNode node ){
				open( node );
			}
			public void nodeRemoved( HalcyonNode node ){
				closeAll( node );
			}
		});
	}

	public HalcyonNodeRepository getNodes()
	{
		return nodes;
	}

	public CControl getControl() {
		return control;
	}

	public CWorkingArea getWorkingArea() {
		return workingArea;
	}

	public void open( HalcyonNode node ){

		for(HalcyonNodeDockable n: pages)
		{
			if(n.getNode() == node) return;
		}

		final HalcyonNodeDockable page = new HalcyonNodeDockable( pageFactory );
		page.addCDockableStateListener( new CDockableAdapter(){
			@Override
			public void visibilityChanged( CDockable dockable ) {
				if( dockable.isVisible() ){
					pages.add( page );
				}
				else{
					pages.remove( page );
				}
			}
		});

		page.setNode( node );

		page.setLocation( CLocation.working( workingArea ).rectangle( 0, 0, 1, 1 ) );
		workingArea.add( page );
		page.setVisible( true );
	}

	public void closeAll( HalcyonNode node ){
		for( HalcyonNodeDockable page : pages.toArray( new HalcyonNodeDockable[ pages.size() ] )){
			if( page.getNode()  == node ){
				page.setVisible( false );
				control.removeDockable( page );
			}
		}
	}


	/**
	 * A factory which creates {@link view.HalcyonNodeDockable}s.
	 */
	private class HalcyonNodeFactory implements MultipleCDockableFactory<HalcyonNodeDockable, HalcyonNodeLayout>
	{
		public HalcyonNodeLayout create() {
			return new HalcyonNodeLayout();
		}

		public HalcyonNodeDockable read( HalcyonNodeLayout layout ) {
			String name = layout.getName();
			HalcyonNode node = nodes.getNode( name );
			if( node == null )
				return null;
			final HalcyonNodeDockable page = new HalcyonNodeDockable( this );
			page.addCDockableStateListener( new CDockableAdapter(){
				@Override
				public void visibilityChanged( CDockable dockable ) {
					if( dockable.isVisible() ){
						pages.add( page );
					}
					else{
						pages.remove( page );
					}
				}
			});
			page.setNode( node );
			return page;
		}

		public HalcyonNodeLayout write( HalcyonNodeDockable dockable ) {
			HalcyonNodeLayout layout = new HalcyonNodeLayout();
			layout.setName( dockable.getNode().getName() );
			return layout;
		}

		public boolean match( HalcyonNodeDockable dockable, HalcyonNodeLayout layout ){
			String name = dockable.getNode().getName();
			return name.equals( layout.getName() );
		}
	}

	/**
	 * Describes the layout of one {@link HalcyonNodeDockable}
	 */
	private static class HalcyonNodeLayout implements MultipleCDockableLayout
	{
		/** the name of the picture */
		private String name;

		/**
		 * Sets the name of the picture that is shown.
		 * @param name the name of the picture
		 */
		public void setName( String name ) {
			this.name = name;
		}

		/**
		 * Gets the name of the picture that is shown.
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		public void readStream( DataInputStream in ) throws IOException
		{
			name = in.readUTF();
		}

		public void readXML( XElement element ) {
			name = element.getString();
		}

		public void writeStream( DataOutputStream out ) throws IOException {
			out.writeUTF( name );
		}

		public void writeXML( XElement element ) {
			element.setString( name );
		}
	}
}
