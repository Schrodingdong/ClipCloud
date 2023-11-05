package com.schrodingdong.clipcloud_client.elements;

import com.schrodingdong.clipcloud_client.App;

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Date;

public class ImageClipBoardElement extends ClipBoardElement<BufferedImage> {
    private transient BufferedImage content;
    private String imgName;
    private String tmpPath;

    public ImageClipBoardElement(BufferedImage content) {
        this.content = content;
        this.imgName = Date.from(Instant.now()).getTime() + ".png";
        this.tmpPath = App.OFFLINE_IMAGE_ELEMENTS_FILE+ "/" + imgName;
    }

    @Override
    public BufferedImage getContent() {
        return content;
    }

    @Override
    public void setContent(BufferedImage element) {
        this.content = element;
    }

    public String getImgName() {
        return imgName;
    }

    public String getTmpPath() {
        return tmpPath;
    }

    @Override
    public String toString() {
        return "ImageClipBoardElement{" +
                "content=" + content +
                ", imgName='" + imgName + '\'' +
                ", tmpPath='" + tmpPath + '\'' +
                ", created=" + created +
                ", uuid=" + uuid +
                ", osVersion='" + osVersion + '\'' +
                ", osName='" + osName + '\'' +
                ", osArch='" + osArch + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
