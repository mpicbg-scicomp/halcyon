package halcyon.view.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import halcyon.view.ConsolePane;

import org.dockfx.DockNode;

/**
 * Standard Output and Error capture console
 */
public class StdOutputCaptureConsole extends DockNode
{
  private final ConsolePane consolePane;

  /**
   * Instantiates a new Standard output/error capture console.
   */
  public StdOutputCaptureConsole()
  {
    super(new ConsolePane());
    setTitle("Console");

    consolePane = (ConsolePane) getContents();

    System.setOut(new PrintStream(new StreamAppender(" ",
                                                     consolePane.getConsolePanel(),
                                                     System.out)));
    System.setErr(new PrintStream(new StreamAppender("!",
                                                     consolePane.getConsolePanel(),
                                                     System.err)));
  }

  /**
   * The Stream appender for output stream.
   */
  public class StreamAppender extends OutputStream
  {
    private StringBuilder buffer;
    private String prefix;
    private TextAppender textAppender;
    private PrintStream old;

    /**
     * Instantiates a new Stream appender.
     * 
     * @param prefix
     *          the prefix
     * @param consumer
     *          the consumer
     * @param old
     *          the old
     */
    public StreamAppender(String prefix,
                          TextAppender consumer,
                          PrintStream old)
    {
      this.prefix = prefix;
      buffer = new StringBuilder(128);
      buffer.append(prefix);
      this.old = old;
      this.textAppender = consumer;
    }

    /**
     * Write b.
     * 
     * @param b
     *          the b
     * @throws IOException
     *           the io exception
     */
    @Override
    public void write(int b) throws IOException
    {
      char c = (char) b;
      String value = Character.toString(c);
      buffer.append(value);
      if (value.equals("\n"))
      {
        textAppender.appendText(buffer.toString());
        buffer.delete(0, buffer.length());
        buffer.append(prefix);
      }
      old.print(c);
    }
  }
}
