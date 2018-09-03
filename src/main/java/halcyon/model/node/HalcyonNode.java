package halcyon.model.node;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.Node;

import halcyon.model.property.NodeProperty;

/**
 * Halcyon Node presents any kinds of element which contains GUI interfaces. By
 * default, it supports JavaFX Node {@link javafx.scene.Node}
 */
public class HalcyonNode extends HalcyonNodeBase
                         implements HalcyonNodeInterface
{

  private NodeProperty panel = null;
  private final ReadOnlyBooleanWrapper existPanel =
                                                  new ReadOnlyBooleanWrapper();

  /**
   * Create a HalcyonNode by wrapping the panel with the given name and type
   * 
   * @param name
   *          the name
   * @param type
   *          the type
   * @param panel
   *          the panel
   * @return the halcyon node
   */
  public static HalcyonNode wrap(final String name,
                                 final HalcyonNodeType type,
                                 final Node panel)
  {
    return new HalcyonNode(name, type, panel);
  }

  /**
   * Instantiates a new Halcyon node.
   */
  public HalcyonNode()
  {
    super();
    existPanel.bind(name.isNotEmpty().and(panel.isNotEmpty()));
  }

  /**
   * Instantiates a new Halcyon node.
   * 
   * @param name
   *          the name
   */
  public HalcyonNode(String name)
  {
    this.name.setValue(name);
  }

  /**
   * Instantiates a new Halcyon node.
   * 
   * @param name
   *          the name
   * @param type
   *          the type
   */
  public HalcyonNode(String name, HalcyonNodeType type)
  {
    this.name.setValue(name);
    this.type = type;
  }

  /**
   * Instantiates a new Halcyon node.
   * 
   * @param name
   *          the name
   * @param type
   *          the type
   * @param panel
   *          the panel
   */
  public HalcyonNode(String name, HalcyonNodeType type, Node panel)
  {
    this.name.setValue(name);
    this.type = type;
    this.setPanel(panel);
  }

  /**
   * Gets JavaFX Node panel {@link javafx.scene.Node}.
   * 
   * @return the panel
   */
  @Override
  public Node getPanel()
  {
    if (panel == null)
      return null;

    return panel.get();
  }

  /**
   * Gets Panel property {@link halcyon.model.property.NodeProperty}.
   * 
   * @return the node property
   */
  public NodeProperty panelProperty()
  {
    return panel;
  }

  /**
   * Sets panel.
   * 
   * @param panel
   *          the panel
   */
  public void setPanel(Node panel)
  {
    if (this.panel == null)
      this.panel = new NodeProperty(null, "Content");

    this.panel.set(panel);
  }
}
