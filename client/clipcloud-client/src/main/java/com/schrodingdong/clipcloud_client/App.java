package com.schrodingdong.clipcloud_client;

import com.schrodingdong.clipcloud_client.observer.ClipboardObserver;

import java.io.*;

public class App {
    public static final String HOME_PATH = System.getProperty("user.home");
    public static final String CLIPCLOUD_PATH = HOME_PATH + "/.clipcloud";
    public static final String ELEMENTS_PATH = CLIPCLOUD_PATH + "/elements";
    public static final String OFFLINE_ELEMENTS_FILE = ELEMENTS_PATH+ "/offline_elements";
    private static ObjectOutputStream objectOutputStream_offlineElements;
    private static ObjectInputStream objectInputStream_offlineElements;

    public static void main(String[] args) {
        // initialize the needed folders
        new File(CLIPCLOUD_PATH).mkdir();
        new File(ELEMENTS_PATH).mkdir();


        // initialize the observer
        ClipboardObserver observer = new ClipboardObserver();
        System.out.println("Clipboard observer started. Press Ctrl+C to exit.");

        while (true) {}
    }
}
