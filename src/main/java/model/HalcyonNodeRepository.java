package model;

import java.util.ArrayList;
import java.util.List;

/**
 * A modifiable set of {@link model.HalcyonNode}s.
 */
public class HalcyonNodeRepository
{
	/** observers of this repository, will be informed whenever pictures are added or removed */
	private List<HalcyonNodeRepositoryListener> listeners = new ArrayList<HalcyonNodeRepositoryListener>();

	/** the pictures in this repository*/
	private List<HalcyonNode> nodes = new ArrayList<HalcyonNode>();

	/**
	 * Adds an observer to this repository. The observer will be informed whenever
	 * a picture is added or removed from this repository.
	 * @param listener the new observer
	 */
	public void addListener( HalcyonNodeRepositoryListener listener ){
		listeners.add( listener );
	}

	/**
	 * Removes an observer from this repository.
	 * @param listener the observer to remove
	 */
	public void removeListener( HalcyonNodeRepositoryListener listener ){
		listeners.remove( listener );
	}

	/**
	 * Adds a picture to the list of pictures.
	 * @param picture the new picture
	 */
	public void add( HalcyonNode picture ){
		nodes.add( picture );
		for( HalcyonNodeRepositoryListener listener : listeners.toArray( new HalcyonNodeRepositoryListener[ listeners.size() ] ) )
			listener.nodeAdded( picture );
	}

	/**
	 * Removes a picture from the list of pictures.
	 * @param picture the picture to remove
	 */
	public void remove( HalcyonNode picture ){
		if( nodes.remove( picture )){
			for( HalcyonNodeRepositoryListener listener : listeners.toArray( new HalcyonNodeRepositoryListener[ listeners.size() ] ) )
				listener.nodeRemoved( picture );
		}
	}

	/**
	 * Gets the number of pictures which are stored in this repository.
	 * @return the number of pictures
	 */
	public int getNodeCount(){
		return nodes.size();
	}

	/**
	 * Gets the index'th picture of this repository.
	 * @param index the location of the picture
	 * @return the picture
	 */
	public HalcyonNode getNode( int index ){
		return nodes.get( index );
	}

	/**
	 * Gets the first node with the {@link HalcyonNode#getName() name} <code>name</code>.
	 * @param name the name of the picture
	 * @return a picture or <code>null</code>
	 */
	public HalcyonNode getNode( String name ){
		for( HalcyonNode node : nodes )
			if( node.getName().equals( name ))
				return node;

		return null;
	}
}
