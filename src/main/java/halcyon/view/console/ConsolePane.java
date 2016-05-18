package halcyon.view.console;

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

	/**
	 * The Max lines.
	 */
	// It remains more than 1000 lines at some point
	final int maxLines = 1000;
	/**
	 * The text area.
	 */
	final TextArea mTextArea = new TextArea();

	/**
	 * Instantiates a new Console pane.
	 */
	public ConsolePane()
	{
		setFitToWidth( true );
		setFitToHeight( true );
		setContent(mTextArea);
	}

	/**
	 * Append text.
	 * @param pString the p string
	 */
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
