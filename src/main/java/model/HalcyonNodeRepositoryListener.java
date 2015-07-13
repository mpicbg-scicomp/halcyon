package model;

/**
 * Halcyon node repository listener
 */
public interface HalcyonNodeRepositoryListener
{
	/**
	 * Invoked when a Halcyon node was added to the observed repository.
	 * @param node the new Halcyon node
	 */
	public void nodeAdded( HalcyonNode node );

	/**
	 * Invoked when a Halcyon node was removed from the observed repository.
	 * @param node the removed Halcyon node
	 */
	public void nodeRemoved( HalcyonNode node );
}
