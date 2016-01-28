package window.demo;

import demo.DemoType;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import model.node.HalcyonNode;
import model.list.HalcyonNodeRepository;

import view.ViewManager;

import window.control.ControlWindowBase;
import window.toolbar.ToolbarInterface;
import window.util.WavelengthColors;

/**
 * Control type Toolbar window
 */
public class DemoToolbarWindow extends ControlWindowBase implements
		ToolbarInterface
{
	final private HalcyonNodeRepository nodes;

	public DemoToolbarWindow( final ViewManager manager )
	{
		super( new VBox() );
		getDockTitleBar().setVisible( false );

		nodes = manager.getNodes();

		setTitle( "Demo Toolbar" );

		final VBox box = (VBox) getContents();

		Button btn = new Button( "Add Camera-1" );
		btn.setOnAction( e -> {
			final HalcyonNode n = new HalcyonNode( "Camera-1", DemoType.ONE );
			final VBox cameraPanel = new VBox();

			cameraPanel.getChildren().add( new Button( "Test Button 1" ) );
			cameraPanel.getChildren().add( new Button( "Test Button 2" ) );
			cameraPanel.getChildren().add( new Button( "Test Button 3" ) );
			cameraPanel.getChildren().add( new Button( "Test Button 4" ) );

			n.setPanel( cameraPanel );

			nodes.add( n );
		} );

		box.getChildren().add( btn );

		btn = new Button( "Add Laser-1" );
		btn.setOnAction( e -> {
			final HalcyonNode n = new HalcyonNode( "Laser-1", DemoType.TWO );
			final VBox laserPanel = new VBox();

			laserPanel.getChildren().add( new Label( "Label1" ) );
			laserPanel.getChildren().add( new TextField( "TextField1" ) );
			laserPanel.getChildren().add( new Label( "Label2" ) );
			laserPanel.getChildren().add( new TextField( "TextField2" ) );

			n.setPanel( laserPanel );

			nodes.add( n );
		} );

		box.getChildren().add( btn );

		btn = new Button( "Add Laser-2" );
		btn.setOnAction( e -> {
			final HalcyonNode n = HalcyonNode.wrap( "Laser-2", DemoType.ONE, new VBox(  ) );
			nodes.add( n );
		} );

		box.getChildren().add( btn );

		btn = new Button( "Test Std Out" );
		btn.setOnAction( e -> {

			for (int i = 0; i < 2000; i++)
			{
				System.out.println( "" + i + " " + "Console Test" );
			}

		} );

		box.getChildren().add( btn );

		btn = new Button( "Test Std Err" );
		btn.setOnAction( e -> {

			for (int i = 0; i < 2000; i++)
			{
				System.err.println( "" + i + " " + "Console Test" );
			}

		} );

		box.getChildren().add( btn );

		// Wavelength color check
		btn = new Button( "488" );
		btn.setStyle( "-fx-background-color: " + WavelengthColors.getWebColorString( "488" ) );
		box.getChildren().add( btn );
	}
}
