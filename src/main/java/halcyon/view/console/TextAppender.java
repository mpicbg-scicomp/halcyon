package halcyon.view.console;

/**
 * TextAppender interface is used for standard output/error
 */
public interface TextAppender
{
  /**
   * Append text.
   * 
   * @param text
   *          the text
   */
  public void appendText(String text);
}