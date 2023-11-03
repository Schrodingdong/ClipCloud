package com.schrodingdong.clipcloud_client;

import com.schrodingdong.clipcloud_client.observer.ClipboardObserver;

import java.io.File;

public class App {
    public static final String HOME_PATH = System.getProperty("user.home");
    public static final String CLIPCLOUD_PATH = HOME_PATH + "/.clipcloud";
    public static final String ELEMENTS_PATH = CLIPCLOUD_PATH + "/elements";
    public static final String OFFLINE_ELEMENTS_FILE = CLIPCLOUD_PATH + "/offline_elements";
    public static final String FIRST_OFFLINE_ELEMENT_OF_SESSION_FILE = CLIPCLOUD_PATH + "/first_offline_element_of_session";

    public static void main(String[] args) {
        // initialize the needed folders
        new File(CLIPCLOUD_PATH).mkdir();
        new File(ELEMENTS_PATH).mkdir();

        // initialize the observer
        ClipboardObserver observer = new ClipboardObserver();

        // startup message !
        System.out.println("Clipboard observer started. Press Ctrl+C to exit.");

        while (true) {}
//        System.out.println(">> user thingy :: "+  System.getProperty("user.home"));
    }
}
