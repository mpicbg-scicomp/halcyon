package halcyon.util;

import javafx.stage.Stage;

/**
 * RunnableFX is a common interface for all the JavaFX based GUI components
 * utils.RunFX.start() can launch all the inherited class instances.
 */
public interface RunnableFX
{
  /**
   * Initiates RunnableFX application.
   */
  public void init();

  /**
   * Starts the RunnableFX application.
   * 
   * @param stage
   *          the stage
   */
  public void start(Stage stage);

  /**
   * Stops the RunnableFX application.
   */
  public void stop();
}
