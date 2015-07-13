package window;

import bibliothek.gui.dock.common.DefaultSingleCDockable;

/**
 * Basic Control type Window for tools, which will be overridden by inherited class
 */
public abstract class ControlType extends DefaultSingleCDockable
{
	public ControlType(String name)
	{
		super( name );
	}
}
