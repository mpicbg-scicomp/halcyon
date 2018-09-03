package halcyon.demo;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import org.dockfx.DockNode;

/**
 * Demo Toolbar window for demonstrating. Users can make their own ToolBar
 * window and add them {@link halcyon.HalcyonFrame}. Please, have a look
 * {@link DemoHalcyonMain}.
 */
public class DemoToolbarPanel extends DockNode
{

  public DemoToolbarPanel()
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
