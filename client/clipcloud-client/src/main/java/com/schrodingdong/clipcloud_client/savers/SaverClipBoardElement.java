package com.schrodingdong.clipcloud_client.savers;


import com.schrodingdong.clipcloud_client.api_client.ApiClient;
import com.schrodingdong.clipcloud_client.authentication.AbstractAuthenticator;
import com.schrodingdong.clipcloud_client.authentication.AuthenticationException;
import com.schrodingdong.clipcloud_client.authentication.AwsAuthenticator;
import com.schrodingdong.clipcloud_client.authentication.RequestException;
import com.schrodingdong.clipcloud_client.clip_elements.ClipBoardElement;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpRequest;

/**
 * This class is responsible for saving the clipboard element to the cloud.
 * In case where there is a connection failure, we will have to save the clipboard element to the local storage.
 */
public abstract class SaverClipBoardElement {
    public static final String SAVE_ELEMENT_URL = "https://9yfjdn60m7.execute-api.eu-west-2.amazonaws.com/dev/save-clip-element";
    public static final URI SAVE_ELEMENT_URI = URI.create(SAVE_ELEMENT_URL);
    protected ObjectOutputStream objectOutputStream_offlineElements;
    protected ObjectInputStream objectInputStream_offlineElements;

    public void saveClipBoardElement(ClipBoardElement<?> element) {
        saveClipBoardElementToLocal(element);
//        synchronizeLocalElementsWithCloud();
//        try{
//            saveClipBoardElementToCloud(element);
//        } catch (AuthenticationException | RequestException e){
//            System.err.println(e.getMessage());
//        }
    }

    protected void saveClipBoardElementToCloud(ClipBoardElement<?> element) throws AuthenticationException, RequestException{
        System.out.println(">>> Saving Text element to cloud ...");
        // check access token :
        if (AwsAuthenticator.getAccessToken() == null)
            throw new AuthenticationException("Access token is null");

        // get the jsonObject of the element
        String json = element.getJson();

        // create request
        HttpRequest saveRequest = HttpRequest.newBuilder(SAVE_ELEMENT_URI)
                .header("Authorization", AbstractAuthenticator.getAccessToken())
                .header("Content-type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // send request
        ApiClient.getInstance().sendRequest(saveRequest);
        System.out.println(">>> Successfully saved in cloud!");
    }
    protected abstract void saveClipBoardElementToLocal(ClipBoardElement<?> element);
    protected abstract void synchronizeLocalElementsWithCloud();
    private boolean isNetworkConnected() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            Socket socket = new Socket(address, 80);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
