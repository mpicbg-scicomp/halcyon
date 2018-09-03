package halcyon.model.property;

import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;
import javafx.scene.Node;

import com.sun.javafx.binding.ExpressionHelper;

/**
 * NodeProperty for holding JavaFX {@link javafx.scene.Node}.
 */
public class NodeProperty implements
                          ObservableObjectValue<Node>,
                          WritableObjectValue<Node>,
                          Property<Node>
{
  private static final Object DEFAULT_BEAN = null;
  private static final String DEFAULT_NAME = "";

  private Node node;
  private final Object bean;
  private final String name;

  private ObservableValue<? extends Node> observable = null;
  private InvalidationListener listener = null;
  private boolean valid = true;
  private ExpressionHelper<Node> helper = null;

  /**
   * Instantiates a new Node property.
   * 
   * @param bean
   *          the bean
   * @param name
   *          the name
   */
  public NodeProperty(Object bean, String name)
  {
    this.bean = bean;
    this.name = (name == null) ? DEFAULT_NAME : name;
  }

  /**
   * Bind.
   * 
   * @param newObservable
   *          the new observable
   */
  @Override
  public void bind(ObservableValue<? extends Node> newObservable)
  {
    if (newObservable == null)
    {
      throw new NullPointerException("Cannot bind to null");
    }
    if (!newObservable.equals(observable))
    {
      unbind();
      observable = newObservable;
      if (listener == null)
      {
        listener = new Listener(this);
      }
      observable.addListener(listener);
      markInvalid();
    }
  }

  /**
   * Unbind.
   */
  @Override
  public void unbind()
  {
    if (observable != null)
    {
      node = observable.getValue();
      observable.removeListener(listener);
      observable = null;
    }
  }

  /**
   * Is bound boolean.
   * 
   * @return the boolean
   */
  @Override
  public boolean isBound()
  {
    return observable != null;
  }

  /**
   * Bind bidirectional.
   * 
   * @param other
   *          the other
   */
  @Override
  public void bindBidirectional(Property<Node> other)
  {
    Bindings.bindBidirectional(this, other);
  }

  /**
   * Unbind bidirectional.
   * 
   * @param other
   *          the other
   */
  @Override
  public void unbindBidirectional(Property<Node> other)
  {
    Bindings.unbindBidirectional(this, other);
  }

  /**
   * Gets bean.
   * 
   * @return the bean
   */
  @Override
  public Object getBean()
  {
    return bean;
  }

  /**
   * Gets name.
   * 
   * @return the name
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * Add listener.
   * 
   * @param listener
   *          the listener
   */
  @Override
  public void addListener(ChangeListener<? super Node> listener)
  {
    helper = ExpressionHelper.addListener(helper, this, listener);
  }

  /**
   * Remove listener.
   * 
   * @param listener
   *          the listener
   */
  @Override
  public void removeListener(ChangeListener<? super Node> listener)
  {
    helper = ExpressionHelper.removeListener(helper, listener);
  }

  /**
   * Gets value.
   * 
   * @return the value
   */
  @Override
  public Node getValue()
  {
    return get();
  }

  /**
   * Sets value.
   * 
   * @param newValue
   *          the new value
   */
  @Override
  public void setValue(Node newValue)
  {
    set(newValue);
  }

  /**
   * Add listener.
   * 
   * @param listener
   *          the listener
   */
  @Override
  public void addListener(InvalidationListener listener)
  {
    helper = ExpressionHelper.addListener(helper, this, listener);
  }

  /**
   * Remove listener.
   * 
   * @param listener
   *          the listener
   */
  @Override
  public void removeListener(InvalidationListener listener)
  {
    helper = ExpressionHelper.removeListener(helper, listener);
  }

  /**
   * To string string.
   * 
   * @return the string
   */
  @Override
  public String toString()
  {
    final Object bean = getBean();
    final String name = getName();
    final StringBuilder result = new StringBuilder("NodeProperty [");
    if (bean != null)
    {
      result.append("bean: ").append(bean).append(", ");
    }
    if ((name != null) && (!name.equals("")))
    {
      result.append("name: ").append(name).append(", ");
    }
    result.append("value: ").append(get()).append("]");
    return result.toString();
  }

  /**
   * Fire value changed event.
   */
  protected void fireValueChangedEvent()
  {
    ExpressionHelper.fireValueChangedEvent(helper);
  }

  private void markInvalid()
  {
    if (valid)
    {
      valid = false;
      invalidated();
      fireValueChangedEvent();
    }
  }

  /**
   * Invalidated.
   */
  protected void invalidated()
  {

  }

  /**
   * Get node.
   * 
   * @return the node
   */
  @Override
  public Node get()
  {
    valid = true;
    return observable == null ? node : observable.getValue();
  }

  /**
   * Set.
   * 
   * @param newValue
   *          the new value
   */
  @Override
  public void set(Node newValue)
  {
    if (isBound())
    {
      throw new java.lang.RuntimeException("A bound value cannot be set.");
    }
    if ((node == null) ? newValue != null : !node.equals(newValue))
    {
      node = newValue;
      markInvalid();
    }
  }

  /**
   * Is not empty boolean binding.
   * 
   * @return the boolean binding
   */
  public BooleanBinding isNotEmpty()
  {
    NodeProperty currentPanel = this;

    return new BooleanBinding()
    {
      @Override
      protected boolean computeValue()
      {
        return currentPanel.valid;
      }
    };
  }

  private static class Listener implements InvalidationListener
  {

    private final WeakReference<NodeProperty> wref;

    /**
     * Instantiates a new Listener.
     * 
     * @param ref
     *          the ref
     */
    public Listener(NodeProperty ref)
    {
      this.wref = new WeakReference<>(ref);
    }

    /**
     * Invalidated.
     * 
     * @param observable
     *          the observable
     */
    @Override
    public void invalidated(Observable observable)
    {
      NodeProperty ref = wref.get();
      if (ref == null)
      {
        observable.removeListener(this);
      }
      else
      {
        ref.markInvalid();
      }
    }
  }
}
