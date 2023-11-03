package com.schrodingdong.clipcloud_client.elements;

import java.awt.image.BufferedImage;
import java.io.File;

public class FileClipBoardElement extends ClipBoardElement<File>{
    private File content;

    public FileClipBoardElement(File content) {
        this.content = content;
    }

    @Override
    public File getContent() {
        return content;
    }

    @Override
    public void setContent(File element) {
        this.content = element;
    }
}
