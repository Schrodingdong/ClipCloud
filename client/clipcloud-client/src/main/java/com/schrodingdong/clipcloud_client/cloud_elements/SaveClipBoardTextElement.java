package com.schrodingdong.clipcloud_client.cloud_elements;

import com.schrodingdong.clipcloud_client.App;
import com.schrodingdong.clipcloud_client.elements.ClipBoardElement;
import com.schrodingdong.clipcloud_client.elements.TextClipBoardElement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveClipBoardTextElement extends SaveClipBoardElement {
    @Override
    public void saveClipBoardElementToCloud(ClipBoardElement<?> element) {
        // TODO
    }
    @Override
    public void saveClipBoardElementToLocal(ClipBoardElement<?> element) {
        List<ClipBoardElement<?>> elementList;

        try (ObjectInputStream objectInputStream_offlineElements = new ObjectInputStream(new FileInputStream(App.OFFLINE_ELEMENTS_FILE))) {
            // Read the existing list (if any)
            elementList = (List<ClipBoardElement<?>>) objectInputStream_offlineElements.readObject();
            System.out.println(elementList.toString());
        } catch (EOFException e) {
            // No elements in the file
            elementList = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Add the new element to the list
        elementList.add(element);

        try (ObjectOutputStream objectOutputStream_offlineElements = new ObjectOutputStream(new FileOutputStream(App.OFFLINE_ELEMENTS_FILE))) {
            // Save the updated list
            objectOutputStream_offlineElements.writeObject(elementList);
            objectOutputStream_offlineElements.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void synchronizeLocalElementsWithCloud() {
        try (ObjectInputStream objectInputStream_offlineElements = new ObjectInputStream(new FileInputStream(App.OFFLINE_ELEMENTS_FILE))) {
            List<TextClipBoardElement> el = (List<TextClipBoardElement>) objectInputStream_offlineElements.readObject();
            System.out.println(el.toString());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
