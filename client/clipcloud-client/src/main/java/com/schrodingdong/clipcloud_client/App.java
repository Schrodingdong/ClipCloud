package com.schrodingdong.clipcloud_client;

import com.schrodingdong.clipcloud_client.observer.ClipboardObserver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static final String HOME_PATH = System.getProperty("user.home");
    public static final String CLIPCLOUD_PATH = HOME_PATH + "/.clipcloud";
    public static final String ELEMENTS_PATH = CLIPCLOUD_PATH + "/elements";
    public static final String OFFLINE_TEXT_ELEMENTS_FILE = ELEMENTS_PATH+ "/offline_text_elements";
    public static final String OFFLINE_IMAGE_ELEMENTS_FILE = ELEMENTS_PATH+ "/offline_image_elements";
    public static final String OFFLINE_IMAGE_META_ELEMENTS_FILE = ELEMENTS_PATH+ "/offline_image_meta_elements";
    public static final String OFFLINE_FILE_ELEMENTS_FILE = ELEMENTS_PATH+ "/offline_file_elements";
    public static final String OFFLINE_FILE_META_ELEMENTS_FILE = ELEMENTS_PATH+ "/offline_file_meta_elements";

    public static void initFiles(){
        // initialize the needed folders
        try{
            new File(CLIPCLOUD_PATH).mkdir();
            new File(ELEMENTS_PATH).mkdir();
            new File(OFFLINE_IMAGE_ELEMENTS_FILE).mkdir();
            new File(OFFLINE_FILE_ELEMENTS_FILE).mkdir();

            new File(OFFLINE_TEXT_ELEMENTS_FILE).createNewFile();
            new File(OFFLINE_IMAGE_META_ELEMENTS_FILE).createNewFile();
            new File(OFFLINE_FILE_META_ELEMENTS_FILE).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        initFiles();
        // initialize the observer
        ClipboardObserver observer = new ClipboardObserver();
        System.out.println("Clipboard observer started. Press Ctrl+C to exit.");
        while (true) {}
    }

}
