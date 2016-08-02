package halcyon.view;

import halcyon.view.console.TextAppender;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

/**
 * ConsolePane for Standard Output and Standard Error messages.
 */
public class ConsolePanel extends ScrollPane implements TextAppender
{
	private static final long cCheckPeriod = 1_000_000_000;
	private volatile long mLastCheck = System.nanoTime();

	/**
	 * The Max lines.
	 */
	// It remains more than 100_000 characters at some point
	final int maxCharacters = 100_000;
	/**
	 * The text area.
	 */
	final TextArea mTextArea = new TextArea();

	/**
	 * Instantiates a new Console pane.
	 */
	public ConsolePanel()
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

	/**
	 * Clear all the text in Console.
	 */
	public void clearText()
	{
		Platform.runLater(() -> {
			mTextArea.clear();
		});
	}

	private void controlSize()
	{
		final long lTimeNow = System.nanoTime();

		if (lTimeNow > mLastCheck + cCheckPeriod)
		{
			if (mTextArea.lengthProperty().get() > maxCharacters)
			{
				mTextArea.deleteText(	0,
															mTextArea.lengthProperty().get() - maxCharacters);
			}

			mLastCheck = lTimeNow;
		}
	}
}
