package window.console;

import javafx.scene.Node;
import window.control.ControlWindowBase;

/**
 * Console Base class
 */
public abstract class ConsoleBase extends ControlWindowBase implements ConsoleInterface
{
	public ConsoleBase( Node n )
	{
		super( n );
	}
}
