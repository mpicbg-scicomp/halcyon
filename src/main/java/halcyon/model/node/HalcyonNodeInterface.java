package halcyon.model.node;

import javafx.scene.Node;

/**
 * Halcyon Node Interface
 */
public interface HalcyonNodeInterface
{
  public HalcyonNodeType getType();

  public String getName();

  /**
   * Adds an observer to this Halcyon node.
   * 
   * @param listener
   *          the new observer
   */
  public void addListener(HalcyonNodeListener listener);

  /**
   * Removes an observer from this Halcyon node.
   * 
   * @param listener
   *          the listener to remove
   */
  public void removeListener(HalcyonNodeListener listener);

  /**
   * Gets JavaFX panel.
   *
   * @return the JavaFX panel
   */
  public Node getPanel();
}
