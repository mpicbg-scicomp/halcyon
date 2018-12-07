package halcyon.model.node;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import javafx.embed.swing.SwingNode;
import javafx.scene.Node;

/**
 * Halcyon Swing Node
 */
public class HalcyonSwingNode extends HalcyonNodeBase	implements
		HalcyonNodeInterface
{
	private boolean mDockable;

	private JComponent mJComponent;

	/**
	 * Instantiates a new Halcyon swing node.
	 * @param name the HalcyonNode name
	 * @param type the HalcyonNode type
	 * @param pJComponent the JComponent
	 */
	public HalcyonSwingNode(String name,
			HalcyonNodeType type,
			JComponent pJComponent)
	{
		super(name, type);
		mJComponent = pJComponent;
		setDockable(true);
	}

	/**
	 * Gets HalcyonNode type.
	 * @return the type
	 */
	@Override
	public HalcyonNodeType getType()
	{
		return type;
	}

	/**
	 * Gets SwingNode by wrapping the swing component.
	 * @return the panel
	 */
	@Override
	public Node getPanel()
	{
		if (mDockable)
		{
			SwingNode lSwingNode = new SwingNode();

			if (mJComponent != null)
			{
				lSwingNode.setContent(mJComponent);
			}

			return lSwingNode;
		}
		else
			return null;
	}

	/**
	 * Sets visible.
	 * @param pB the visible flag
	 */
	public void setVisible(boolean pB)
	{
		SwingUtilities.invokeLater(() -> {
			if (mJComponent != null)
				mJComponent.setVisible(pB);
		});
	}

	/**
	 * Close.
	 */
	public void close()
	{
		if (mJComponent != null)
			mJComponent = null;

	}

	/**
	 * Is dockable boolean.
	 * @return the boolean
	 */
	public boolean isDockable()
	{
		return mDockable;
	}

	/**
	 * Sets dockable.
	 * @param pDockable the p dockable
	 */
	public void setDockable(boolean pDockable)
	{
		mDockable = pDockable;
	}
}
