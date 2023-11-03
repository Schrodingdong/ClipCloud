package com.schrodingdong.clipcloud_client;

import com.schrodingdong.clipcloud_client.observer.ClipboardObserver;

public class App {
    public static void main(String[] args) {
        ClipboardObserver observer = new ClipboardObserver();
        System.out.println("Clipboard observer started. Press Ctrl+C to exit.");
        while (true) {
            // Keep the program running to observe the clipboard
        }
    }
}
