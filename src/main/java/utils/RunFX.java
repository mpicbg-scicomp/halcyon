package utils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.SceneBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.WindowEvent;
import model.component.RunnableFX;

/**
 * RunFx is an utility class to start any inherited class from FxControl
 */
public class RunFX extends Application
{
	private RunnableFX ctrl;

	@Override public void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		String className = getParameters().getRaw().get( 0 );

//		System.out.println(className);
		Class theClass = Class.forName(className, true,
				Thread.currentThread().getContextClassLoader());

		ctrl = ( RunnableFX ) theClass.newInstance();
		ctrl.init();
	}

	@Override public void start( Stage primaryStage ) throws Exception
	{
		ctrl.start( primaryStage );
	}

	public static void start( RunnableFX ctrl )
	{
		launch( new String[] { ctrl.getClass().getName() } );
//		JFXPanel panel = new JFXPanel();
//		Platform.runLater(new Runnable() {
//			public void run() {
//				RunFX fx = new RunFX( ctrl );
//
//				Stage stage = StageBuilder.create()
//						.scene( panel.getScene() )
//						.onCloseRequest(new EventHandler<WindowEvent >() {
//							@Override
//							public void handle(WindowEvent windowEvent) {
//								panel.setVisible( false );
//								System.exit(0);
//							}
//						})
//						.build();
//
//				try
//				{
//					fx.init();
//					fx.start( stage );
//				}
//				catch ( Exception e )
//				{
//					e.printStackTrace();
//				}
//			}
//		});
	}
}
