package com.schrodingdong.clipcloud_client.savers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schrodingdong.clipcloud_client.App;
import com.schrodingdong.clipcloud_client.authentication.AbstractAuthenticator;
import com.schrodingdong.clipcloud_client.clip_elements.ClipBoardElement;
import com.schrodingdong.clipcloud_client.clip_elements.TextClipBoardElement;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SaveClipBoardTextElement extends SaveClipBoardElement {
    @Override
    public void saveClipBoardElementToCloud(ClipBoardElement<?> element) {
        // get the jsonObject of the element
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(element);
        } catch (JsonProcessingException e) {
            System.out.println("SaveClipBoardTextElement.saveClipBoardElementToCloud >> error mapping to json");
            throw new RuntimeException(e);
        }
        // init client
        HttpClient httpClient = HttpClient.newHttpClient();

        // create request
        HttpRequest saveRequest = HttpRequest.newBuilder(SAVE_ELEMENT_URI)
                .header("Authorization", AbstractAuthenticator.getAccessToken())
                .header("Content-type","application/json")
                .header("Content-length", Integer.toString(json.length()))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // send request
        try {
            HttpResponse<String> response = httpClient.send(saveRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(">>> response body : \n\t" + response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void saveClipBoardElementToLocal(ClipBoardElement<?> element) {
        System.out.println("Saving element to local storage : \n\t >>"+element.toString());
        List<ClipBoardElement<?>> elementList;

        try (ObjectInputStream objectInputStream_offlineElements = new ObjectInputStream(new FileInputStream(App.OFFLINE_TEXT_ELEMENTS_FILE))) {
            // Read the existing list (if any)
            elementList = (List<ClipBoardElement<?>>) objectInputStream_offlineElements.readObject();
        } catch (EOFException e) {
            // No elements in the file
            elementList = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Add the new element to the list
        elementList.add(element);
        // order the list by date
        // so we can go through the list and send the elements in the right order when we synchronize
        elementList.sort((o1, o2) -> o1.getCreated().compareTo(o2.getCreated()));

        try (ObjectOutputStream objectOutputStream_offlineElements = new ObjectOutputStream(new FileOutputStream(App.OFFLINE_TEXT_ELEMENTS_FILE))) {
            // Save the updated list
            objectOutputStream_offlineElements.writeObject(elementList);
            objectOutputStream_offlineElements.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void synchronizeLocalElementsWithCloud() {
        try (ObjectInputStream objectInputStream_offlineElements = new ObjectInputStream(new FileInputStream(App.OFFLINE_TEXT_ELEMENTS_FILE))) {
            List<TextClipBoardElement> el = (List<TextClipBoardElement>) objectInputStream_offlineElements.readObject();
            System.out.println(el.toString());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
