package halcyon.window.console;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

/**
 * ConsolePane for Standard Out and Standard Error messages.
 */
public class ConsolePane extends ScrollPane implements TextAppender
{
	private static final long cCheckPeriod = 1_000_000_000;
	private volatile long mLastCheck = System.nanoTime();

	// It remains more than 1000 lines at some point
	final int maxLines = 1000;
	final TextArea mTextArea = new TextArea();

	public ConsolePane()
	{
		setFitToWidth(true);
		setContent(mTextArea);
		mTextArea.needsLayoutProperty()
							.addListener((observable, oldValue, newValue) -> vvalueProperty().set(1.0d));
	}

	public void appendText(final String pString)
	{
		Platform.runLater(() -> {
			controlSize();

			mTextArea.appendText(pString);
		});

	}

	private void controlSize()
	{
		final long lTimeNow = System.nanoTime();

		if (lTimeNow > mLastCheck + cCheckPeriod)
		{
			if (mTextArea.lengthProperty().get() > maxLines)
			{
				mTextArea.deleteText(	0,
															mTextArea.lengthProperty().get() - maxLines);
			}

			mLastCheck = lTimeNow;
		}
	}
}
