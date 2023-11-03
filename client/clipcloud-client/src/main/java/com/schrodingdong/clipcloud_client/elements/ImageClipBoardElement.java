package com.schrodingdong.clipcloud_client.elements;

import java.awt.image.BufferedImage;

public class ImageClipBoardElement extends ClipBoardElement<BufferedImage> {
    private BufferedImage content;

    public ImageClipBoardElement(BufferedImage content) {
        this.content = content;
    }

    @Override
    public BufferedImage getContent() {
        return content;
    }

    @Override
    public void setContent(BufferedImage element) {
        this.content = element;
    }
}
