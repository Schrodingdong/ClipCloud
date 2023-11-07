package com.schrodingdong.clipcloud_client.clip_elements;

public class TextClipBoardElement extends ClipBoardElement<String> {
    private String content;
    public final ClipBoardElementTypes type = ClipBoardElementTypes.TEXT;

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
                ", type=" + type+
                ", created=" + created +
                ", uuid=" + uuid +
                ", osVersion='" + osVersion + '\'' +
                ", osName='" + osName + '\'' +
                ", osArch='" + osArch + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
