package halcyon.view;

import java.util.concurrent.locks.ReentrantLock;

import halcyon.view.console.TextAppender;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

/**
 * ConsolePane for Standard Output and Standard Error messages.
 */
public class ConsolePanel extends ScrollPane implements TextAppender
{
	private static final int cMaxCharactersInTextArea = 100_000;
	private static final long cUpdatePeriodInMilliseconds = 200;

	private ReentrantLock mReentrantLock = new ReentrantLock();
	private StringBuilder mStringBuilder;

	private long mLastUpdate = System.currentTimeMillis();

	/**
	 * The Max lines.
	 */
	// It remains more than 100_000 characters at some point

	/**
	 * The text area.
	 */
	final TextArea mTextArea = new TextArea();

	/**
	 * Instantiates a new Console pane.
	 */
	public ConsolePanel()
	{
		setFitToWidth(true);
		setFitToHeight(true);
		setContent(mTextArea);

		mTextArea.setEditable(false);



		mStringBuilder = new StringBuilder();
		mStringBuilder.ensureCapacity(cMaxCharactersInTextArea);

	}

	/**
	 * Append text.
	 * 
	 * @param pString
	 *          the p string
	 */
	public void appendText(final String pString)
	{
		mReentrantLock.lock();
		try
		{
			mStringBuilder.append(pString);
		}
		finally
		{
			mReentrantLock.unlock();
		}

		if (mLastUpdate + cUpdatePeriodInMilliseconds < System.currentTimeMillis())
		{
			mLastUpdate = System.currentTimeMillis();

			Platform.runLater(() -> {

				if (mReentrantLock.tryLock())
				{
					try
					{
						if (mStringBuilder.length() > 0)
						{
							String lString = mStringBuilder.toString();
							mTextArea.appendText(lString);
							mStringBuilder.setLength(0);

							int lLength = mTextArea.lengthProperty()
												.get();
							if (lLength > cMaxCharactersInTextArea)
							{
								mTextArea.deleteText(	0,
																			lLength - cMaxCharactersInTextArea);
								mTextArea.selectPositionCaret(lLength-1);
								mTextArea.deselect();
							}
						}
					}
					finally
					{
						mReentrantLock.unlock();
					}
				}
			});
		}
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

}
