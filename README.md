# Halcyon

Halcyon provides basic interfaces which are ```Console``` and ```TreePanel```.
*ConsolePane* is linked with standard out and standard error streams. ```TreePanel``` contains all other HalcyonNode instances.

This framework consists of model and view/controller.

## Class - ```halcyon.HalcyonFrame```
Halcyon holds normally three basic types of nodes, which are Console, ToolBar and TreePanel. ```HalcyonFrame``` is the entry point to create GUI based on JavaFX stage. Console is used as console output/error as well as logging printing panel. ToolBar is used for providing control panels where user can operate tasks. TreePanel is a kind of hub to manage all the other mission specific jobs. For example, we can use this as DeviceTree where multiple devices appear in a tree structure. Each device is shown by clicking the specific tree node and controlled in the device panel.

Halcyon is based MVC pattern where Model/View/Controller are separated and linked by EventHandlers.

Scrrenshot
--
![Screenshot](https://github.com/ClearControl/Halcyon/blob/master/artwork/HalcyonCapture.png?raw=true)

## Package - ```halcyon.model```

### Package - ```halcyon.model.node```
1. HalcyonNode - JavaFX based node
1. HalcyonSwingNode - Swing based node
1. HalcyonOtherNode - neither JavaFX nor Swing based (e.g. OpenGL window)

### Package - ```halcyon.model.collection```
1. HalcyonNodeRepository - Halcyon node collection

### Package - ```halcyon.model.property```
1. NodeProperty - NodeProperty holds a ```javafx.scene.Node``` instance in HalcyonNode.

## Package - ```halcyon.view```
1. HalcyonPanel - contains HalcyonNode.
1. ConsolePanel - used for Console. 
1. TreePanel - a panel holds multiple HalcyonPanel containing one of HalcyonNode in a tree structure. When clicking specific node, the corresponding HalcyonNode opens an appropriate GUI component.

### Package - ```halcyon.view.demo```
1. DemoHalcyonMain - is an entry point of the demo application.
1. DemoHalcyonNodeType - has a custom-made HalcyonType enumeration. This will be used for mapping the icons in the TreePanel.
1. DemoResourceUtil - is a resource access utility only for demonstration. It contains where the icon files are located.
1. DemoToolbarPanel - contains a user-defined Toolbar which will be located in Toolbar Area in the Pane.

## Package - ```halcyon.controller```
1. ViewManager - a controller for coordinating view and model according to user interactions.

## Package -  ```halcyon.util```
1. RunFX - can run all ```RunnableFX``` the inherited class.
1. RunnableFX - is an interface which can be run by ```RunFX```.
