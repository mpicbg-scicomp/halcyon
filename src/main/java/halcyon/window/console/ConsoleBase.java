package halcyon.window.console;

import halcyon.window.control.ControlWindowBase;
import javafx.scene.Node;

/**
 * Console Base class
 */
public abstract class ConsoleBase extends ControlWindowBase	implements
																														ConsoleInterface
{
	public ConsoleBase(Node n)
	{
		super(n);
	}
}
