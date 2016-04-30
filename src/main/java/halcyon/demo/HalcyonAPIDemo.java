package halcyon.demo;

import halcyon.HalcyonFrame;
import halcyon.model.node.HalcyonNode;
import halcyon.model.node.HalcyonNodeType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import halcyon.view.TreePanel;
import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.junit.Test;

public class HalcyonAPIDemo extends Application
{

	@Override
	public void start(Stage pPrimaryStage) throws Exception
	{
		// TODO: support other type of devices
		final String lRootIconPath = ResourcesUtil.getString("root.icon");

		ArrayList<HalcyonNodeType> lNodeTypeList = new ArrayList<HalcyonNodeType>();
		for (HalcyonNodeType lHalcyonNodeType : HalcyonNodeTypeExample.values())
			lNodeTypeList.add(lHalcyonNodeType);

		final HalcyonFrame lHalcyonFrame = new HalcyonFrame(new TreePanel("Config",
																																			"Test Microscopy",
																																			getClass().getResourceAsStream(lRootIconPath),
																																			lNodeTypeList));

		final HalcyonNode lLaser1 = HalcyonNode.wrap(	"Laser-1",
																									HalcyonNodeTypeExample.ONE,
																									new VBox());

		final HalcyonNode lLaser2 = HalcyonNode.wrap(	"Laser-2",
																									HalcyonNodeTypeExample.ONE,
																									new VBox());

		final HalcyonNode lCamera = HalcyonNode.wrap(	"Camera-1",
																									HalcyonNodeTypeExample.TWO,
																									new VBox());

		final HalcyonNode lStage1 = HalcyonNode.wrap(	"Stage-1",
																									HalcyonNodeTypeExample.THREE,
																									new VBox());

		lHalcyonFrame.addNode(lLaser1);
		lHalcyonFrame.addNode(lLaser2);
		lHalcyonFrame.addNode(lCamera);
		lHalcyonFrame.addNode(lStage1);

		lHalcyonFrame.addToolbar(new DemoToolbarWindow());

		pPrimaryStage.setOnCloseRequest(event -> System.exit(0));

		lHalcyonFrame.start(pPrimaryStage);
	}

	public static void main(String[] args) throws Exception
	{
		launch(args);
	}
}
