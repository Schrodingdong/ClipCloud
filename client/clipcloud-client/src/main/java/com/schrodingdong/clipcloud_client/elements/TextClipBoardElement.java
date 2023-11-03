package com.schrodingdong.clipcloud_client.elements;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class TextClipBoardElement extends ClipBoardElement<String> {
    protected String content;

    public TextClipBoardElement(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String clipBoardElement) {
        this.content = clipBoardElement;
    }

    @Override
    public String toString() {
        return "TextClipBoardElement{" +
                "content='" + content + '\'' +
                ", created=" + created +
                ", uuid=" + uuid +
                ", osVersion='" + osVersion + '\'' +
                ", osName='" + osName + '\'' +
                ", osArch='" + osArch + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
