package window.demo;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.component.RunnableFX;
import org.controlsfx.control.textfield.TextFields;
import utils.RunFX;

import java.util.Arrays;

/**
 * Created by moon on 1/29/16.
 */
public class DemoCamera implements RunnableFX
{
	final private int resUnit = 256;
	private int expectedWidth, expectedHeight;
	private StringProperty widthStringProperty, heightStringProperty;
	private GridPane gridPane;
	Rectangle rect = createDraggableRectangle( 140, 135, 20, 30 );
	Line hLine, vLine;
	Text hText, vText;

	public DemoCamera()
	{

	}

	@Override public void init()
	{
		gridPane = new GridPane();

		for ( int x = 8; x < 12; x++ )
		{
			for ( int y = 8; y < 12; y++ )
			{
				int width = ( int ) Math.pow( 2, x );
				int height = ( int ) Math.pow( 2, y );

				Button button = new Button( height + "\n" + width );
				button.setMaxWidth( Double.MAX_VALUE );
				button.setOnAction( event -> {
					expectedWidth = width;
					expectedHeight = height;
					widthStringProperty.setValue( Integer.toString( width ) );
					heightStringProperty.setValue( Integer.toString( height ) );

					System.out.println( "Set width/height: " + width + "/" + height );
				} );

				// Place the button on the GridPane
				gridPane.add( button, x, y );
			}
		}
	}

	@Override public void start( Stage stage )
	{
		Parent pane = getPanel();

		Scene scene = new Scene( pane, Color.WHITE );

		//scene.setFullScreen(true);

		stage.setTitle( "Camera-1" );
		stage.setScene( scene );
		stage.show();
	}

	@Override public void stop()
	{

	}

	public Parent getPanel()
	{
		int size = 300;
		Pane canvas = new Pane();
		canvas.setStyle( "-fx-background-color: green;" );
		canvas.setPrefSize( size, size );

		Line line = new Line( size / 2, 0, size / 2, size );
		canvas.getChildren().add( line );

		line = new Line( 0, size / 2, size, size / 2 );
		canvas.getChildren().add( line );

		canvas.getChildren().addAll( rect, hLine, vLine, hText, vText );

//		HBox hBox = new HBox();
//		hBox.setBackground( null );
//		hBox.setPadding( new Insets( 15, 15, 15, 15 ) );
//		hBox.setSpacing( 10 );
//		hBox.getChildren().addAll( gridPane, canvas );
//		hBox.setStyle( "-fx-border-style: solid;"
//				+ "-fx-border-width: 1;"
//				+ "-fx-border-color: grey" );

		HBox widthBox = new HBox( 5 );
		widthBox.setPadding( new Insets(30, 10, 10, 10) );
		widthBox.setAlignment( Pos.CENTER );
		widthBox.getChildren().add( new Label( "Width: " ) );
		TextField width = new TextField();
		width.setPrefWidth( 80 );
		widthStringProperty = width.textProperty();
		widthBox.getChildren().add( width );

		HBox heightBox = new HBox( 5 );
		heightBox.setPadding( new Insets(10, 10, 10, 10) );
		heightBox.setAlignment( Pos.CENTER );
		heightBox.getChildren().add( new Label( "Height: " ));
		TextField height = new TextField();
		height.setPrefWidth( 80 );
		heightStringProperty = height.textProperty();
		heightBox.getChildren().add( height );

		VBox vBox = new VBox( gridPane, widthBox, heightBox );

		AnchorPane hBox = new AnchorPane();
		hBox.setBackground( null );
		hBox.setPadding( new Insets( 15, 15, 15, 15 ) );
		hBox.getChildren().addAll( vBox, canvas );
		AnchorPane.setLeftAnchor( canvas, 200d );
		hBox.setStyle( "-fx-border-style: solid;"
				+ "-fx-border-width: 1;"
				+ "-fx-border-color: grey" );

		return hBox;
	}

	private void setDragHandlers(final Line line, final Rectangle rect, final Cursor cursor, Wrapper< Point2D > mouseLocation)
	{
		line.setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				line.setCursor( cursor );
			}
		});

		line.setOnMouseExited(new EventHandler<MouseEvent >()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				line.setCursor( Cursor.NONE );
			}
		});

		line.setOnMouseDragged( new EventHandler< MouseEvent >()
		{
			@Override public void handle( MouseEvent event )
			{
				if ( cursor == Cursor.V_RESIZE )
				{
					System.out.println(event.getSceneY());
				}
				else if ( cursor == Cursor.H_RESIZE )
				{
					System.out.println(event.getSceneX());
				}
			}
		} );

		line.setOnDragDetected( event -> {
			mouseLocation.value = new Point2D( event.getSceneX(), event.getSceneY() );
		} );

		line.setOnMouseReleased( event -> {
			mouseLocation.value = null;
		} );
	}


	private Rectangle createDraggableRectangle( double x, double y, double width, double height )
	{
		final double handleRadius = 5;

		Rectangle rect = new Rectangle( x, y, width, height );
		rect.setFill( Color.color( 0, 0, 0, 0.50 ) );

		Wrapper< Point2D > mouseLocation = new Wrapper<>();

		hText = new Text();
		hText.setStroke( Color.WHITE );
		hText.textProperty().bind( rect.widthProperty().asString( "%.0f px" ) );
		hText.translateXProperty().bind( rect.xProperty().add( rect.widthProperty().divide( 3 )) );
		hText.translateYProperty().bind( rect.yProperty().add( rect.heightProperty().add( 13 )));

		hLine = new Line( x, y, x + width, y);
		hLine.setStroke( Color.WHITE );
		setDragHandlers( hLine, rect, Cursor.V_RESIZE, mouseLocation );

		hLine.startXProperty().bind( rect.xProperty() );
		hLine.startYProperty().bind( rect.yProperty().add( rect.heightProperty() ) );
		hLine.endYProperty().bind( rect.yProperty().add( rect.heightProperty() ) );
		hLine.endXProperty().bind( rect.xProperty().add( rect.widthProperty() ) );

		hLine.setOnMouseDragged( event -> {
			if ( mouseLocation.value != null )
			{
				double deltaY = event.getSceneY() - mouseLocation.value.getY();
				double newMaxY = rect.getY() + rect.getHeight() + deltaY;
				if ( newMaxY >= rect.getY()
						&& newMaxY <= rect.getParent().getBoundsInLocal().getHeight() )
				{
					rect.setY( rect.getY() - ( deltaY / 2 ) );
					rect.setHeight( rect.getHeight() + deltaY );
				}
				mouseLocation.value = new Point2D( event.getSceneX(), event.getSceneY() );
			}
		} );

		vText = new Text();
		vText.setStroke( Color.WHITE );
		vText.setTranslateX( 7 );
		vText.textProperty().bind( rect.heightProperty().asString( "%.0f px" ) );
		vText.translateXProperty().bind( rect.xProperty().add( rect.widthProperty().add( 10 ) ) );
		vText.translateYProperty().bind( rect.yProperty().add( rect.heightProperty().divide( 2 )));

		vLine = new Line( x + width, y, x + width, y + height);
		vLine.setStroke( Color.WHITE );
		setDragHandlers( vLine, rect, Cursor.H_RESIZE, mouseLocation );

		vLine.startXProperty().bind( rect.xProperty().add( rect.widthProperty() ) );
		vLine.startYProperty().bind( rect.yProperty() );
		vLine.endXProperty().bind( rect.xProperty().add( rect.widthProperty() ) );
		vLine.endYProperty().bind( rect.yProperty().add( rect.heightProperty() ) );

		vLine.setOnMouseDragged( event -> {
			if ( mouseLocation.value != null )
			{
				double deltaX = event.getSceneX() - mouseLocation.value.getX();
				double newMaxX = rect.getX() + rect.getWidth() + deltaX;
				if ( newMaxX >= rect.getX()
						&& newMaxX <= rect.getParent().getBoundsInLocal().getWidth() )
				{
					rect.setX( rect.getX() - ( deltaX / 2 ) );
					rect.setWidth( rect.getWidth() + deltaX );
				}
				mouseLocation.value = new Point2D( event.getSceneX(), event.getSceneY() );
			}
		} );

		// top left resize handle:
		Circle resizeHandleNW = new Circle( handleRadius, Color.BLUEVIOLET );
		// bind to top left corner of Rectangle:
		resizeHandleNW.centerXProperty().bind( rect.xProperty().add( 20 ) );
		resizeHandleNW.centerYProperty().bind( rect.yProperty() );

		// bottom right resize handle:
		Circle resizeHandleSE = new Circle( handleRadius, Color.GOLDENROD );
		// bind to bottom right corner of Rectangle:
		resizeHandleSE.centerXProperty().bind( rect.xProperty().add( rect.widthProperty() ) );
		resizeHandleSE.centerYProperty().bind( rect.yProperty().add( rect.heightProperty() ) );

		// move handle:
		Circle moveHandle = new Circle( handleRadius, Color.CRIMSON );
		// bind to bottom center of Rectangle:
		moveHandle.centerXProperty().bind( rect.xProperty() );
		moveHandle.centerYProperty().bind( rect.yProperty() );

		// force circles to live in same parent as rectangle:
		rect.parentProperty().addListener( ( obs, oldParent, newParent ) -> {
			for ( Circle c : Arrays.asList( resizeHandleNW, resizeHandleSE, moveHandle ) )
			{
				Pane currentParent = ( Pane ) c.getParent();
				if ( currentParent != null )
				{
					currentParent.getChildren().remove( c );
				}
				( ( Pane ) newParent ).getChildren().add( c );
			}
		} );

		setUpDragging( resizeHandleNW, mouseLocation );
		setUpDragging( resizeHandleSE, mouseLocation );
		setUpDragging( moveHandle, mouseLocation );

		resizeHandleNW.setOnMouseDragged( event -> {
			if ( mouseLocation.value != null )
			{
				double deltaX = event.getSceneX() - mouseLocation.value.getX();
				double deltaY = event.getSceneY() - mouseLocation.value.getY();
				double newX = rect.getX() + deltaX;
				if ( newX >= handleRadius
						&& newX <= rect.getX() + rect.getWidth() - handleRadius )
				{
					rect.setX( newX );
					rect.setWidth( rect.getWidth() - deltaX );
				}
				double newY = rect.getY() + deltaY;
				if ( newY >= handleRadius
						&& newY <= rect.getY() + rect.getHeight() - handleRadius )
				{
					rect.setY( newY );
					rect.setHeight( rect.getHeight() - deltaY );
				}
				mouseLocation.value = new Point2D( event.getSceneX(), event.getSceneY() );
			}
		} );

		resizeHandleSE.setOnMouseDragged( event -> {
			if ( mouseLocation.value != null )
			{
				double deltaX = event.getSceneX() - mouseLocation.value.getX();
				double deltaY = event.getSceneY() - mouseLocation.value.getY();
				double newMaxX = rect.getX() + rect.getWidth() + deltaX;
				if ( newMaxX >= rect.getX()
						&& newMaxX <= rect.getParent().getBoundsInLocal().getWidth() - handleRadius )
				{
					rect.setX( rect.getX() - ( deltaX / 2 ) );
					rect.setWidth( rect.getWidth() + deltaX );
				}
				double newMaxY = rect.getY() + rect.getHeight() + deltaY;
				if ( newMaxY >= rect.getY()
						&& newMaxY <= rect.getParent().getBoundsInLocal().getHeight() - handleRadius )
				{
					rect.setY( rect.getY() - ( deltaY / 2 ) );
					rect.setHeight( rect.getHeight() + deltaY );
				}
				mouseLocation.value = new Point2D( event.getSceneX(), event.getSceneY() );
			}
		} );

		moveHandle.setOnMouseDragged( event -> {
			if ( mouseLocation.value != null )
			{
				double deltaX = event.getSceneX() - mouseLocation.value.getX();
				double deltaY = event.getSceneY() - mouseLocation.value.getY();
				double newX = rect.getX() + deltaX;
				double newMaxX = newX + rect.getWidth();
				if ( newX >= handleRadius
						&& newMaxX <= rect.getParent().getBoundsInLocal().getWidth() - handleRadius )
				{
					rect.setX( newX );
				}
				double newY = rect.getY() + deltaY;
				double newMaxY = newY + rect.getHeight();
				if ( newY >= handleRadius
						&& newMaxY <= rect.getParent().getBoundsInLocal().getHeight() - handleRadius )
				{
					rect.setY( newY );
				}
				mouseLocation.value = new Point2D( event.getSceneX(), event.getSceneY() );
			}

		} );

		return rect;
	}

	private void setUpDragging( Circle circle, Wrapper< Point2D > mouseLocation )
	{

		circle.setOnDragDetected( event -> {
			circle.getParent().setCursor( Cursor.CLOSED_HAND );
			mouseLocation.value = new Point2D( event.getSceneX(), event.getSceneY() );
		} );

		circle.setOnMouseReleased( event -> {
			circle.getParent().setCursor( Cursor.DEFAULT );
			mouseLocation.value = null;
		} );
	}

	static class Wrapper< T >
	{
		T value;
	}

	public static void main( final String[] args )
	{
		RunFX.start( new DemoCamera() );
	}
}