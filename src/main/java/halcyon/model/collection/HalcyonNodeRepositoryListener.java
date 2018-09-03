package halcyon.model.collection;

import halcyon.model.node.HalcyonNodeInterface;

/**
 * Halcyon node repository listener
 */
public interface HalcyonNodeRepositoryListener
{
  /**
   * Invoked when a Halcyon node was added to the observed repository.
   * 
   * @param node
   *          the new Halcyon node
   */
  public void nodeAdded(HalcyonNodeInterface node);

  /**
   * Invoked when a Halcyon node was removed from the observed repository.
   * 
   * @param node
   *          the removed Halcyon node
   */
  public void nodeRemoved(HalcyonNodeInterface node);
}
