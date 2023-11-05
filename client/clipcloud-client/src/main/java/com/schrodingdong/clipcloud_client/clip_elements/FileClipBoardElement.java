package com.schrodingdong.clipcloud_client.elements;

import com.schrodingdong.clipcloud_client.App;

import java.io.File;

public class FileClipBoardElement extends ClipBoardElement<File>{
    private transient File content;
    private String srcPath;
    private String tmpPath;

    public FileClipBoardElement(File content, String srcPath) {
        this.content = content;
        this.srcPath = srcPath;
        this.tmpPath = App.OFFLINE_FILE_ELEMENTS_FILE + "/" + content.getName();
    }

    @Override
    public File getContent() {
        return content;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public String getTmpPath() {
        return tmpPath;
    }

    @Override
    public void setContent(File element) {
        this.content = element;
    }

    @Override
    public String toString() {
        return "FileClipBoardElement{" +
                "content=" + content +
                ", srcPath='" + srcPath + '\'' +
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
