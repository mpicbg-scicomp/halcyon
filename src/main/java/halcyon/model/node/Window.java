package halcyon.model.node;

/**
 * Created by moon on 8/29/16.
 */
public interface Window
{
  // Getter/setter the size
  public abstract int getWidth();

  public abstract int getHeight();

  public abstract void setSize(int width, int height);

  // Getter/setter the position
  public abstract int getX();

  public abstract int getY();

  public abstract void setPosition(int x, int y);

  // Operations
  public abstract void show();

  public abstract void hide();

  public abstract void close();
}
