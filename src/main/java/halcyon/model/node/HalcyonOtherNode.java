package halcyon.model.node;

import javafx.scene.Node;

/**
 * Halcyon 'Other' Node These nodes are not managed by Halcyon, we just provide
 * closures that specify how to show, hide and close the corresponding windows.
 */
public class HalcyonOtherNode extends HalcyonNodeBase	implements
																											HalcyonNodeInterface
{

	private Runnable mRunnableShow, mRunnableHide, mRunnableClose;

	/**
	 * Instantiates a new HalcyonOther node.
	 * @param name the name
	 * @param type the type
	 * @param pRunnableShow the runnable show delegate
	 * @param pRunnableHide the runnable hide delegate
	 * @param pRunnableClose the runnable close delegate
	 */
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

	/**
	 * Not necessary because it manages the panel by itself.
	 * @return the panel
	 */
	@Override
	public Node getPanel()
	{
		throw new UnsupportedOperationException("Cannot request panel from an external node (external nodes are non-dockable)");
	}

	/**
	 * Sets visible property.
	 * @param pVisible the visible flag
	 */
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

	/**
	 * Close.
	 */
	public void close()
	{
		if (mRunnableClose != null)
			mRunnableClose.run();
	}

}
