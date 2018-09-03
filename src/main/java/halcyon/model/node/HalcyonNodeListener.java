package halcyon.model.node;

/**
 * An observer of a {@link HalcyonNode}.
 */
public interface HalcyonNodeListener
{
  /**
   * Called when the observed Halcyon node has changed.
   */
  public void nodeChanged();
}
