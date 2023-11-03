package com.schrodingdong.clipcloud_client.elements;

public class TextClipBoardElement extends ClipBoardElement<String> {
    private String content;

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
}
