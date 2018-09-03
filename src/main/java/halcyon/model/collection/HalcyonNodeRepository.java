package halcyon.model.collection;

import java.util.ArrayList;
import java.util.List;

import halcyon.model.node.HalcyonNodeInterface;

/**
 * A modifiable set of {@link halcyon.model.node.HalcyonNode}s.
 */
public class HalcyonNodeRepository
{
  /**
   * observers of this repository, will be informed whenever nodes are added or
   * removed
   */
  private List<HalcyonNodeRepositoryListener> listeners =
                                                        new ArrayList<>();

  /** the nodes in this repository */
  private List<HalcyonNodeInterface> nodes = new ArrayList<>();

  /**
   * Adds an observer to this repository. The observer will be informed whenever
   * a nodes is added or removed from this repository.
   * 
   * @param listener
   *          the new observer
   */
  public void addListener(HalcyonNodeRepositoryListener listener)
  {
    listeners.add(listener);
  }

  /**
   * Removes an observer from this repository.
   * 
   * @param listener
   *          the observer to remove
   */
  public void removeListener(HalcyonNodeRepositoryListener listener)
  {
    listeners.remove(listener);
  }

  /**
   * Adds a node to the list of Halcyon nodes.
   * 
   * @param node
   *          the new node
   */
  public void add(HalcyonNodeInterface node)
  {
    if (nodes.contains(node))
    {
      System.err.println(node.getName()
                         + " is not unique in the collection. Please, use different name");
      return;
    }
    nodes.add(node);
    for (HalcyonNodeRepositoryListener listener : listeners.toArray(new HalcyonNodeRepositoryListener[listeners.size()]))
      listener.nodeAdded(node);
  }

  /**
   * Removes a node from the list of Halcyon node.
   * 
   * @param node
   *          the node to remove
   */
  public void remove(HalcyonNodeInterface node)
  {
    if (nodes.remove(node))
    {
      for (HalcyonNodeRepositoryListener listener : listeners.toArray(new HalcyonNodeRepositoryListener[listeners.size()]))
        listener.nodeRemoved(node);
    }
  }

  /**
   * Gets the number of nodes which are stored in this repository.
   * 
   * @return the number of nodes
   */
  public int getNodeCount()
  {
    return nodes.size();
  }

  /**
   * Gets the index'th node of this repository.
   * 
   * @param index
   *          the location of the node
   * @return the node
   */
  public HalcyonNodeInterface getNode(int index)
  {
    return nodes.get(index);
  }

  /**
   * Gets the first node with the
   * {@link halcyon.model.node.HalcyonNode#getName() name} <code>name</code>.
   * 
   * @param name
   *          the name of the node
   * @return a node or <code>null</code>
   */
  public HalcyonNodeInterface getNode(String name)
  {
    for (HalcyonNodeInterface node : nodes)
      if (node.getName().equals(name))
        return node;

    return null;
  }
}
