package halcyon.model.node;

import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingNode;
import javafx.scene.Node;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Halcyon Swing Node
 * 
 * TODO: this should in interface, rename HalcyonNodeBase TODO: add static
 * methdos to quickly wrap panels.
 */
public class HalcyonSwingNode implements HalcyonNodeInterface
{
	private String name;

	private HalcyonNodeType type;

	private JFrame mJFrame;

	private final List<HalcyonNodeListener> listeners = new ArrayList<HalcyonNodeListener>();

	private boolean mDockable;

	public HalcyonSwingNode(String name,
													HalcyonNodeType type,
													JFrame pJFrame,
													boolean pDockable)
	{
		this.name = name;
		this.type = type;
		mJFrame = pJFrame;
		setDockable(pDockable);
	}

	@Override
	public HalcyonNodeType getType()
	{
		return type;
	}

	@Override
	public Node getPanel()
	{
		if (mDockable)
		{
			SwingNode lSwingNode = new SwingNode();
			lSwingNode.setContent((JComponent) mJFrame.getContentPane()
																								.getComponent(0));
			return lSwingNode;
		}
		else
			return null;
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		return this.name == ((HalcyonSwingNode) obj).getName();
	}

	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}

	/**
	 * Adds an observer to this Halcyon node.
	 * 
	 * @param listener
	 *          the new observer
	 */
	public void addListener(HalcyonNodeListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * Removes an observer from this Halcyon node.
	 * 
	 * @param listener
	 *          the listener to remove
	 */
	public void removeListener(HalcyonNodeListener listener)
	{
		listeners.remove(listener);
	}

	public String getName()
	{
		return name;
	}

	public void setVisible(boolean pB)
	{
		SwingUtilities.invokeLater(() -> {
			mJFrame.setVisible(pB);
		});
	}

	public void close()
	{
		mJFrame.dispose();
	}

	public boolean isDockable()
	{
		return mDockable;
	}

	public void setDockable(boolean pDockable)
	{
		mDockable = pDockable;
	}
}
