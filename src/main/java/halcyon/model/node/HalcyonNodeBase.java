package halcyon.model.node;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Halcyon Node Base class
 */
public abstract class HalcyonNodeBase implements HalcyonNodeInterface
{

  protected HalcyonNodeType type;
  protected final StringProperty name = new SimpleStringProperty();
  protected final List<HalcyonNodeListener> listeners =
                                                      new ArrayList<HalcyonNodeListener>();

  public HalcyonNodeBase()
  {
  }

  public HalcyonNodeBase(String name, HalcyonNodeType type)
  {
    this.name.setValue(name);
    this.type = type;
  }

  @Override
  public HalcyonNodeType getType()
  {
    return type;
  }

  @Override
  public String toString()
  {
    return name.getValue().toString();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null || getClass() != obj.getClass())
    {
      return false;
    }
    return this.name.getValue() == ((HalcyonNodeBase) obj).getName();
  }

  @Override
  public int hashCode()
  {
    return this.name.getValue().hashCode();
  }

  /**
   * Adds an observer to this Halcyon node.
   * 
   * @param listener
   *          the new observer
   */
  public void addListener(HalcyonNodeListener listener)
  {
    listeners.add(listener);
  }

  /**
   * Removes an observer from this Halcyon node.
   * 
   * @param listener
   *          the listener to remove
   */
  public void removeListener(HalcyonNodeListener listener)
  {
    listeners.remove(listener);
  }

  public String getName()
  {
    return name.getValue();
  }
}
