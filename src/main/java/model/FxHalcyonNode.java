package model;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * HalcyonNode for JavaFX
 */
public class FxHalcyonNode extends HalcyonNode
{
	private JFXPanel fxPanel;
	private Node node;

	public FxHalcyonNode( String name, Type type, Node node )
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

	protected void start()
	{
		StackPane root = new StackPane();

		root.getChildren().add( node );

		fxPanel.setScene( new Scene( root, 300, 250 ) );
	}
}
