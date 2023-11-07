package com.schrodingdong.clipcloud_client.clip_elements;

import com.schrodingdong.clipcloud_client.App;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class FileClipBoardElement extends ClipBoardElement<File>{
    private transient File content;
    private String contentBase64;
    private String srcPath;
    private String tmpPath;
    public final ClipBoardElementTypes type = ClipBoardElementTypes.FILE;

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

    public void setContentBase64(){
        byte[] byteFileContent = new byte[0];
        try {
            byteFileContent = FileUtils.readFileToByteArray(content);
            this.contentBase64 = Base64.getEncoder().encodeToString(byteFileContent);
        } catch (IOException e) {
            System.err.println(">> Error converting the file to byte[]");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "FileClipBoardElement{" +
                "content=" + content +
                ", srcPath='" + srcPath + '\'' +
                ", tmpPath='" + tmpPath + '\'' +
                ", type=" + type+
                ", created=" + created +
                ", uuid=" + uuid +
                ", osVersion='" + osVersion + '\'' +
                ", osName='" + osName + '\'' +
                ", osArch='" + osArch + '\'' +
                ", userName='" + userName + '\'' +
                ", contentBase64='" + contentBase64+ '\'' +
                '}';
    }
}
