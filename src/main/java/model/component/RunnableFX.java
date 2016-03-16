package model.component;

import javafx.stage.Stage;

/**
 * RunnableFX is a common interface for all the JavaFX based GUI components
 * utils.RunFX.start() can launch all the inherited class instances.
 */
public interface RunnableFX
{
	public void init();

	public void start(Stage stage);

	public void stop();
}
