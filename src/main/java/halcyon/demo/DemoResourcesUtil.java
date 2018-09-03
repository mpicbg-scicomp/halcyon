package halcyon.demo;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Resource accessor
 */
public class DemoResourcesUtil
{
  // Load the bundles
  private static final ResourceBundle strings =
                                              ResourceBundle.getBundle("halcyon.demo.images.Strings");

  private static final String VERSION = getString("build.date");

  /**
   * Return string property value.
   * 
   * @param key
   *          to find the String value
   * @return a String corresponding to the given key.
   */
  public static String getString(String key)
  {
    try
    {
      return strings.getString(key);
    }
    catch (MissingResourceException e)
    {
      return key;
    }
  }
}
