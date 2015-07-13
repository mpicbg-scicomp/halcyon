package demo.dock;

import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.EmptyMultipleCDockableFactory;
import demo.javafx.SwingInterop;

import javax.swing.JApplet;


/**
 * Chart Frame for Docking Window
 */
public class ChartFrame extends DefaultMultipleCDockable
{
	public static final EmptyMultipleCDockableFactory<ChartFrame> FACTORY = new EmptyMultipleCDockableFactory<ChartFrame>(){
		@Override
		public ChartFrame createDockable() {
			return new ChartFrame();
		}
	};

	public ChartFrame()
	{
		super( FACTORY );
		setTitleText( "Chart" );

		JApplet applet = new SwingInterop();
		applet.init();

		getContentPane().add( applet.getContentPane() );

		applet.start();
	}

	public boolean isFocusLostAllowed(){
		return true;
	}
}
