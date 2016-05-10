package halcyon.model.node;

import javafx.scene.Node;

/**
 * Halcyon 'Other' Node These nodes are not managed by Halcyon, we just provide
 * closures that specify how to show, hide and close the corresponding windows.
 * 
 */
public class HalcyonOtherNode extends HalcyonNodeBase	implements
																											HalcyonNodeInterface
{

	private Runnable mRunnableShow, mRunnableHide, mRunnableClose;

	public HalcyonOtherNode(String name,
													HalcyonNodeType type,
													Runnable pRunnableShow,
													Runnable pRunnableHide,
													Runnable pRunnableClose)
	{
		super(name, type);
		mRunnableShow = pRunnableShow;
		mRunnableHide = pRunnableHide;
		mRunnableClose = pRunnableClose;
	}

	@Override
	public Node getPanel()
	{
		throw new UnsupportedOperationException("Cannot request panel from an external node (external nodes are non-dockable)");
	}

	public void setVisible(boolean pVisible)
	{
		if (pVisible)
		{
			if (mRunnableShow != null)
				mRunnableShow.run();
		}
		else
		{
			if (mRunnableHide != null)
				mRunnableHide.run();
		}
	}

	public void close()
	{
		if (mRunnableClose != null)
			mRunnableClose.run();
	}

}
