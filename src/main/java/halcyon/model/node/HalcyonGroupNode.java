package halcyon.model.node;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * HalcyonGroupNode contains multiple nodes' panels
 */
public class HalcyonGroupNode extends HalcyonNodeBase implements HalcyonNodeInterface
{
	public enum Grouping { Vertical, Horizontal }

	private final Pane mPane;

	public ObservableList< Node > getNodeList()
	{
		return mList;
	}

	private ObservableList<Node> mList;

	public HalcyonGroupNode(String name,
			HalcyonNodeType type, Grouping grouping,
			List<HalcyonNodeInterface> nodes )
	{
		super(name, type);

		if(grouping == Grouping.Horizontal)
		{
			mPane = new HBox();
		}
		else if(grouping == Grouping.Vertical)
		{
			mPane = new VBox();
		}
		else
		{
			System.err.println( "Grouping method is not specified." );
			mPane = null;
		}

		if(mPane != null)
		{
			for( HalcyonNodeInterface node : nodes )
			{
//				if(node.getPanel() != null)
				mPane.getChildren().add( node.getPanel() );
			}
			mList = mPane.getChildren();
		}
	}

	@Override public Node getPanel()
	{
		return mPane;
	}

	public void removeNode( HalcyonNodeInterface node )
	{
		mPane.getChildren().removeIf( c -> c.equals( node.getPanel() ) );

	}

}
