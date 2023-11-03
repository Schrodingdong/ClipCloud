package com.schrodingdong.clipcloud_client.observer;

import com.schrodingdong.clipcloud_client.cloud_elements.SaveClipBoardElement;
import com.schrodingdong.clipcloud_client.cloud_elements.SaveClipBoardImageElement;
import com.schrodingdong.clipcloud_client.cloud_elements.SaveClipBoardTextElement;
import com.schrodingdong.clipcloud_client.cloud_elements.SaveClipeBoardFileElement;
import com.schrodingdong.clipcloud_client.elements.ClipBoardElement;
import com.schrodingdong.clipcloud_client.elements.FileClipBoardElement;
import com.schrodingdong.clipcloud_client.elements.ImageClipBoardElement;
import com.schrodingdong.clipcloud_client.elements.TextClipBoardElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClipboardObserver implements ClipboardOwner {
    private Transferable transferable;
    private SaveClipBoardElement saveClipBoardElement;

    public ClipboardObserver() {
        // Get the system clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // Set this class as the clipboard owner
        clipboard.setContents(new StringSelection(""), this);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        try {
            // Get the clipboard content
            // with the Thread.Sleep(), we will be retrieving the latest clipboard element
            // the use of the Thread.sleep() is to make sure that the clipboard is not being used by another process
            // TODO : get the minimum time required to wait for the clipboard to be available
            Thread.sleep(2000);
            transferable = clipboard.getContents(this);

            //Try catch to know the type, and make the needed saving procedures
            try{
                // get the clip content
                String clipboardText = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                // format it into a class
                ClipBoardElement<String> clipBoardElement = new TextClipBoardElement(clipboardText);
                // save it
                saveClipBoardElement = new SaveClipBoardTextElement();
                System.out.println(
                        clipBoardElement.getContent()
                );
//                saveClipBoardElement.saveClipBoardElement(clipBoardElement);
            } catch (Exception e) {
                System.err.println("not a string");
            }
            try{
                BufferedImage clipboardImage = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
                // format the data
                ClipBoardElement<BufferedImage> clipBoardElement = new ImageClipBoardElement(clipboardImage);
                // save it
                saveClipBoardElement = new SaveClipBoardImageElement();
//                saveClipBoardElement.saveClipBoardElement(clipBoardElement);
            } catch (Exception e) {
                System.err.println("not an image");
            }
            try{
                List<File> clipboardFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                for (File file : clipboardFiles) {
                    // format the data
                    ClipBoardElement<File> clipBoardElement = new FileClipBoardElement(file);
                    // save it
                    saveClipBoardElement = new SaveClipeBoardFileElement();
//                    saveClipBoardElement.saveClipBoardElement(clipBoardElement);
                } // try a batch saving ??
            } catch (Exception e) {
                System.err.println("not a file");
            }
            // reclaim ownership of the clipboard
            clipboard.setContents(new StringSelection(""), this);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
