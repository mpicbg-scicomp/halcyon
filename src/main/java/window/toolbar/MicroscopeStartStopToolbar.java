package window.toolbar;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Microscope Start/Stop toolbar
 */
public class MicroscopeStartStopToolbar extends ToolbarBase
{
	public MicroscopeStartStopToolbar()
	{
		super( new VBox() );
		getDockTitleBar().setVisible( false );

		setTitle( "Start/Stop" );

		final VBox panel = (VBox) getContents();

		Button btn = new Button("Start");
		btn.setOnAction( ( e ) -> {
			System.out.println( "START" );
		} );

		panel.getChildren().add( btn );

		btn = new Button("Stop");
		btn.setOnAction( ( e ) -> {
			System.out.println( "STOP" );
		} );

		panel.getChildren().add( btn );

		setContents( panel );
	}
}
