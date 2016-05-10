package halcyon.window.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.dockfx.DockNode;

/**
 * Standard Output and Error capture console
 */
public class StdOutputCaptureConsole extends DockNode
{
	private final ConsolePane consolePane;

	public StdOutputCaptureConsole()
	{
		super(new ConsolePane());
		setTitle("Console");

		consolePane = (ConsolePane) getContents();

		System.setOut(new PrintStream(new StreamAppender(	"StdOut",
																											consolePane,
																											System.out)));
		System.setErr(new PrintStream(new StreamAppender(	"StdErr",
																											consolePane,
																											System.err)));
	}

	public class StreamAppender extends OutputStream
	{
		private StringBuilder buffer;
		private String prefix;
		private TextAppender textAppender;
		private PrintStream old;

		public StreamAppender(String prefix,
													TextAppender consumer,
													PrintStream old)
		{
			this.prefix = prefix;
			buffer = new StringBuilder(128);
			buffer.append("[").append(prefix).append("] ");
			this.old = old;
			this.textAppender = consumer;
		}

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
				buffer.append("[").append(prefix).append("] ");
			}
			old.print(c);
		}
	}
}
