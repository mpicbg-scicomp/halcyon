package halcyon.model.node;

import halcyon.model.property.NodeProperty;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingNode;
import javafx.scene.Node;

import javax.swing.JComponent;

/**
 * Halcyon Node
 * 
 * TODO: this should in interface, rename HalcyonNodeBase TODO: add static
 * methdos to quickly wrap panels.
 */
public class HalcyonNode implements HalcyonNodeInterface
{

	private HalcyonNodeType type;
	private NodeProperty panel = null;
	private final ReadOnlyBooleanWrapper existPanel = new ReadOnlyBooleanWrapper();
	private final StringProperty name = new SimpleStringProperty();
	private final List<HalcyonNodeListener> listeners = new ArrayList<HalcyonNodeListener>();

	// TODO: finish.
	public static HalcyonNode wrap(	final String name,
																	final HalcyonNodeType type,
																	final Node panel)
	{
		return new HalcyonNode(name, type, panel);
	}

	public HalcyonNode()
	{
		existPanel.bind(name.isNotEmpty().and(panel.isNotEmpty()));
	}

	public HalcyonNode(String name)
	{
		this.name.setValue(name);
	}

	public HalcyonNode(String name, HalcyonNodeType type)
	{
		this.name.setValue(name);
		this.type = type;
	}

	public HalcyonNode(String name, HalcyonNodeType type, Node panel)
	{
		this.name.setValue(name);
		this.type = type;
		this.setPanel(panel);
	}

	public HalcyonNode(	String name,
											HalcyonNodeType type,
											JComponent panel)
	{
		this.name.setValue(name);
		this.type = type;
		final SwingNode lSwingNode = new SwingNode();
		lSwingNode.setContent(panel);
		this.setPanel(lSwingNode);
	}

	@Override
	public HalcyonNodeType getType()
	{
		return type;
	}

	@Override
	public Node getPanel()
	{
		if (panel == null)
			return null;

		return panel.get();
	}

	public NodeProperty panelProperty()
	{
		return panel;
	}

	public void setPanel(Node panel)
	{
		if (this.panel == null)
			this.panel = new NodeProperty(null, "Content");

		this.panel.set(panel);
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
		return this.name.getValue() == ((HalcyonNode) obj).getName();
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
