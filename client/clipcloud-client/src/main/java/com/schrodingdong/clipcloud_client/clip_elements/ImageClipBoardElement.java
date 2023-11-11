package com.schrodingdong.clipcloud_client.clip_elements;

import com.schrodingdong.clipcloud_client.App;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

public class ImageClipBoardElement extends ClipBoardElement<BufferedImage> {
    private transient String imgName;
    private transient String tmpPath;
    private final String EXTENSION = "png";

    public ImageClipBoardElement(BufferedImage content) {
        // local specific variables -----------------------------------
        this.imgName = Date.from(Instant.now()).getTime() + "." + EXTENSION;
        this.tmpPath = App.OFFLINE_IMAGE_ELEMENTS_FILE+ "/" + imgName;
        // ------------------------------------------------------------
        this.type = ClipBoardElementTypes.IMAGE;
        setContent(content);
    }

    @Override
    public BufferedImage getContent() {
        return content;
    }

    @Override
    public void setContent(BufferedImage element) {
        this.content = element;
        setContentBase64(content);
    }

    @Override
    protected void setContentBase64(BufferedImage contentElement){
        byte[] byteImageContent;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(contentElement,EXTENSION, baos);
            byteImageContent = baos.toByteArray();
            this.contentBase64 = Base64.getEncoder().encodeToString(byteImageContent);
        } catch (IOException e) {
            System.err.println(">> Error converting the img to byte[]");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getImgName() {
        return imgName;
    }

    public String getTmpPath() {
        return tmpPath;
    }
}
