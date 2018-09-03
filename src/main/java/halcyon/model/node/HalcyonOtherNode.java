package halcyon.model.node;

import javafx.scene.Node;

/**
 * Halcyon 'Other' Node These nodes are not managed by Halcyon, we just provide
 * closures that specify how to show, hide and close the corresponding windows.
 */
public class HalcyonOtherNode extends HalcyonNodeBase
                              implements HalcyonNodeInterface
{
  private final Window mWindow;

  /**
   * Instantiates a new HalcyonOther node.
   * 
   * @param name
   *          the name
   * @param type
   *          the type
   * @param pWindow
   *          the window contains window-related properties and functions
   */
  public HalcyonOtherNode(String name,
                          HalcyonNodeType type,
                          Window pWindow)
  {
    super(name, type);
    mWindow = pWindow;
  }

  /**
   * Not necessary because it manages the panel by itself.
   * 
   * @return the panel
   */
  @Override
  public Node getPanel()
  {
    throw new UnsupportedOperationException("Cannot request panel from an external node (external nodes are non-dockable)");
  }

  /**
   * Sets visible property.
   * 
   * @param pVisible
   *          the visible flag
   */
  public void setVisible(boolean pVisible)
  {
    if (null != mWindow)
    {
      if (pVisible)
        mWindow.show();
      else
        mWindow.hide();
    }
  }

  /**
   * Close.
   */
  public void close()
  {
    if (null != mWindow)
      mWindow.close();
  }

  /**
   * Get the window size as a two dimensional integer array.
   * 
   * @return the two dimensional integer array
   */
  public Integer[] getSize()
  {
    return new Integer[]
    { mWindow.getWidth(), mWindow.getHeight() };
  }

  /**
   * Get the window position as a two dimensional integer array.
   * 
   * @return the two dimensional integer array
   */
  public Integer[] getPosition()
  {
    return new Integer[]
    { mWindow.getX(), mWindow.getY() };
  }

  /**
   * Sets the window size with specific two dimensional array
   * 
   * @param size
   *          the size of the window
   */
  public void setSize(Integer[] size)
  {
    mWindow.setSize(size[0], size[1]);
  }

  /**
   * Sets the window position with specific two dimensional array.
   * 
   * @param position
   *          the position of the window
   */
  public void setPosition(Integer[] position)
  {
    mWindow.setPosition(position[0], position[1]);
  }
}
