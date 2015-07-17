package window.control;

import javax.swing.Icon;

/**
 * Device Node class
 */
public class DeviceNode
{
	private String name;

	private Icon icon;

	public DeviceNode( String name, Icon icon )
	{
		this.name = name;
		this.icon = icon;
	}

	public String getName()
	{
		return name;
	}

	public Icon getIcon()
	{
		return icon;
	}
}