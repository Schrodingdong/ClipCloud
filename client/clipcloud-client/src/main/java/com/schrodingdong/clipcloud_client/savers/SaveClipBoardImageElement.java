package com.schrodingdong.clipcloud_client.savers;

import com.schrodingdong.clipcloud_client.App;
import com.schrodingdong.clipcloud_client.clip_elements.ClipBoardElement;
import com.schrodingdong.clipcloud_client.clip_elements.ImageClipBoardElement;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// TODO
public class SaveClipBoardImageElement extends SaveClipBoardElement {
    @Override
    public void saveClipBoardElementToCloud(ClipBoardElement element) {
        // TODO
    }


    @Override
    protected void saveClipBoardElementToLocal(ClipBoardElement<?> element) {
        // save the image in tmp folder
        ImageClipBoardElement imgElement = (ImageClipBoardElement) element;
        saveImageToTmpFolder(imgElement);

        // set the base64 version
        ((ImageClipBoardElement) element).setContentBase64();

        // save the metadata in the offline file
        saveOfflineMetadataToFile(imgElement);
    }

    private void saveOfflineMetadataToFile(ImageClipBoardElement imgElement) {
        List<ImageClipBoardElement> elementList;

        try (ObjectInputStream objectInputStream_offlineElements = new ObjectInputStream(new FileInputStream(App.OFFLINE_IMAGE_META_ELEMENTS_FILE))) {
            // Read the existing list (if any)
            elementList = (List<ImageClipBoardElement>) objectInputStream_offlineElements.readObject();
        } catch (EOFException e) {
            // No elements in the file
            elementList = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        elementList.add(imgElement);
        elementList.sort((o1, o2) -> o1.getCreated().compareTo(o2.getCreated()));

        try (ObjectOutputStream objectOutputStream_offlineElements = new ObjectOutputStream(new FileOutputStream(App.OFFLINE_IMAGE_META_ELEMENTS_FILE))) {
            // Save the updated list
            objectOutputStream_offlineElements.writeObject(elementList);
            objectOutputStream_offlineElements.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveImageToTmpFolder(ImageClipBoardElement element) {
        try {
            ImageIO.write(element.getContent(), "png", new File(element.getTmpPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void synchronizeLocalElementsWithCloud() {
        try (ObjectInputStream objectInputStream_offlineElements = new ObjectInputStream(new FileInputStream(App.OFFLINE_IMAGE_META_ELEMENTS_FILE))) {
            List<ImageClipBoardElement> el = (List<ImageClipBoardElement>) objectInputStream_offlineElements.readObject();
            System.out.println(el.toString());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
