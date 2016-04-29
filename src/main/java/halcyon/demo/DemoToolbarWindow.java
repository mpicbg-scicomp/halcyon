package halcyon.demo;

import org.dockfx.DockNode;

import halcyon.model.list.HalcyonNodeRepository;
import halcyon.model.node.HalcyonNode;
import halcyon.view.ViewManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Control type Toolbar window
 */
public class DemoToolbarWindow extends DockNode
{

	public DemoToolbarWindow()
	{
		super(new VBox());
		getDockTitleBar().setVisible(false);

		setTitle("Demo Toolbar");

		final VBox box = (VBox) getContents();

		Button btn = new Button("Test Std Out");
		btn.setOnAction(e -> {

			for (int i = 0; i < 2000; i++)
			{
				System.out.println("" + i + " " + "Console Test");
			}

		});

		box.getChildren().add(btn);

		btn = new Button("Test Std Err");
		btn.setOnAction(e -> {

			for (int i = 0; i < 2000; i++)
			{
				System.err.println("" + i + " " + "Console Test");
			}

		});

		box.getChildren().add(btn);

		// Wavelength color check
		btn = new Button("488");
		btn.setStyle("-fx-background-color: #0FAFF0");
		box.getChildren().add(btn);
	}
}
