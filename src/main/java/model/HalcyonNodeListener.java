package model;

/**
 * An observer of a {@link model.HalcyonNode}.
 */
public interface HalcyonNodeListener
{
	/**
	 * Called when the observed Halcyon node has changed.
	 */
	public void nodeChanged();
}
