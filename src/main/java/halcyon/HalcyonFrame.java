package halcyon;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import halcyon.controller.ViewManager;
import halcyon.model.collection.HalcyonNodeRepository;
import halcyon.model.collection.ObservableCollection;
import halcyon.model.node.HalcyonNodeInterface;
import halcyon.view.TreePanel;

import org.dockfx.DockNode;
import org.dockfx.DockPane;

/**
 * FxFrame support JavaFX based on docking framework.
 */
public class HalcyonFrame extends Application
{

  final private double mWindowWidth, mWindowHeight;
  final private String mWindowtitle;
  final private String mAppIconPath;
  final private HalcyonNodeRepository mNodes;

  final private ObservableCollection<DockNode> mConsoleDockNodes =
                                                                 new ObservableCollection<>();
  final private ObservableCollection<DockNode> mToolBarDockNodes =
                                                                 new ObservableCollection<>();

  private TreePanel mTreePanel;

  private ViewManager mViewManager;

  private Stage mPrimaryStage;

  /**
   * Gets the ViewManager.
   *
   * @return the ViewManager
   */
  public ViewManager getViewManager()
  {
    return mViewManager;
  }

  /**
   * Instantiates a new Halcyon frame.
   *
   * @param pWindowTitle
   *          window title
   * @param pAppIconPath
   *          the app icon path
   * @param pWindowWidth
   *          the window width
   * @param pWindowHeight
   *          the window height
   */

  public HalcyonFrame(String pWindowTitle,
                      String pAppIconPath,
                      int pWindowWidth,
                      int pWindowHeight)
  {
    mWindowtitle = pWindowTitle;
    mAppIconPath = pAppIconPath;
    mWindowWidth = pWindowWidth;
    mWindowHeight = pWindowHeight;
    mNodes = new HalcyonNodeRepository();
  }

  /**
   * Instantiates a new Halcyon frame.
   *
   * @param pWindowTitle
   *          window title
   * @param pWindowWidth
   *          the window width
   * @param pWindowHeight
   *          the window height
   */
  public HalcyonFrame(String pWindowTitle,
                      int pWindowWidth,
                      int pWindowHeight)
  {
    this(pWindowTitle, null, pWindowWidth, pWindowHeight);
  }

  /**
   * Instantiates a new Halcyon frame. the Halcyon window occupies the entire
   * screen.
   *
   * @param pWindowTitle
   *          window title
   */
  public HalcyonFrame(String pWindowTitle)
  {
    this(pWindowTitle,
         (int) Screen.getPrimary().getVisualBounds().getWidth() - 50,
         (int) Screen.getPrimary()
                     .getVisualBounds()
                     .getHeight() - 30);
  }

  /**
   * Sets tree panel.
   *
   * @param pTreePanel
   *          the Tree Panel
   */
  public void setTreePanel(TreePanel pTreePanel)
  {
    mTreePanel = pTreePanel;
    mTreePanel.setNodes(mNodes);
  }

  /**
   * Add a Halcyon node.
   *
   * @param node
   *          the node
   */
  public void addNode(HalcyonNodeInterface node)
  {
    mNodes.add(node);
  }

  /**
   * Add a toolbar.
   *
   * @param toolbar
   *          the toolbar
   */
  public void addToolbar(DockNode toolbar)
  {
    mToolBarDockNodes.add(toolbar);
  }

  /**
   * Add a console.
   *
   * @param console
   *          the console
   */
  public void addConsole(DockNode console)
  {
    mConsoleDockNodes.add(console);
  }

  /**
   * Starts JavaFX application.
   *
   * @param pPrimaryStage
   *          the p primary stage
   */
  @Override
  public void start(Stage pPrimaryStage)
  {

    if (Platform.isFxApplicationThread())
      internalStart(pPrimaryStage);
    else
      Platform.runLater(() -> {
        internalStart(pPrimaryStage);
      });

  }

  double initX;
  double initY;

  private void internalStart(Stage pPrimaryStage)
  {
    mPrimaryStage = pPrimaryStage;

    String lLayoutFile = getUserDataDirectory(mWindowtitle)
                         + "layout.pref";
    String lOtherLayoutFile = getUserDataDirectory(mWindowtitle)
                              + "others.pref";

    // create a dock pane that will manage our dock mNodes and handle the layout
    DockPane lDockPane = new DockPane();

    Menu lToolbarMenu = new Menu("Toolbar");
    Menu lConsoleMenu = new Menu("Console");

    // Save preference menu item
    MenuItem lSaveMenuItem = new MenuItem("Save");
    lSaveMenuItem.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {

        lDockPane.storePreference(lLayoutFile);

        // save the additional preferences for HalcyonOtherNode size/position
        mViewManager.storeOtherNodePreference(lOtherLayoutFile);
      }
    });

    // Restore preference menu item
    MenuItem lRestoreMenuItem = new MenuItem("Restore");
    lRestoreMenuItem.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        lDockPane.loadPreference(lLayoutFile,
                                 nodeName -> mViewManager.restore(mNodes.getNode(nodeName)));

        // load the additional preferences for HalcyonOtherNode size/position
        mViewManager.loadOtherNodePreference(lOtherLayoutFile);
      }
    });

    // Check the folder for Auto save/load layout file
    dirExist(getUserDataDirectory(mWindowtitle));

    String lAutoLayoutFile = getUserDataDirectory(mWindowtitle)
                             + ".auto";
    CheckMenuItem lAutoLayoutMenuItem = new CheckMenuItem("Auto");

    lAutoLayoutMenuItem.setOnAction(event -> {
      if (lAutoLayoutMenuItem.isSelected())
      {
        // create auto file
        try
        {
          new File(lAutoLayoutFile).createNewFile();
        }
        catch (IOException e)
        {
          System.err.println(e.toString());
          // e.printStackTrace();
        }

        mPrimaryStage.setOnCloseRequest(closeEvent -> lSaveMenuItem.fire());
      }
      else
      {
        // delete the auto file
        new File(lAutoLayoutFile).delete();

        mPrimaryStage.setOnCloseRequest(null);
      }
    });

    // Reset menu item
    MenuItem lResetMenuItem = new MenuItem("Reset");
    lResetMenuItem.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        // Reset the layout of the view
        mPrimaryStage.sizeToScene();
        mPrimaryStage.setX(initX);
        mPrimaryStage.setY(initY);

        for (int i = 0; i < mNodes.getNodeCount(); i++)
        {
          mViewManager.close(mNodes.getNode(i));
        }
      }
    });

    Menu lLayoutMenu = new Menu("Layout");
    lLayoutMenu.getItems().addAll(lSaveMenuItem,
                                  lRestoreMenuItem,
                                  lAutoLayoutMenuItem,
                                  lResetMenuItem);

    Menu lViewMenu = new Menu("View");
    lViewMenu.getItems().addAll(lToolbarMenu,
                                lConsoleMenu,
                                lLayoutMenu);

    MenuBar lMenuBar = new MenuBar(lToolbarMenu,
                                   lConsoleMenu,
                                   lLayoutMenu);

    mViewManager = new ViewManager(lDockPane,
                                   mTreePanel,
                                   mNodes,
                                   mConsoleDockNodes,
                                   mToolBarDockNodes,
                                   lViewMenu,
                                   mAppIconPath);

    mTreePanel.setViewManager(mViewManager);

    BorderPane lBorderPane = new BorderPane();
    lBorderPane.setTop(lMenuBar);
    lBorderPane.setCenter(lDockPane);
    Scene lScene =
                 new Scene(lBorderPane, mWindowWidth, mWindowHeight);

    mPrimaryStage.setScene(lScene);
    mPrimaryStage.sizeToScene();

    mPrimaryStage.show();

    initX = mPrimaryStage.getX();
    initY = mPrimaryStage.getY();
    // System.out.println(lScene.getWindow());

    // According to the file, enable the AutoLayoutMenuItem
    if (new File(lAutoLayoutFile).exists())
    {
      lAutoLayoutMenuItem.setSelected(true);

      lRestoreMenuItem.fire();

      mPrimaryStage.setOnCloseRequest(closeEvent -> lSaveMenuItem.fire());
    }
    else
    {
      lAutoLayoutMenuItem.setSelected(false);
    }

    // test the look and feel with both Caspian and Modena
    Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);

    // initialize the default styles for the dock pane and undocked nodes using
    // the DockFX
    // library's internal Default.css stylesheet
    // unlike other custom control libraries this allows the user to override
    // them globally
    // using the style manager just as they can with internal JavaFX controls
    // this must be called after the primary stage is shown
    // https://bugs.openjdk.java.net/browse/JDK-8132900
    DockPane.initializeDefaultUserAgentStylesheet();
  }

  /**
   * External starts from Java swing environment.
   */
  public void externalStart()
  {
    HalcyonFrame lThis = this;
    Platform.runLater(() -> {
      Stage stage = new Stage();
      stage.setTitle(mWindowtitle);
      try
      {
        lThis.start(stage);
      }
      catch (Throwable e)
      {
        e.printStackTrace();
      }
    });

  }

  /**
   * External stops.
   */
  public void externalStop()
  {
    HalcyonFrame lThis = this;
    Platform.runLater(() -> {
      try
      {
        lThis.stop();
      }
      catch (Throwable e)
      {
        e.printStackTrace();
      }
    });
  }

  /**
   * Is visible boolean.
   *
   * @return the boolean
   */
  public boolean isVisible()
  {
    return mPrimaryStage == null ? false : mPrimaryStage.isShowing();
  }

  public static String getUserDataDirectory(String appTitle)
  {
    if (appTitle != null && !appTitle.isEmpty())
      return System.getProperty("user.home") + File.separator
             + ".halcyon"
             + File.separator
             + appTitle
             + File.separator
             + getApplicationVersionString()
             + File.separator;
    else
      return System.getProperty("user.home") + File.separator
             + ".halcyon"
             + File.separator
             + getApplicationVersionString()
             + File.separator;
  }

  public static String getApplicationVersionString()
  {
    return "1.0";
  }

  public static boolean dirExist(String dir)
  {
    return new File(dir).exists() || new File(dir).mkdirs();
  }
}
