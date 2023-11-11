package com.schrodingdong.clipcloud_client.observer;

import com.schrodingdong.clipcloud_client.savers.SaverClipBoardElement;
import com.schrodingdong.clipcloud_client.savers.SaverClipBoardImageElement;
import com.schrodingdong.clipcloud_client.savers.SaverClipBoardTextElement;
import com.schrodingdong.clipcloud_client.savers.SaverClipeBoardFileElement;
import com.schrodingdong.clipcloud_client.clip_elements.ClipBoardElement;
import com.schrodingdong.clipcloud_client.clip_elements.FileClipBoardElement;
import com.schrodingdong.clipcloud_client.clip_elements.ImageClipBoardElement;
import com.schrodingdong.clipcloud_client.clip_elements.TextClipBoardElement;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ClipboardObserver implements ClipboardOwner {
    private Transferable transferable;
    private SaverClipBoardElement saverClipBoardElement;
    private static final List<DataFlavor> supportedDataFlavors = List.of(
            DataFlavor.stringFlavor,
            DataFlavor.imageFlavor,
            DataFlavor.javaFileListFlavor
    );

    public ClipboardObserver() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // Get the system clipboard
        clipboard.setContents(new StringSelection(""), this); // Set this class as the clipboard owner
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // init runnable
        Runnable lostOwnerShipHandler = new LostOwnerShipHandler();
        ((LostOwnerShipHandler) lostOwnerShipHandler).setClipboard(clipboard);
        ((LostOwnerShipHandler) lostOwnerShipHandler).setContents(contents);
        ((LostOwnerShipHandler) lostOwnerShipHandler).setInstance(this);

        // start the thread
        Thread lostOwnerShipHandlerThread = new Thread(lostOwnerShipHandler);
        lostOwnerShipHandlerThread.start();
        System.out.println(">>>>> Thread started");

        // reclaim ownership of the clipboard
        clipboard.setContents(new StringSelection(""), this);
    }

}

class LostOwnerShipHandler implements Runnable{
    private Transferable transferable;
    private SaverClipBoardElement saverClipBoardElement;
    private Clipboard clipboard;
    private Transferable contents;
    private ClipboardObserver instance;

    @Override
    public void run() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        transferable = clipboard.getContents(instance);

        //Try catch to know the type, and make the needed saving procedures
        textCheckAndSave();
        imageCheckAndSave();
        fileCheckAndSave();



    }

    public void setClipboard(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    public void setContents(Transferable contents) {
        this.contents = contents;
    }
    public void setInstance(ClipboardObserver instance) {
        this.instance = instance;
    }

    private void fileCheckAndSave() {
        try{
            List<File> clipboardFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
            for (File file : clipboardFiles) {
                // format the data
                ClipBoardElement<File> clipBoardElement = new FileClipBoardElement(file, file.getAbsolutePath());
                // save it
                saverClipBoardElement = new SaverClipeBoardFileElement();
                saverClipBoardElement.saveClipBoardElement(clipBoardElement);
            } // try a batch saving ??
        } catch (Exception e) {
            System.err.println(">> clipboard element is : not a file");
        }
    }

    private void imageCheckAndSave() {
        try{
            BufferedImage clipboardImage = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
            // format the data
            ClipBoardElement<BufferedImage> clipBoardElement = new ImageClipBoardElement(clipboardImage);
            // save it
            saverClipBoardElement = new SaverClipBoardImageElement();
            saverClipBoardElement.saveClipBoardElement(clipBoardElement);
        } catch (Exception e) {
            System.err.println(">> clipboard element is : not an image");
        }
    }

    private void textCheckAndSave() {
        try{
            // get the clip content
            String clipboardText = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            // format it into a class
            ClipBoardElement<String> clipBoardElement = new TextClipBoardElement(clipboardText);
            // save it
            saverClipBoardElement = new SaverClipBoardTextElement();
            saverClipBoardElement.saveClipBoardElement(clipBoardElement);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(">> clipboard element is : not a string");
        }
    }
}