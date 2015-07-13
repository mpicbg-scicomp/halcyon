package model;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * Halcyon Node
 */
public class HalcyonNode
{
	public enum Type { Camera, Laser }

	public Type getType()
	{
		return type;
	}

	private Type type;
	private final StringProperty name = new SimpleStringProperty();

	public JPanel getPanel()
	{
		if(panel == null) return null;

		return panel.get();
	}

	public PanelProperty panelProperty()
	{
		return panel;
	}

	public void setPanel( JPanel panel )
	{
		if(this.panel == null)
			this.panel = new PanelProperty( null, "JPanel" );

		this.panel.set( panel );
	}

	private PanelProperty panel = null;

	private final ReadOnlyBooleanWrapper existPanel = new ReadOnlyBooleanWrapper();

	public HalcyonNode()
	{
		existPanel.bind( name.isNotEmpty().and( panel.isNotEmpty() ) );
	}

	/** the observers of this Halcyon node */
	private List<HalcyonNodeListener> listeners = new ArrayList<HalcyonNodeListener>();

	public int getIndex()
	{
		return index;
	}

	public void setIndex( int index )
	{
		this.index = index;
	}

	private int index;

	public HalcyonNode( String name )
	{
		this.name.setValue( name );
	}
	public HalcyonNode( String name, Type type )
	{
		this.name.setValue( name );
		this.type = type;
	}

	@Override
	public String toString() { return name.getValue().toString(); }



	/**
	 * Adds an observer to this Halcyon node.
	 * @param listener the new observer
	 */
	public void addListener( HalcyonNodeListener listener ){
		listeners.add( listener );
	}

	/**
	 * Removes an observer from this Halcyon node.
	 * @param listener the listener to remove
	 */
	public void removeListener( HalcyonNodeListener listener ){
		listeners.remove( listener );
	}


	public String getName()
	{
		return name.getValue();
	}
}
