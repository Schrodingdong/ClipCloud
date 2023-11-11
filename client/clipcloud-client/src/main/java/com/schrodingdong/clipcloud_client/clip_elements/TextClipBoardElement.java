package com.schrodingdong.clipcloud_client.clip_elements;

import org.apache.commons.io.FileUtils;

import java.util.Base64;

public class TextClipBoardElement extends ClipBoardElement<String> {

    public TextClipBoardElement(String content) {
        this.type = ClipBoardElementTypes.TEXT;
        setContent(content);
    }

    @Override
    public String getContent() {
        return content;
    }


    @Override
    public void setContent(String contentElement) {
        this.content = contentElement;
        setContentBase64(content);
    }

    @Override
    protected void setContentBase64(String contentElement) {
        byte[] byteStringContent = content.getBytes();
        this.contentBase64 = Base64.getEncoder().encodeToString(byteStringContent);
    }
}
