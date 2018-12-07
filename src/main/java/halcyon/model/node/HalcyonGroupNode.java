package halcyon.model.node;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import org.apache.commons.lang3.StringUtils;

/**
 * HalcyonGroupNode contains multiple nodes' panels
 */
public class HalcyonGroupNode extends HalcyonNodeBase
                              implements HalcyonNodeInterface
{
  public enum Grouping
  {
   Vertical, Horizontal, Tab
  }

  private final Pane mPane;

  private final TabPane mTabPane;

  private final ScrollPane mScrollPane;

  private ObservableList<Node> mList;

  private final Grouping mGrouping;

  private final LinkedHashSet<String> mNodeNames =
                                                 new LinkedHashSet<>();

  public HalcyonGroupNode(String name,
                          HalcyonNodeType type,
                          Grouping grouping,
                          List<HalcyonNodeInterface> nodes)
  {
    super(name, type);

    mGrouping = grouping;

    if (mGrouping == Grouping.Horizontal)
    {
      mTabPane = null;
      mPane = new HBox();
      mScrollPane = new ScrollPane();
      mScrollPane.setContent(mPane);
    }
    else if (mGrouping == Grouping.Vertical)
    {
      mTabPane = null;
      mPane = new VBox();
      mScrollPane = new ScrollPane();
      mScrollPane.setContent(mPane);
    }
    else if (mGrouping == Grouping.Tab)
    {
      mPane = null;
      mTabPane = new TabPane();
      mScrollPane = new ScrollPane();
      mScrollPane.setContent(mTabPane);
    }
    else
    {
      System.err.println("Grouping method is not specified.");
      mPane = null;
      mTabPane = null;
      mScrollPane = null;
    }

    if (mPane != null)
    {
      for (HalcyonNodeInterface node : nodes)
      {
        mPane.getChildren().add(node.getPanel());
        mNodeNames.add(node.getName());
      }
      mList = mPane.getChildren();
    }
    else if (mTabPane != null)
    {
      List<Node> list = new ArrayList<Node>();
      for (HalcyonNodeInterface node : nodes)
      {
        final Tab tab = new Tab(node.getName());
        tab.setClosable(false);
        tab.setContent(node.getPanel());
        mTabPane.getTabs().add(tab);
        mNodeNames.add(node.getName());
        list.add(node.getPanel());
      }
      mList = FXCollections.observableList(list);
    }
  }

  @Override
  public Node getPanel()
  {
    return mScrollPane;
  }

  public void removeNode(HalcyonNodeInterface node)
  {
    if (mPane != null)
      mPane.getChildren().removeIf(c -> c.equals(node.getPanel()));
    else if (mTabPane != null)
      mTabPane.getTabs()
              .removeIf(c -> c.getContent().equals(node.getPanel()));

    mNodeNames.remove(node.getName());
  }

  public ObservableList<Node> getNodeList()
  {
    return mList;
  }

  public Grouping getGrouping()
  {
    return mGrouping;
  }

  public LinkedHashSet<String> getNodeNames()
  {
    return mNodeNames;
  }

  public String getTitle()
  {
    return StringUtils.join(mNodeNames, " | ");
  }
}
