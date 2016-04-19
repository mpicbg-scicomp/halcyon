package halcyon.model.node;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * HalcyonNode Type enumeration
 */
public interface HalcyonNodeType
{
	String name();

	default public Node getIcon()
	{
		String lKey = name().toLowerCase() + ".icon";
		try
		{
			Properties mProperties = new Properties();
			try
			{
				InputStream lResourceAsStream = this.getClass()
																						.getResourceAsStream("icons/IconMap.properties");
				mProperties.load(lResourceAsStream);
				lResourceAsStream.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			String lProperty = mProperties.getProperty(lKey);
			if (lProperty == null)
			{
				System.err.println("Cannot find property for key: " + lKey);
				return null;
			}

			Node lIcon = getIcon(lProperty);

			if (lIcon == null)
			{
				System.err.println("Cannot find icon for key: " + lProperty);
				return null;
			}

			return lIcon;
		}
		catch (Throwable e)
		{
			System.err.println("Problem while obtaining icon for key: " + lKey);
			e.printStackTrace();
			return null;
		}
	}

	default Node getIcon(String pPath)
	{
		return getIconPath(this.getClass().getResourceAsStream(pPath));
	}

	static Node getIconPath(InputStream resourceStream)
	{
		return new ImageView(new Image(resourceStream));
	}

}
