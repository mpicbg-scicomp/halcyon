package demo.tree;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TreeViewSample extends Application {

	private final Node rootIcon = new ImageView(
			new Image(getClass().getResourceAsStream("/microscope_16.png"))
	);

	private final Node cameraIcon = new ImageView(
			new Image(getClass().getResourceAsStream("/camera_16.png"))
	);

	private final Node laserIcon = new ImageView(
			new Image(getClass().getResourceAsStream("/laser_16.png"))
	);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Tree View Sample");

		TreeItem<String> rootItem = new TreeItem<> ("Microscopy (7)", rootIcon);
		rootItem.setExpanded(true);

		TreeItem<String> instrument = new TreeItem<>( "Camera (5)", cameraIcon );
		for (int i = 1; i < 6; i++) {
			TreeItem<String> item = new TreeItem<> ("Camera-" + i);
			instrument.getChildren().add(item);
		}

		rootItem.getChildren().add( instrument );

		instrument = new TreeItem<>( "Laser (2)", laserIcon );
		for (int i = 1; i < 3; i++) {
			TreeItem<String> item = new TreeItem<> ("Laser-" + i);
			instrument.getChildren().add(item);
		}

		rootItem.getChildren().add( instrument );

		TreeView<String> tree = new TreeView<> (rootItem);

		StackPane root = new StackPane();
		root.getChildren().add(tree);

		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
	}
}
