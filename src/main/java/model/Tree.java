package model;

import javafx.scene.Node;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Halcyon Tree
 */
public class Tree extends TreeView< HalcyonNode >
{
	private final Node rootIcon = new ImageView(
			new Image(getClass().getResourceAsStream("/microscope_16.png"))
	);

	private final Node cameraIcon = new ImageView(
			new Image(getClass().getResourceAsStream("/camera_16.png"))
	);

	private final Node laserIcon = new ImageView(
			new Image(getClass().getResourceAsStream("/laser_16.png"))
	);
}
