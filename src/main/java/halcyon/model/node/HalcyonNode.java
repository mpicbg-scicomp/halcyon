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
 * methods to quickly wrap panels.
 */
public class HalcyonNode extends HalcyonNodeBase implements HalcyonNodeInterface
{

	private NodeProperty panel = null;
	private final ReadOnlyBooleanWrapper existPanel = new ReadOnlyBooleanWrapper();
	
	// TODO: finish.
	public static HalcyonNode wrap(	final String name,
																	final HalcyonNodeType type,
																	final Node panel)
	{
		return new HalcyonNode(name, type, panel);
	}

	public HalcyonNode()
	{
		super();
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

	
}
