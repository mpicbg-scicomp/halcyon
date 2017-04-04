package halcyon.model.node;

import halcyon.model.node.HalcyonNodeBase;
import halcyon.model.node.HalcyonNodeInterface;
import halcyon.model.node.HalcyonNodeType;
import halcyon.model.node.Window;
import halcyon.model.property.NodeProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.Node;

/**
 * Created by dibrov on 15/03/17.
 */
public class HalcyonOtherNodeWithPanel extends HalcyonOtherNode implements HalcyonNodeInterface {

//        private final Window mWindow;
        private NodeProperty panel = null;
        private final ReadOnlyBooleanWrapper existPanel = new ReadOnlyBooleanWrapper();

        /**
         * Instantiates a new HalcyonOther node.
         * @param name the name
         * @param type the type
         * @param pWindow the window contains window-related properties and functions
         */
	public HalcyonOtherNodeWithPanel(String name,
            HalcyonNodeType type,
            Window pWindow, Node pPanel)
        {
            super(name, type, pWindow);
//            mWindow = pWindow;
            this.setPanel(pPanel);
        }

        /**
         * Not necessary because it manages the panel by itself.
         * @return the panel
         */
        @Override
        public Node getPanel()
        {
//            throw new UnsupportedOperationException("Cannot request panel from an external node (external nodes are non-dockable)");
            if (panel == null)
                return null;

            return panel.get();
        }

        /**
         * Sets visible property.
         * @param pVisible the visible flag
         */
//    public void setVisible(boolean pVisible)
//    {
//        if(null != mWindow)
//        {
//            if (pVisible)
//                mWindow.show();
//            else
//                mWindow.hide();
//        }
//    }
//
//    /**
//     * Close.
//     */
//    public void close()
//    {
//        if(null != mWindow)
//            mWindow.close();
//    }
//
    public void setPanel(Node panel)
    {
        if (this.panel == null)
            this.panel = new NodeProperty(null, "Content");

        this.panel.set(panel);
    }
//
//    /**
//     * Get the window size as a two dimensional integer array.
//     * @return the two dimensional integer array
//     */
//    public Integer[] getSize()
//    {
//        return new Integer[]{ mWindow.getWidth(), mWindow.getHeight() };
//    }
//
//    /**
//     * Get the window position as a two dimensional integer array.
//     * @return the two dimensional integer array
//     */
//    public Integer[] getPosition()
//    {
//        return new Integer[]{ mWindow.getX(), mWindow.getY() };
//    }
//
//    /**
//     * Sets the window size with specific two dimensional array
//     * @param size the size of the window
//     */
//    public void setSize(Integer[] size)
//    {
//        mWindow.setSize( size[0], size[1] );
//    }
//
//    /**
//     * Sets the window position with specific two dimensional array.
//     * @param position the position of the window
//     */
//    public void setPosition(Integer[] position)
//    {
//        mWindow.setPosition( position[0], position[1] );
//    }
}
