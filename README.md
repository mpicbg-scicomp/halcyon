# Halcyon

Halcyon provides basic interfaces which are ```Console``` and ```TreePanel```.
*ConsolePane* is linked with standard out and standard error streams. ```TreePanel``` contains all other HalcyonNode instances.

This framework consists of model and view/controller.

## ```halcyon.HalcyonFrame``` class
Halcyon holds normally three basic types of nodes, which are Console, ToolBar and TreePanel. ```HalcyonFrame``` is the entry point to create GUI based on JavaFX stage. Console is used as console output/error as well as logging printing panel. ToolBar is used for providing control panels where user can operate tasks. TreePanel is a kind of hub to manage all the other mission specific jobs. For example, we can use this as DeviceTree where multiple devices appear in a tree structure. Each device is shown by clicking the specific tree node and controlled in the device panel.

Scrrenshot
--
![Screenshot](https://github.com/ClearControl/Halcyon/blob/master/artwork/HalcyonCapture.png?raw=true)

## ```halcyon.model``` package
This package has all the data structures for Halcyon.

### halcyon.model.node
1. HalcyonNode - JavaFX based node
1. HalcyonSwingNode - Swing based node
1. HalcyonOtherNode - neither JavaFX nor Swing based (e.g. OpenGL window)

### halcyon.model.list
1. HalcyonNodeRepository - Halcyon node collection

## ```halcyon.view``` package
1. HalcyonNodeDockable - used either ConsoleDockNode or ToolbarDockNode
1. TreeDockNode - an item class in TreePanel. 
1. TreePanel - a view component holds multiple TreeDockNode containing one of HalcyonNode. When clicking specific node, the corresponding HalcyonNode opens GUI component.
1. ViewManager - a controller for managing user interactions.



Playground for RTlib main GUI engine - work in progress
