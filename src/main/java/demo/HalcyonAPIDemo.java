package demo;

import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.node.HalcyonNode;
import model.node.HalcyonNodeType;

import org.junit.Test;

import view.FxFrame;
import window.demo.DemoToolbarWindow;
import window.toolbar.MicroscopeStartStopToolbar;
import window.console.StdOutputCaptureConsole;

public class HalcyonAPIDemo extends Application
{
	@Test
	public void demo() throws InvocationTargetException, InterruptedException
	{
		// TODO: support other type of devices
		final FxFrame lHalcyonFrame = new FxFrame();

		final HalcyonNode lLaser1 = HalcyonNode.wrap( "Laser-1", HalcyonNodeType.Laser, new VBox(  ) );

		final HalcyonNode lLaser2 = HalcyonNode.wrap( "Laser-2", HalcyonNodeType.Laser, new VBox(  ) );

		final HalcyonNode lCamera = HalcyonNode.wrap( "Camera-1", HalcyonNodeType.Camera, new VBox(  ) );

		final HalcyonNode lStage1 = HalcyonNode.wrap( "Stage-1", HalcyonNodeType.Stage, new VBox(  ) );

		final HalcyonNode lLightSheet1 = HalcyonNode.wrap( "LightSheet-1", HalcyonNodeType.LightSheet, new VBox(  ) );

		final HalcyonNode lFilterWheel1 = HalcyonNode.wrap( "FilterWheel-1", HalcyonNodeType.FilterWheel, new VBox(  ) );

		final HalcyonNode lAdaptiveOptics1 = HalcyonNode.wrap( "AdaptiveOptics-1", HalcyonNodeType.AdaptiveOptics, new VBox(  ) );

		final HalcyonNode lOther1 = HalcyonNode.wrap( "Other-1", HalcyonNodeType.Other, new VBox(  ) );

		lHalcyonFrame.addNode( lLaser1 );
		lHalcyonFrame.addNode( lLaser2 );
		lHalcyonFrame.addNode( lCamera );
		lHalcyonFrame.addNode( lStage1 );
		lHalcyonFrame.addNode( lLightSheet1 );
		lHalcyonFrame.addNode( lFilterWheel1 );
		lHalcyonFrame.addNode( lAdaptiveOptics1 );
		lHalcyonFrame.addNode( lOther1 );


		lHalcyonFrame.addToolbar( new DemoToolbarWindow( lHalcyonFrame.getViewManager() ) );
		lHalcyonFrame.addToolbar( new MicroscopeStartStopToolbar() );
		lHalcyonFrame.addConsole( new StdOutputCaptureConsole() );

	}

	@Test
	public void demoJFX(Stage primaryStage) throws Exception
	{
		// TODO: support other type of devices
		final FxFrame lHalcyonFrame = new FxFrame(  );
		lHalcyonFrame.start( primaryStage );

		final HalcyonNode lLaser1 = HalcyonNode.wrap( "Laser-1", HalcyonNodeType.Laser, new VBox(  ) );

		final HalcyonNode lLaser2 = HalcyonNode.wrap( "Laser-2", HalcyonNodeType.Laser, new VBox(  ) );

		final HalcyonNode lCamera = HalcyonNode.wrap( "Camera-1", HalcyonNodeType.Camera, new VBox(  ) );

		final HalcyonNode lStage1 = HalcyonNode.wrap( "Stage-1", HalcyonNodeType.Stage, new VBox(  ) );

		final HalcyonNode lLightSheet1 = HalcyonNode.wrap( "LightSheet-1", HalcyonNodeType.LightSheet, new VBox(  ) );

		final HalcyonNode lFilterWheel1 = HalcyonNode.wrap( "FilterWheel-1", HalcyonNodeType.FilterWheel, new VBox(  ) );

		final HalcyonNode lAdaptiveOptics1 = HalcyonNode.wrap( "AdaptiveOptics-1", HalcyonNodeType.AdaptiveOptics, new VBox(  ) );

		final HalcyonNode lOther1 = HalcyonNode.wrap( "Other-1", HalcyonNodeType.Other, new VBox(  ) );

		lHalcyonFrame.addNode( lLaser1 );
		lHalcyonFrame.addNode( lLaser2 );
		lHalcyonFrame.addNode( lCamera );
		lHalcyonFrame.addNode( lStage1 );
		lHalcyonFrame.addNode( lLightSheet1 );
		lHalcyonFrame.addNode( lFilterWheel1 );
		lHalcyonFrame.addNode( lAdaptiveOptics1 );
		lHalcyonFrame.addNode( lOther1 );


		lHalcyonFrame.addToolbar( new DemoToolbarWindow( lHalcyonFrame.getViewManager() ) );
		lHalcyonFrame.addToolbar( new MicroscopeStartStopToolbar() );
		lHalcyonFrame.addConsole( new StdOutputCaptureConsole() );

	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setOnCloseRequest( event -> System.exit( 0 ) );
		new HalcyonAPIDemo().demoJFX(primaryStage);
	}

	public static void main(String[] args) throws Exception
	{
		launch(args);
	}
}
