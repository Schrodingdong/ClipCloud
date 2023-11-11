package com.schrodingdong.clipcloud_client.savers;

import com.schrodingdong.clipcloud_client.App;
import com.schrodingdong.clipcloud_client.clip_elements.ClipBoardElement;
import com.schrodingdong.clipcloud_client.clip_elements.FileClipBoardElement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveClipeBoardFileElement extends SaveClipBoardElement{

    @Override
    protected void saveClipBoardElementToLocal(ClipBoardElement<?> element) {
        System.out.println(">>> Saving File element locally...");
        // save the file in tmp folder
        FileClipBoardElement fileElement = (FileClipBoardElement) element;
        saveFileToTmpFolder(fileElement);

        // save the metadata in the offline file
        saveOfflineMetadataToFile(fileElement);
        System.out.println(">>> Successfully saved locally !");
    }


    private void saveOfflineMetadataToFile(FileClipBoardElement fileELement) {
        List<FileClipBoardElement> elementList;

        try (ObjectInputStream objectInputStream_offlineElements = new ObjectInputStream(new FileInputStream(App.OFFLINE_FILE_META_ELEMENTS_FILE))) {
            // Read the existing list (if any)
            elementList = (List<FileClipBoardElement>) objectInputStream_offlineElements.readObject();
        } catch (EOFException e) {
            // No elements in the file
            elementList = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        elementList.add(fileELement);
        elementList.sort((o1, o2) -> o1.getCreated().compareTo(o2.getCreated()));

        try (ObjectOutputStream objectOutputStream_offlineElements = new ObjectOutputStream(new FileOutputStream(App.OFFLINE_FILE_META_ELEMENTS_FILE))) {
            // Save the updated list
            objectOutputStream_offlineElements.writeObject(elementList);
            objectOutputStream_offlineElements.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveFileToTmpFolder(FileClipBoardElement element) {
        try {
            File newTmpSave = new File(App.OFFLINE_FILE_ELEMENTS_FILE + "/" + element.getFilename());
            FileInputStream fis = new FileInputStream(element.getContent()); // read the file contents
            FileOutputStream fos = new FileOutputStream(newTmpSave);
            fos.write(fis.readAllBytes()); // write it in the tmp thingy
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void synchronizeLocalElementsWithCloud() {
        try (ObjectInputStream objectInputStream_offlineElements = new ObjectInputStream(new FileInputStream(App.OFFLINE_FILE_META_ELEMENTS_FILE))) {
            List<FileClipBoardElement> el = (List<FileClipBoardElement>) objectInputStream_offlineElements.readObject();
            System.out.println(el.toString());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
