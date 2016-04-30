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
public class HalcyonSwingNode extends HalcyonNodeBase	implements
																											HalcyonNodeInterface
{

	private JFrame mJFrame;

	private boolean mDockable;

	private JComponent mJComponent;

	//TODO: we should only create HalcyonSwingNode from JComponents, get rid of this constructor
	public HalcyonSwingNode(String name,
													HalcyonNodeType type,
													JFrame pJFrame,
													boolean pDockable)
	{
		super(name, type);
		mJFrame = pJFrame;
		setDockable(pDockable);
	}

	public HalcyonSwingNode(String name,
													HalcyonNodeType type,
													JComponent pJComponent)
	{
		super(name, type);
		mJComponent = pJComponent;
		mJFrame = null;
		setDockable(true);
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
			if (mJFrame != null)
			{
				lSwingNode.setContent((JComponent) mJFrame.getContentPane()
																									.getComponent(0));
			}
			else if (mJComponent != null)
			{
				lSwingNode.setContent(mJComponent);
			}
			return lSwingNode;
		}
		else
			return null;
	}

	public void setVisible(boolean pB)
	{
		SwingUtilities.invokeLater(() -> {
			if (mJFrame != null)
				mJFrame.setVisible(pB);
		});
	}

	public void close()
	{
		if (mJFrame != null)
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
