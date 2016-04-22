package halcyon.view;

import halcyon.model.list.HalcyonNodeRepository;
import halcyon.model.list.HalcyonNodeRepositoryListener;
import halcyon.model.node.HalcyonNode;
import halcyon.model.node.HalcyonNodeInterface;
import halcyon.model.node.HalcyonNodeType;
import halcyon.window.control.ControlWindowBase;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

/**
 * Device Config Window
 */
public class TreePanel extends ControlWindowBase
{
	final private HashMap<String, TreeItem<TreeNode>> subNodes = new HashMap<>();
	final TreeView<TreeNode> tree;

	ContextMenu rootContextMenu;

	public TreePanel(	String pTitle,
										String pRootNodeName,
										InputStream pRootIcon,
										Collection<HalcyonNodeType> pHalcyonNodeTypes)
	{
		super(new VBox());
		setTitle(pTitle);

		TreeItem<TreeNode> rootItem = new TreeItem<>(	new TreeNode(pRootNodeName),
																									HalcyonNodeType.getIconPath(pRootIcon));
		rootItem.setExpanded(true);

		for (HalcyonNodeType type : pHalcyonNodeTypes)
		{
			TreeItem<TreeNode> node = new TreeItem<>(	new TreeNode(type.name()),
																								type.getIcon());
			node.setExpanded(true);
			subNodes.put(type.name(), node);
			rootItem.getChildren().add(node);
		}

		tree = new TreeView<>(rootItem);

		this.heightProperty().addListener( new ChangeListener< Number >()
		{
			@Override public void changed( ObservableValue< ? extends Number > observable, Number oldValue, Number newValue )
			{
				tree.setPrefHeight( newValue.doubleValue() );
			}
		} );
		setContents( tree );
	}

	@Override
	public void setNodes(HalcyonNodeRepository nodes)
	{
		this.nodes = nodes;

		this.nodes.addListener(new HalcyonNodeRepositoryListener()
		{
			@Override
			public void nodeAdded(HalcyonNodeInterface node)
			{
				TreeItem<TreeNode> item = new TreeItem<>(new TreeNode(node.getName(),
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

	@SuppressWarnings("deprecation")
	@Override
	public void setViewManager(ViewManager manager)
	{
		tree.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2)
			{
				// Check the number of selected items
				ObservableList<TreeItem<TreeNode>> list = tree.getSelectionModel()
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
							manager.open(node.getNode());
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

			if (event.isSecondaryButtonDown())
			{
				rootContextMenu.show(tree, Side.BOTTOM, 0, 0);
			}
		});

		rootContextMenu = ContextMenuBuilder.create()
																				.items(	MenuItemBuilder.create()
																																.text("Create a Panel")
																																.onAction(new EventHandler<ActionEvent>()
																																{
																																	@Override
																																	public void handle(ActionEvent arg0)
																																	{
																																		ObservableList<TreeItem<TreeNode>> list = tree.getSelectionModel()
																																																									.getSelectedItems();
																																		VBox vBox = new VBox();

																																		for (TreeItem<TreeNode> n : list)
																																		{
																																			System.out.println(n.getValue()
																																													.getName() + " is selected.");
																																			vBox.getChildren()
																																					.add(n.getValue()
																																								.getNode()
																																								.getPanel());
																																		}

																																		for (TreeItem<TreeNode> n : list)
																																		{
																																			nodes.remove(n.getValue()
																																										.getNode());
																																		}

																																		// Get the
																																		// default
																																		// NodeType
																																		// HalcyonNode
																																		// node =
																																		// new
																																		// HalcyonNode(
																																		// "User panel",
																																		// RTlibNodeType.Laser,
																																		// vBox );
																																		// nodes.add(
																																		// node );
																																	}
																																})
																																.build(),
																								MenuItemBuilder.create()
																																.text("Remove")
																																.onAction(new EventHandler<ActionEvent>()
																																{
																																	@Override
																																	public void handle(ActionEvent arg0)
																																	{
																																		ObservableList<TreeItem<TreeNode>> list = tree.getSelectionModel()
																																																									.getSelectedItems();
																																		for (TreeItem<TreeNode> n : list)
																																		{
																																			nodes.remove(n.getValue()
																																										.getNode());
																																			manager.close(n.getValue()
																																											.getNode());
																																		}
																																	}
																																})
																																.build())
																				.build();

		tree.setContextMenu(rootContextMenu);
	}

	private class TreeNode
	{
		private String name;

		private HalcyonNodeInterface node;

		public TreeNode(String name)
		{
			this.name = name;
		}

		public TreeNode(String name, HalcyonNodeInterface node)
		{
			this.name = name;
			this.node = node;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public HalcyonNodeInterface getNode()
		{
			return node;
		}

		public void setNode(HalcyonNode node)
		{
			this.node = node;
		}

		@Override
		public String toString()
		{
			return this.name;
		}
	}
}
