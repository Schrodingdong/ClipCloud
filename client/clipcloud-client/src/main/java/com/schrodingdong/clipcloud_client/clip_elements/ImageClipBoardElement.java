package com.schrodingdong.clipcloud_client.clip_elements;

import com.schrodingdong.clipcloud_client.App;
import org.apache.commons.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

public class ImageClipBoardElement extends ClipBoardElement<BufferedImage> {
    private transient BufferedImage content;
    private String contentBase64;
    private String imgName;
    private String tmpPath;
    public final ClipBoardElementTypes type = ClipBoardElementTypes.IMAGE;

    public ImageClipBoardElement(BufferedImage content) {
        this.content = content;
//        byte[] byteContent = FileUtils.readFileToByteArray(content);
//        this.contentBase64 = Base64.getEncoder().encode(content.get)
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

    public void setContentBase64(){
        byte[] byteImageContent = new byte[0];
        try {
            byteImageContent = FileUtils.readFileToByteArray(new File(tmpPath));
            this.contentBase64 = Base64.getEncoder().encodeToString(byteImageContent);
        } catch (IOException e) {
            System.err.println(">> Error converting the img to byte[]");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "ImageClipBoardElement{" +
                "content=" + content +
                ", imgName='" + imgName + '\'' +
                ", tmpPath='" + tmpPath + '\'' +
                ", type=" + type+
                ", created=" + created +
                ", uuid=" + uuid +
                ", osVersion='" + osVersion + '\'' +
                ", osName='" + osName + '\'' +
                ", osArch='" + osArch + '\'' +
                ", userName='" + userName + '\'' +
                ", contentBase64='" + contentBase64.toString() + '\'' +
                '}';
    }
}
