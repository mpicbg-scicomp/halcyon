package halcyon.window.console;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * ConsolePane for Standard Out and Standard Error messages.
 */
public class ConsolePane extends ScrollPane implements TextAppender
{
	private static final long cCheckPeriod = 1_000_000_000;
	private volatile long mLastCheck = System.nanoTime();

	// It remains more than 1000 lines at some point
	final int maxLines = 1000;
	final TextFlow textFlow = new TextFlow();

	public ConsolePane()
	{
		setFitToWidth(true);
		setContent(textFlow);
		textFlow.needsLayoutProperty()
						.addListener((observable, oldValue, newValue) -> vvalueProperty().set(1.0d));
	}

	public void appendText(final String str)
	{
		Platform.runLater(() -> {
			controlSize();
			Text text = new Text(str);

			if (str.startsWith("[StdOut]"))
				text.setFill(Color.BLUE);
			else if (str.startsWith("[StdErr]"))
				text.setFill(Color.RED);

			textFlow.getChildren().add(text);
		});

	}

	private void controlSize()
	{
		final long lTimeNow = System.nanoTime();

		if (lTimeNow > mLastCheck + cCheckPeriod)
		{
			if (textFlow.getChildren().size() > maxLines)
			{
				textFlow.getChildren()
								.remove(0, textFlow.getChildren().size() - maxLines);
			}

			mLastCheck = lTimeNow;
		}
	}
}
