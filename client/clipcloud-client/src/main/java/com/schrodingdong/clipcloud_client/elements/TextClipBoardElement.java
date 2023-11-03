package com.schrodingdong.clipcloud_client.elements;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class TextClipBoardElement extends ClipBoardElement<String> {
    private String content;

    public TextClipBoardElement(String content) {
        this.content = content;
        this.created = Date.from(Instant.now());
        this.uuid = UUID.randomUUID();
        this.osVersion = System.getProperty("os.version");
        this.osName = System.getProperty("os.name");
        this.osArch = System.getProperty("os.arch");
        this.userName = System.getProperty("user.name");
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
