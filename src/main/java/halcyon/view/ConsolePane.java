package halcyon.view;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * ConsolePane consists of ConsolePanel and a button for clearing the contents
 * of console
 */
public class ConsolePane extends BorderPane
{
  ConsolePanel mConsolePanel;

  public ConsolePane()
  {
    mConsolePanel = new ConsolePanel();
    Button lClearButton = new Button("Clear");
    lClearButton.setOnAction(event -> {
      mConsolePanel.clearText();
      ;
    });

    setTop(lClearButton);
    setCenter(mConsolePanel);
  }

  public ConsolePanel getConsolePanel()
  {
    return mConsolePanel;
  }

}
