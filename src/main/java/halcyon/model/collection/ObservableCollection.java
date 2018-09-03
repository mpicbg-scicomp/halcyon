package halcyon.model.collection;

import java.util.ArrayList;

/**
 * ObservableCollection
 */
public class ObservableCollection<T>
{
  /**
   * observers of this repository, will be informed whenever items are added or
   * removed
   */
  private ArrayList<ObservableCollectionListener<T>> listeners =
                                                               new ArrayList<>();

  /** the item collection in this repository */
  private ArrayList<T> collection = new ArrayList<>();

  /**
   * Adds an observer to this repository. The observer will be informed whenever
   * a consoles is added or removed from this collection.
   * 
   * @param listener
   *          the new observer
   */
  public void addListener(ObservableCollectionListener<T> listener)
  {
    listeners.add(listener);
  }

  /**
   * Removes an observer from this collection.
   * 
   * @param listener
   *          the observer to remove
   */
  public void removeListener(ObservableCollectionListener<T> listener)
  {
    listeners.remove(listener);
  }

  /**
   * Adds an item to the list of items.
   * 
   * @param item
   *          the new item
   */
  public void add(T item)
  {
    collection.add(item);
    for (ObservableCollectionListener listener : listeners.toArray(new ObservableCollectionListener[listeners.size()]))
      listener.itemAdded(item);
  }

  /**
   * Removes an item from the list of item.
   * 
   * @param item
   *          the new item
   */
  public void remove(T item)
  {
    if (collection.remove(item))
    {
      for (ObservableCollectionListener listener : listeners.toArray(new ObservableCollectionListener[listeners.size()]))
        listener.itemRemoved(item);
    }
  }

  /**
   * Gets the number of items which are stored in this collection.
   * 
   * @return the number of items
   */
  public int getCount()
  {
    return collection.size();
  }

  /**
   * Gets the index'th item of this collection.
   * 
   * @param index
   *          the location of the item
   * @return the item
   */
  public T get(int index)
  {
    return collection.get(index);
  }

  /**
   * Gets the first item with the {@link Object#toString() name}
   * <code>name</code>.
   * 
   * @param name
   *          the name of the item
   * @return the item or <code>null</code>
   */
  public T get(String name)
  {
    for (T item : collection)
      if (item.toString().equals(name))
        return item;

    return null;
  }

  public ArrayList<T> getList()
  {
    return collection;
  }
}
