# ClipCloud - Client Side

## Description
This is the client side of the ClipCloud project.

## Clipboard observer
In this code, we create a ClipboardObserver class that implements the ClipboardOwner interface. We set this class as the clipboard owner by calling clipboard.setContents(new StringSelection(""), this); in the constructor.

The lostOwnership method is called when the ownership of the clipboard is lost. Inside this method, you can access the clipboard's content using the clipboard.getContents(this); method, and then perform actions based on the clipboard content. Finally, we reclaim ownership of the clipboard by setting its contents to an empty string.

The main method initializes the ClipboardObserver and keeps the program running to observe the clipboard. You can run this code, and it will continuously monitor the clipboard for changes and print the clipboard content when it detects a change.

## what does loosing the ownership of the clipboard 
For example, if your application has placed some data in the clipboard and another application comes along and puts different data into the clipboard, your application would lose ownership of the clipboard. At this point, the lostOwnership method would be called, allowing you to respond to the change in the clipboard's content or take any necessary actions.

Losing ownership of the clipboard in the context of the ClipboardOwner interface means that another application or component has taken control of the clipboard's contents. When an application sets itself as the clipboard owner using the setContents method, it can specify a ClipboardOwner that will be notified when the clipboard's ownership is lost. Ownership is typically lost when another application or component sets new data in the clipboard, effectively overwriting the previous contents.

> So it is like the clipboard is owned by our app at first, and when we use it on another app, a listener is called.