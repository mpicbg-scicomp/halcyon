package halcyon.model.node;

import java.util.HashSet;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * HalcyonGroupNode contains multiple nodes' panels
 */
public class HalcyonGroupNode extends HalcyonNodeBase implements HalcyonNodeInterface
{
	public enum Grouping { Vertical, Horizontal }

	private final Pane mPane;

	private ObservableList<Node> mList;

	private final Grouping mGrouping;

	private final HashSet<String> mNodeNames = new HashSet<>();

	public HalcyonGroupNode(String name,
			HalcyonNodeType type, Grouping grouping,
			List<HalcyonNodeInterface> nodes )
	{
		super(name, type);

		mGrouping = grouping;

		if(mGrouping == Grouping.Horizontal)
		{
			mPane = new HBox();
		}
		else if(mGrouping == Grouping.Vertical)
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
				mNodeNames.add( node.getName() );
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
		mNodeNames.remove( node.getName() );
	}

	public ObservableList< Node > getNodeList()
	{
		return mList;
	}

	public Grouping getGrouping()
	{
		return mGrouping;
	}

	public HashSet< String > getNodeNames()
	{
		return mNodeNames;
	}
}
