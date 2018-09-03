package halcyon.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import halcyon.model.node.HalcyonNodeInterface;
import halcyon.model.node.HalcyonNodeListener;

import org.dockfx.DockNode;
import org.dockfx.demo.DockFX;

/**
 * HalcyonPanel encapsulates HalcyonNode
 */
public class HalcyonPanel extends DockNode
{
  /** the current node */
  private HalcyonNodeInterface node;

  private HalcyonNodeListener listener = () -> {

  };

  /**
   * Instantiates a new Halcyon panel.
   * 
   * @param node
   *          the node
   */
  public HalcyonPanel(HalcyonNodeInterface node)
  {
    super(node.getPanel(),
          node.getName(),
          new ImageView(new Image(DockFX.class.getResource("docknode.png")
                                              .toExternalForm())));

    // getDockTitleBar().setVisible( false );

    if (isVisible() && getNode() != null)
      getNode().removeListener(listener);

    this.node = node;

    this.setTitle(node == null ? "" : node.getName());

    if (isVisible() && node != null)
      node.addListener(listener);
  }

  /**
   * Sets Halcyon node explicitly.
   * 
   * @param node
   *          the node
   */
  public void setNode(HalcyonNodeInterface node)
  {
    if (isVisible() && getNode() != null)
      getNode().removeListener(listener);

    this.node = node;

    this.setContents(node.getPanel());

    this.setTitle(node == null ? "" : node.getName());

    if (isVisible() && node != null)
      node.addListener(listener);
  }

  /**
   * Gets HalcyonNode.
   * 
   * @return the HalcyonNode
   */
  public HalcyonNodeInterface getNode()
  {
    return node;
  }
}
