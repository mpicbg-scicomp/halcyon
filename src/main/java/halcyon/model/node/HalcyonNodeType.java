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
  /**
   * Name string of HalcyonNode type.
   * 
   * @return the string
   */
  String name();

  /**
   * Gets icon.
   * 
   * @return the icon
   */
  default public Node getIcon()
  {
    String lKey = name().toLowerCase() + ".icon";
    try
    {
      Properties mProperties = new Properties();
      try
      {
        InputStream lResourceAsStream =
                                      this.getClass()
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
      System.err.println("Problem while obtaining icon for key: "
                         + lKey);
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Gets icon from the resource.
   * 
   * @param pPath
   *          the resource path
   * @return the icon
   */
  default Node getIcon(String pPath)
  {
    return getIconPath(this.getClass().getResourceAsStream(pPath));
  }

  /**
   * Gets icon from the resource stream.
   * 
   * @param resourceStream
   *          the resource stream
   * @return the Node
   */
  static Node getIconPath(InputStream resourceStream)
  {
    return new ImageView(new Image(resourceStream));
  }

}
