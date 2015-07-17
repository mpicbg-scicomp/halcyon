package window.control;

import bibliothek.gui.dock.common.DefaultSingleCDockable;

/**
 * Basic Control type Window for tools, which will be overridden by inherited
 * class
 */
public abstract class ControlWindowBase extends DefaultSingleCDockable implements ControlWindowInterface
{
	public ControlWindowBase( String name )
	{
		super( name );
	}

	@Override public String toString()
	{
		return this.getUniqueId();
	}
}
