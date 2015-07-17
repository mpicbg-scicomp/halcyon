package model.javafx;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import model.node.HalcyonNode;
import model.node.HalcyonNodeType;
import model.provider.JFXPanelProvider;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * HalcyonNode for JavaFX
 */
public class FxHalcyonNode extends HalcyonNode implements JFXPanelProvider
{
	private JFXPanel fxPanel;
	private Node node;

	public FxHalcyonNode( String name, HalcyonNodeType type, Node node )
	{
		super(name, type);
		this.node = node;

		Platform.setImplicitExit( false );

		fxPanel = new JFXPanel();

		JPanel panel = new JPanel( new BorderLayout() );

		panel.add( fxPanel, BorderLayout.CENTER );

		setPanel( panel );

		Platform.runLater( this::start );
	}

	@Override public JFXPanel getJFXPanel()
	{
		return fxPanel;
	}

	protected void start()
	{
		StackPane root = new StackPane();

		root.getChildren().add( node );

		fxPanel.setScene( new Scene( root, 300, 250 ) );
	}
}
