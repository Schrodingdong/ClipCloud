package com.schrodingdong.clipcloud_client.cloud_elements;


import com.schrodingdong.clipcloud_client.elements.ClipBoardElement;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This class is responsible for saving the clipboard element to the cloud.
 * In case where there is a connection failure, we will have to save the clipboard element to the local storage.
 */
public abstract class SaveClipBoardElement implements Serializable {

    public void saveClipBoardElement(ClipBoardElement element) {
        if(isNetworkConnected()){
            System.out.println(">> Network is connected");
            saveClipBoardElementToCloud(element);
        } else {
            System.out.println(">> Network is not connected");
            saveClipBoardElementToLocal(element);
        }
    }

    protected abstract void saveClipBoardElementToCloud(ClipBoardElement element);
    protected abstract void saveClipBoardElementToLocal(ClipBoardElement element);
    private boolean isNetworkConnected() {
        try {
            // You can use a well-known host like google.com or any other reliable host
            InetAddress address = InetAddress.getByName("www.google.com");

            // Try to establish a socket connection to the host on a specific port (80 for HTTP)
            Socket socket = new Socket(address, 80);

            // Close the socket if the connection is successful
            socket.close();

            // If the connection was successful, return true
            return true;
        } catch (Exception e) {
            // If an exception is thrown, it means there's no network connection
            return false;
        }
    }

}
