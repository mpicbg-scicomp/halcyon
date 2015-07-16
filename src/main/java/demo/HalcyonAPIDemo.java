package demo;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.FxChartHalcyonNode;
import model.HalcyonNode;
import model.HalcyonNodeType;

import org.junit.Test;

import view.HalcyonFrame;
import window.DemoToolbarWindow;
import window.MicroscopeStartStopToolbar;
import window.StdOutputCaptureConsole;

public class HalcyonAPIDemo
{
	@Test
	public void demo() throws InvocationTargetException,
										InterruptedException
	{
		// TODO: support other type of devices
		final HalcyonFrame lHalcyonFrame = new HalcyonFrame( HalcyonFrame.GUIBackend.Swing );

		final HalcyonNode lLaser1 = new FxChartHalcyonNode( "Laser-1", HalcyonNodeType.Laser );

		final HalcyonNode lLaser2 = new FxChartHalcyonNode( "Laser-2", HalcyonNodeType.Laser );

		final HalcyonNode lCamera = new FxChartHalcyonNode( "Camera-1", HalcyonNodeType.Camera );

		final HalcyonNode lStage1 = HalcyonNode.wrap( "Stage-1", HalcyonNodeType.Stage, new JPanel() );

		lHalcyonFrame.addNode( lLaser1 );
		lHalcyonFrame.addNode( lLaser2 );
		lHalcyonFrame.addNode( lCamera );
		lHalcyonFrame.addNode(lStage1);

		lHalcyonFrame.addToolbar( new DemoToolbarWindow( lHalcyonFrame.getViewManager() ) );
		lHalcyonFrame.addToolbar( new MicroscopeStartStopToolbar() );
		lHalcyonFrame.addConsole( new StdOutputCaptureConsole() );

		SwingUtilities.invokeAndWait(() -> {
			lHalcyonFrame.setVisible(true);
		});


		while (lHalcyonFrame.isVisible())
		{
			Thread.sleep(100);
		}
		
	}

	public static void main(String[] args) throws InvocationTargetException,
																				InterruptedException
	{
		new HalcyonAPIDemo().demo();
	}
}
