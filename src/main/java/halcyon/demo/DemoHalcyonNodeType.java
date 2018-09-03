package halcyon.demo;

import javafx.scene.Node;

import halcyon.model.node.HalcyonNodeType;

/**
 * Example of HalcyonNodeType
 */
public enum DemoHalcyonNodeType implements HalcyonNodeType
{
 ONE, TWO, THREE;

  @Override
  public Node getIcon()
  {
    return getIcon(DemoResourcesUtil.getString(name().toLowerCase()
                                               + ".icon"));
  }
}
