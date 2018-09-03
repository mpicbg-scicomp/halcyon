package halcyon.util.demo;

import java.util.Arrays;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import halcyon.util.RunFX;
import halcyon.util.RunnableFX;

/**
 * DemoCamera application for showing Camera resolution control
 */
public class RunFXDemo implements RunnableFX
{
  final private int resUnit = 256;
  final int size = 300;

  // String properties hold the actual dimension size for the capture resolution
  private StringProperty widthStringProperty, heightStringProperty;

  // Double properties hold pixel based values for the rectangle's width and
  // height
  private DoubleProperty widthDoubleProperty, heightDoubleProperty;

  private GridPane gridPane;
  Rectangle rect = createDraggableRectangle(37.5, 37.5);
  Line hLine, vLine;
  Text hText, vText;

  public RunFXDemo()
  {
    // Setting up the double properties with 256x256
    widthDoubleProperty = new SimpleDoubleProperty(37.5);
    heightDoubleProperty = new SimpleDoubleProperty(37.5);
  }

  @Override
  public void init()
  {
    gridPane = new GridPane();

    for (int x = 7; x < 11; x++)
    {
      for (int y = 7; y < 11; y++)
      {
        int width = 2 << x;
        int height = 2 << y;

        Button button = new Button(height + "\n" + width);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> {
          widthDoubleProperty.set(width * size / 2048);
          heightDoubleProperty.set(height * size / 2048);

          widthStringProperty.set(Integer.toString(width));
          heightStringProperty.set(Integer.toString(height));

          // System.out.println( "Set width/height: " + width + "/" + height );
        });

        // Place the button on the GridPane
        gridPane.add(button, x, y);
      }
    }
  }

  @Override
  public void start(Stage stage)
  {
    Parent pane = getPanel();
    Scene scene = new Scene(pane, Color.WHITE);

    stage.setTitle("Camera-1");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop()
  {

  }

  public Parent getPanel()
  {
    Pane canvas = new Pane();
    canvas.setStyle("-fx-background-color: green;");
    canvas.setPrefSize(size, size);

    Line line = new Line(size / 2, 0, size / 2, size);
    canvas.getChildren().add(line);

    line = new Line(0, size / 2, size, size / 2);
    canvas.getChildren().add(line);

    canvas.getChildren().addAll(rect);

    HBox widthBox = new HBox(5);
    widthBox.setPadding(new Insets(30, 10, 10, 10));
    widthBox.setAlignment(Pos.CENTER);
    widthBox.getChildren().add(new Label("Width: "));
    TextField width = new TextField();
    width.setPrefWidth(80);
    widthStringProperty = width.textProperty();
    widthBox.getChildren().add(width);

    HBox heightBox = new HBox(5);
    heightBox.setPadding(new Insets(10, 10, 10, 10));
    heightBox.setAlignment(Pos.CENTER);
    heightBox.getChildren().add(new Label("Height: "));
    TextField height = new TextField();
    height.setPrefWidth(80);
    heightStringProperty = height.textProperty();
    heightBox.getChildren().add(height);

    VBox vBox = new VBox(gridPane, widthBox, heightBox);

    AnchorPane hBox = new AnchorPane();
    hBox.setBackground(null);
    hBox.setPadding(new Insets(15, 15, 15, 15));
    hBox.getChildren().addAll(vBox, canvas);

    AnchorPane.setLeftAnchor(vBox, 3d);
    AnchorPane.setTopAnchor(vBox, 10d);

    AnchorPane.setLeftAnchor(canvas, 220d);
    AnchorPane.setTopAnchor(canvas, 10d);

    hBox.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1;"
                  + "-fx-border-color: grey");

    widthDoubleProperty.bindBidirectional(rect.widthProperty());
    heightDoubleProperty.bindBidirectional(rect.heightProperty());

    Bindings.bindBidirectional(widthStringProperty,
                               widthDoubleProperty,
                               new StringConverter<Number>()
                               {
                                 @Override
                                 public String toString(Number object)
                                 {
                                   return Integer.toString((int) Math.round(object.doubleValue()
                                                                            * 2048
                                                                            / size));
                                 }

                                 @Override
                                 public Number fromString(String string)
                                 {
                                   return Double.parseDouble(string)
                                          * size / 2048;
                                 }
                               });

    Bindings.bindBidirectional(heightStringProperty,
                               heightDoubleProperty,
                               new StringConverter<Number>()
                               {
                                 @Override
                                 public String toString(Number object)
                                 {
                                   return Integer.toString((int) Math.round(object.doubleValue()
                                                                            * 2048
                                                                            / size));
                                 }

                                 @Override
                                 public Number fromString(String string)
                                 {
                                   return Double.parseDouble(string)
                                          * size / 2048;
                                 }
                               });

    return hBox;
  }

  private void setDragHandlers(final Line line,
                               final Rectangle rect,
                               final Cursor cursor,
                               Wrapper<Point2D> mouseLocation)
  {
    line.setOnMouseEntered(mouseEvent -> line.setCursor(cursor));

    line.setOnMouseDragged(event -> {
      if (cursor == Cursor.V_RESIZE)
      {
        System.out.println(event.getSceneY());
      }
      else if (cursor == Cursor.H_RESIZE)
      {
        System.out.println(event.getSceneX());
      }
    });

    line.setOnDragDetected(event -> {
      mouseLocation.value = new Point2D(event.getSceneX(),
                                        event.getSceneY());
    });

    line.setOnMouseReleased(event -> {
      mouseLocation.value = null;
      line.setCursor(Cursor.NONE);
    });
  }

  private Rectangle createDraggableRectangle(double width,
                                             double height)
  {
    double x = size / 2 - width / 2;
    double y = size / 2 - height / 2;

    Rectangle rect = new Rectangle(x, y, width, height);

    rect.heightProperty()
        .addListener((observable,
                      oldValue,
                      newValue) -> rect.setY(size / 2
                                             - newValue.intValue()
                                               / 2));

    rect.widthProperty()
        .addListener((observable,
                      oldValue,
                      newValue) -> rect.setX(size / 2
                                             - newValue.intValue()
                                               / 2));

    rect.setFill(Color.color(0, 0, 0, 0.50));

    Wrapper<Point2D> mouseLocation = new Wrapper<>();

    hText = new Text();
    hText.setStroke(Color.WHITE);
    hText.textProperty()
         .bind(rect.widthProperty()
                   .multiply(2048f / size)
                   .asString("%.0f px"));
    hText.translateXProperty()
         .bind(rect.xProperty()
                   .add(rect.widthProperty().divide(2.5)));
    hText.translateYProperty()
         .bind(rect.yProperty()
                   .add(rect.heightProperty().subtract(13)));

    hLine = new Line(x, y, x + width, y);
    hLine.setStrokeWidth(5);
    hLine.setStroke(Color.WHITE);
    setDragHandlers(hLine, rect, Cursor.V_RESIZE, mouseLocation);

    hLine.startXProperty().bind(rect.xProperty());
    hLine.startYProperty()
         .bind(rect.yProperty().add(rect.heightProperty()));
    hLine.endYProperty()
         .bind(rect.yProperty().add(rect.heightProperty()));
    hLine.endXProperty()
         .bind(rect.xProperty().add(rect.widthProperty()));

    hLine.setOnMouseDragged(event -> {
      if (mouseLocation.value != null)
      {
        double deltaY =
                      event.getSceneY() - mouseLocation.value.getY();
        double newMaxY = rect.getY() + rect.getHeight() + deltaY;
        if (newMaxY >= rect.getY() && newMaxY <= size)
        {
          rect.setHeight(rect.getHeight() + deltaY * 2);
        }
        mouseLocation.value = new Point2D(event.getSceneX(),
                                          event.getSceneY());
      }
    });

    vText = new Text();
    vText.setStroke(Color.WHITE);
    vText.setTranslateX(7);
    vText.textProperty()
         .bind(rect.heightProperty()
                   .multiply(2048f / size)
                   .asString("%.0f px"));
    vText.translateXProperty()
         .bind(rect.xProperty()
                   .add(rect.widthProperty().subtract(55)));
    vText.translateYProperty()
         .bind(rect.yProperty().add(rect.heightProperty().divide(2)));

    vLine = new Line(x + width, y, x + width, y + height);
    vLine.setStrokeWidth(5);
    vLine.setStroke(Color.WHITE);
    setDragHandlers(vLine, rect, Cursor.H_RESIZE, mouseLocation);

    vLine.startXProperty()
         .bind(rect.xProperty().add(rect.widthProperty()));
    vLine.startYProperty().bind(rect.yProperty());
    vLine.endXProperty()
         .bind(rect.xProperty().add(rect.widthProperty()));
    vLine.endYProperty()
         .bind(rect.yProperty().add(rect.heightProperty()));

    vLine.setOnMouseDragged(event -> {
      if (mouseLocation.value != null)
      {
        double deltaX =
                      event.getSceneX() - mouseLocation.value.getX();
        double newMaxX = rect.getX() + rect.getWidth() + deltaX;
        if (newMaxX >= rect.getX() && newMaxX <= size)
        {
          rect.setWidth(rect.getWidth() + deltaX * 2);
        }
        mouseLocation.value = new Point2D(event.getSceneX(),
                                          event.getSceneY());
      }
    });

    // force controls to live in same parent as rectangle:
    rect.parentProperty().addListener((obs, oldParent, newParent) -> {
      for (Node c : Arrays.asList(hLine, vLine, hText, vText))
      {
        Pane currentParent = (Pane) c.getParent();
        if (currentParent != null)
        {
          currentParent.getChildren().remove(c);
        }
        ((Pane) newParent).getChildren().add(c);
      }
    });

    return rect;
  }

  static class Wrapper<T>
  {
    T value;
  }

  public static void main(final String[] args)
  {
    RunFX.start(new RunFXDemo());
  }
}