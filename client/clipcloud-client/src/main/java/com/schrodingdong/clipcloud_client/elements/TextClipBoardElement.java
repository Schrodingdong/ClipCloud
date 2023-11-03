package com.schrodingdong.clipcloud_client.elements;

public class TextClipBoardElement implements IClipBoardElement<String>{
    private String content;

    @Override
    public String getClipBoardElement() {
        return content;
    }

    @Override
    public void setClipBoardElement(String clipBoardElement) {
        this.content = clipBoardElement;
    }
}
