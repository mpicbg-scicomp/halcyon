package model.property;

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
 * PanelProperty for holding node
 */
public class NodeProperty	implements
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

	public NodeProperty(Object bean, String name)
	{
		this.bean = bean;
		this.name = (name == null) ? DEFAULT_NAME : name;
	}

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

	@Override
	public boolean isBound()
	{
		return observable != null;
	}

	@Override
	public void bindBidirectional(Property<Node> other)
	{
		Bindings.bindBidirectional(this, other);
	}

	@Override
	public void unbindBidirectional(Property<Node> other)
	{
		Bindings.unbindBidirectional(this, other);
	}

	@Override
	public Object getBean()
	{
		return bean;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void addListener(ChangeListener<? super Node> listener)
	{
		helper = ExpressionHelper.addListener(helper, this, listener);
	}

	@Override
	public void removeListener(ChangeListener<? super Node> listener)
	{
		helper = ExpressionHelper.removeListener(helper, listener);
	}

	@Override
	public Node getValue()
	{
		return get();
	}

	@Override
	public void setValue(Node newValue)
	{
		set(newValue);
	}

	@Override
	public void addListener(InvalidationListener listener)
	{
		helper = ExpressionHelper.addListener(helper, this, listener);
	}

	@Override
	public void removeListener(InvalidationListener listener)
	{
		helper = ExpressionHelper.removeListener(helper, listener);
	}

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

	protected void invalidated()
	{

	}

	@Override
	public Node get()
	{
		valid = true;
		return observable == null ? node : observable.getValue();
	}

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

		public Listener(NodeProperty ref)
		{
			this.wref = new WeakReference<>(ref);
		}

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
