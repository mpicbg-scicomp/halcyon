package halcyon.model.collection;

/**
 * ObservableCollectionListener
 */
public interface ObservableCollectionListener<T>
{
  /**
   * Invoked when an item was added to the observed collection.
   * 
   * @param item
   *          the new item
   */
  public void itemAdded(T item);

  /**
   * Invoked when an item was removed from the observed collection.
   * 
   * @param item
   *          the removed item
   */
  public void itemRemoved(T item);
}
