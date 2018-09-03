package halcyon.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

import halcyon.controller.ViewManager;
import halcyon.demo.DemoHalcyonNodeType;
import halcyon.model.collection.HalcyonNodeRepository;
import halcyon.model.collection.HalcyonNodeRepositoryListener;
import halcyon.model.node.HalcyonGroupNode;
import halcyon.model.node.HalcyonNode;
import halcyon.model.node.HalcyonNodeInterface;
import halcyon.model.node.HalcyonNodeType;

import org.dockfx.DockNode;

/**
 * TreePanel contains multiple HalcyonNodes for making HalcyonPanel
 */
public class TreePanel extends DockNode
{
  private HalcyonNodeRepository nodes;
  final private HashMap<String, TreeItem<TreeNode>> subNodes =
                                                             new HashMap<>();

  /**
   * The TreeView.
   */
  final TreeView<TreeNode> tree;

  /**
   * The Root context menu.
   */
  ContextMenu rootContextMenu;

  /**
   * Instantiates a new Tree panel.
   * 
   * @param pTitle
   *          the p title
   * @param pRootNodeName
   *          the p root node name
   * @param pRootIcon
   *          the p root icon
   * @param pHalcyonNodeTypes
   *          the p halcyon node types
   */
  public TreePanel(String pTitle,
                   String pRootNodeName,
                   InputStream pRootIcon,
                   Collection<HalcyonNodeType> pHalcyonNodeTypes)
  {
    super(new VBox());
    setTitle(pTitle);

    TreeItem<TreeNode> rootItem =
                                new TreeItem<>(new TreeNode(pRootNodeName),
                                               HalcyonNodeType.getIconPath(pRootIcon));
    rootItem.setExpanded(true);

    for (HalcyonNodeType type : pHalcyonNodeTypes)
    {
      TreeItem<TreeNode> node =
                              new TreeItem<>(new TreeNode(type.name()),
                                             type.getIcon());
      // node.setExpanded(true);
      subNodes.put(type.name(), node);
      rootItem.getChildren().add(node);
    }

    tree = new TreeView<>(rootItem);
    tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    this.heightProperty().addListener(new ChangeListener<Number>()
    {
      @Override
      public void changed(ObservableValue<? extends Number> observable,
                          Number oldValue,
                          Number newValue)
      {
        tree.setPrefHeight(newValue.doubleValue());
      }
    });
    setContents(tree);
  }

  /**
   * Sets Halcyon node repository and setups the event listeners.
   * 
   * @param nodes
   *          the nodes
   */
  public void setNodes(HalcyonNodeRepository nodes)
  {
    this.nodes = nodes;

    this.nodes.addListener(new HalcyonNodeRepositoryListener()
    {
      @Override
      public void nodeAdded(HalcyonNodeInterface node)
      {
        TreeItem<TreeNode> item =
                                new TreeItem<>(new TreeNode(node.getName(),
                                                            node));
        subNodes.get(node.getType().name()).getChildren().add(item);
      }

      @Override
      public void nodeRemoved(HalcyonNodeInterface node)
      {
        subNodes.get(node.getType().name())
                .getChildren()
                .removeIf(c -> c.getValue()
                                .getNode()
                                .getName()
                                .equals(node.getName()));
      }
    });
  }

  /**
   * Sets ViewManager.
   * 
   * @param viewManager
   *          the manager
   */
  @SuppressWarnings("deprecation")
  public void setViewManager(ViewManager viewManager)
  {
    tree.setOnMouseClicked(event -> {

      if (event.getClickCount() == 2)
      {
        if (event.isShiftDown())
        {
          TreeItem<TreeNode> item = tree.getSelectionModel()
                                        .getSelectedItem();
          if (item != null)
          {
            TreeNode node = item.getValue();
            if (node.getNode() != null)
            {
              viewManager.makeIndependentWindow(node.getNode());
              viewManager.open(node.getNode());
            }
          }
        }
        else
        {
          // Check the number of selected items
          ObservableList<TreeItem<TreeNode>> list =
                                                  tree.getSelectionModel()
                                                      .getSelectedItems();

          if (list.size() == 1)
          {
            TreeItem<TreeNode> item = tree.getSelectionModel()
                                          .getSelectedItem();
            if (item != null)
            {
              TreeNode node = item.getValue();
              if (node.getNode() != null)
              {
                viewManager.open(node.getNode());
              }
            }
          }
          else if (list.size() > 1)
          {
            // System.out.println("list size is " + list.size());
            // Make a composite panel from the selected items.

            // Open them in one panel

          }
        }
      }
    });

    rootContextMenu =
                    ContextMenuBuilder.create()
                                      .items(MenuItemBuilder.create()
                                                            .text("open")
                                                            .onAction(new EventHandler<ActionEvent>()
                                                            {
                                                              @Override
                                                              public void handle(ActionEvent arg0)
                                                              {
                                                                ObservableList<TreeItem<TreeNode>> list =
                                                                                                        tree.getSelectionModel()
                                                                                                            .getSelectedItems();

                                                                List<HalcyonNodeInterface> nodeList =
                                                                                                    new ArrayList<HalcyonNodeInterface>();

                                                                list.stream()
                                                                    .forEach(c -> {
                                                                      if (!c.getValue()
                                                                            .equals(tree.getRoot()))
                                                                      {
                                                                        if (c.getValue()
                                                                             .getNode() == null)
                                                                          c.getChildren()
                                                                           .forEach(t -> nodeList.add(t.getValue()
                                                                                                       .getNode()));
                                                                        else
                                                                          nodeList.add(c.getValue()
                                                                                        .getNode());
                                                                      }
                                                                    });

                                                                nodeList.stream()
                                                                        .forEach(viewManager::open);
                                                              }
                                                            })
                                                            .build(),
                                             MenuItemBuilder.create()
                                                            .text("open externally")
                                                            .onAction(new EventHandler<ActionEvent>()
                                                            {
                                                              @Override
                                                              public void handle(ActionEvent arg0)
                                                              {
                                                                ObservableList<TreeItem<TreeNode>> list =
                                                                                                        tree.getSelectionModel()
                                                                                                            .getSelectedItems();

                                                                List<HalcyonNodeInterface> nodeList =
                                                                                                    new ArrayList<HalcyonNodeInterface>();

                                                                list.stream()
                                                                    .forEach(c -> {
                                                                      if (!c.getValue()
                                                                            .equals(tree.getRoot()))
                                                                      {
                                                                        if (c.getValue()
                                                                             .getNode() == null)
                                                                          c.getChildren()
                                                                           .forEach(t -> nodeList.add(t.getValue()
                                                                                                       .getNode()));
                                                                        else
                                                                          nodeList.add(c.getValue()
                                                                                        .getNode());
                                                                      }
                                                                    });

                                                                nodeList.stream()
                                                                        .forEach(viewManager::makeIndependentWindow);
                                                              }
                                                            })
                                                            .build(),

                                             MenuItemBuilder.create()
                                                            .text("open vertically grouped panel")
                                                            .onAction(new EventHandler<ActionEvent>()
                                                            {
                                                              @Override
                                                              public void handle(ActionEvent arg0)
                                                              {
                                                                ObservableList<TreeItem<TreeNode>> list =
                                                                                                        tree.getSelectionModel()
                                                                                                            .getSelectedItems();

                                                                List<HalcyonNodeInterface> nodeList =
                                                                                                    new ArrayList<HalcyonNodeInterface>();

                                                                list.stream()
                                                                    .forEach(c -> {
                                                                      if (!c.getValue()
                                                                            .equals(tree.getRoot()))
                                                                      {
                                                                        if (c.getValue()
                                                                             .getNode() == null)
                                                                          c.getChildren()
                                                                           .forEach(t -> nodeList.add(t.getValue()
                                                                                                       .getNode()));
                                                                        else
                                                                          nodeList.add(c.getValue()
                                                                                        .getNode());
                                                                      }
                                                                    });

                                                                nodeList.stream()
                                                                        .forEach(viewManager::close);

                                                                HalcyonGroupNode lHalcyonGroupNode =
                                                                                                   new HalcyonGroupNode("",
                                                                                                                        DemoHalcyonNodeType.ONE,
                                                                                                                        HalcyonGroupNode.Grouping.Vertical,
                                                                                                                        nodeList);

                                                                viewManager.makeIndependentWindow(lHalcyonGroupNode);
                                                              }
                                                            })
                                                            .build(),

                                             MenuItemBuilder.create()
                                                            .text("open horizontally grouped panel")
                                                            .onAction(new EventHandler<ActionEvent>()
                                                            {
                                                              @Override
                                                              public void handle(ActionEvent arg0)
                                                              {
                                                                ObservableList<TreeItem<TreeNode>> list =
                                                                                                        tree.getSelectionModel()
                                                                                                            .getSelectedItems();

                                                                List<HalcyonNodeInterface> nodeList =
                                                                                                    new ArrayList<HalcyonNodeInterface>();

                                                                list.stream()
                                                                    .forEach(c -> {
                                                                      if (!c.getValue()
                                                                            .equals(tree.getRoot()))
                                                                      {
                                                                        if (c.getValue()
                                                                             .getNode() == null)
                                                                          c.getChildren()
                                                                           .forEach(t -> nodeList.add(t.getValue()
                                                                                                       .getNode()));
                                                                        else
                                                                          nodeList.add(c.getValue()
                                                                                        .getNode());
                                                                      }
                                                                    });

                                                                nodeList.stream()
                                                                        .forEach(viewManager::close);

                                                                HalcyonGroupNode lHalcyonGroupNode =
                                                                                                   new HalcyonGroupNode("",
                                                                                                                        DemoHalcyonNodeType.ONE,
                                                                                                                        HalcyonGroupNode.Grouping.Horizontal,
                                                                                                                        nodeList);

                                                                viewManager.makeIndependentWindow(lHalcyonGroupNode);
                                                              }
                                                            })
                                                            .build(),
                                             MenuItemBuilder.create()
                                                            .text("open tab grouped panel")
                                                            .onAction(new EventHandler<ActionEvent>()
                                                            {
                                                              @Override
                                                              public void handle(ActionEvent arg0)
                                                              {
                                                                ObservableList<TreeItem<TreeNode>> list =
                                                                                                        tree.getSelectionModel()
                                                                                                            .getSelectedItems();

                                                                List<HalcyonNodeInterface> nodeList =
                                                                                                    new ArrayList<HalcyonNodeInterface>();

                                                                list.stream()
                                                                    .forEach(c -> {
                                                                      if (!c.getValue()
                                                                            .equals(tree.getRoot()))
                                                                      {
                                                                        if (c.getValue()
                                                                             .getNode() == null)
                                                                          c.getChildren()
                                                                           .forEach(t -> nodeList.add(t.getValue()
                                                                                                       .getNode()));
                                                                        else
                                                                          nodeList.add(c.getValue()
                                                                                        .getNode());
                                                                      }
                                                                    });

                                                                nodeList.stream()
                                                                        .forEach(viewManager::close);

                                                                HalcyonGroupNode lHalcyonGroupNode =
                                                                                                   new HalcyonGroupNode("",
                                                                                                                        DemoHalcyonNodeType.ONE,
                                                                                                                        HalcyonGroupNode.Grouping.Tab,
                                                                                                                        nodeList);

                                                                viewManager.makeIndependentWindow(lHalcyonGroupNode);
                                                              }
                                                            })
                                                            .build(),

                                             MenuItemBuilder.create()
                                                            .text("close")
                                                            .onAction(new EventHandler<ActionEvent>()
                                                            {
                                                              @Override
                                                              public void handle(ActionEvent arg0)
                                                              {
                                                                ObservableList<TreeItem<TreeNode>> list =
                                                                                                        tree.getSelectionModel()
                                                                                                            .getSelectedItems();

                                                                List<HalcyonNodeInterface> nodeList =
                                                                                                    new ArrayList<HalcyonNodeInterface>();

                                                                list.stream()
                                                                    .forEach(c -> {
                                                                      if (!c.getValue()
                                                                            .equals(tree.getRoot()))
                                                                      {
                                                                        if (c.getValue()
                                                                             .getNode() == null)
                                                                          c.getChildren()
                                                                           .forEach(t -> nodeList.add(t.getValue()
                                                                                                       .getNode()));
                                                                        else
                                                                          nodeList.add(c.getValue()
                                                                                        .getNode());
                                                                      }
                                                                    });

                                                                nodeList.stream()
                                                                        .forEach(viewManager::close);
                                                              }
                                                            })
                                                            .build())
                                      .build();

    tree.setContextMenu(rootContextMenu);

    removeNoChildNode();
  }

  private void removeNoChildNode()
  {
    final List<TreeItem> lRemoveItemList = new ArrayList<>();

    tree.getRoot().getChildren().forEach(c -> {
      if (c.getChildren().size() == 0)
      {
        lRemoveItemList.add(c);
        subNodes.remove(c.getValue().getName());
      }
    });

    tree.getRoot()
        .getChildren()
        .removeIf(c -> lRemoveItemList.contains(c));
  }

  private class TreeNode
  {
    private String name;

    private HalcyonNodeInterface node;

    /**
     * Instantiates a new Tree node.
     * 
     * @param name
     *          the name
     */
    public TreeNode(String name)
    {
      this.name = name;
    }

    /**
     * Instantiates a new Tree node.
     * 
     * @param name
     *          the name
     * @param node
     *          the node
     */
    public TreeNode(String name, HalcyonNodeInterface node)
    {
      this.name = name;
      this.node = node;
    }

    /**
     * Gets name.
     * 
     * @return the name
     */
    public String getName()
    {
      return name;
    }

    /**
     * Sets name.
     * 
     * @param name
     *          the name
     */
    public void setName(String name)
    {
      this.name = name;
    }

    /**
     * Gets Halcyon node.
     * 
     * @return the node
     */
    public HalcyonNodeInterface getNode()
    {
      return node;
    }

    /**
     * Sets Halcyon node.
     * 
     * @param node
     *          the node
     */
    public void setNode(HalcyonNode node)
    {
      this.node = node;
    }

    /**
     * Makes a string.
     * 
     * @return the string
     */
    @Override
    public String toString()
    {
      return this.name;
    }
  }
}
