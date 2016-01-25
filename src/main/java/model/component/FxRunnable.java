package model.component;

import javafx.stage.Stage;

/**
 * FxRunnable is a common interface for all the JavaFX based GUI components
 * utils.RunFX.start() can launch all the inherited class instances.
 */
public interface FxRunnable
{
	public void init();
	public void start(Stage stage);
	public void stop();
}
