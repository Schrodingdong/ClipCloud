package com.schrodingdong.clipcloud_client.observer;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ClipboardObserver implements ClipboardOwner {

    public ClipboardObserver() {
        // Get the system clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // Set this class as the clipboard owner
        clipboard.setContents(new StringSelection(""), this);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // This method is called when the ownership of the clipboard is lost
        // You can add your code to monitor the clipboard here
        try {
            // Get the clipboard content
            Transferable transferable = clipboard.getContents(this);
            String clipboardContent = (String) transferable.getTransferData(DataFlavor.stringFlavor);

            // Perform actions based on the clipboard content
            System.out.println("Clipboard content: " + clipboardContent);

            // Reclaim ownership of the clipboard
            clipboard.setContents(new StringSelection(""), this);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        } finally {
            // Reclaim ownership of the clipboard
            clipboard.setContents(new StringSelection(""), this);
        }
    }


}
