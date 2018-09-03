package halcyon.view;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import halcyon.view.console.TextAppender;

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

  private final ScheduledExecutorService mScheduler =
                                                    Executors.newScheduledThreadPool(1);

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

    Runnable lRunnable = () -> {
      try
      {
        update();
      }
      catch (Throwable e)
      {
        e.printStackTrace();
      }
    };

    mScheduler.scheduleAtFixedRate(lRunnable,
                                   0,
                                   500,
                                   TimeUnit.MILLISECONDS);
  }

  /**
   * Appends text.
   * 
   * @param pStringToAppend
   *          the string to append
   */
  public void appendText(final String pStringToAppend)
  {
    mReentrantLock.lock();
    try
    {
      mStringBuilder.append(pStringToAppend);
    }
    finally
    {
      mReentrantLock.unlock();
    }

    if (mLastUpdate
        + cUpdatePeriodInMilliseconds < System.currentTimeMillis())
    {
      update();
    }
  }

  protected void update()
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

            int lLength = mTextArea.lengthProperty().get();
            if (lLength > cMaxCharactersInTextArea)
            {
              mTextArea.deleteText(0,
                                   lLength
                                      - cMaxCharactersInTextArea);
              mTextArea.selectPositionCaret(lLength - 1);
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
