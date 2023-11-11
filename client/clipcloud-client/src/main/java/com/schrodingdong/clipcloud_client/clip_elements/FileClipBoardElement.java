package com.schrodingdong.clipcloud_client.clip_elements;

import com.schrodingdong.clipcloud_client.App;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.Base64;

public class FileClipBoardElement extends ClipBoardElement<File>{
    private transient String srcPath;
    private transient String tmpPath;
    private transient String extension;

    public FileClipBoardElement(File content, String srcPath) {
        super();
        // local specific variables ------------------------------------------------
        this.srcPath = srcPath;
        this.tmpPath = App.OFFLINE_FILE_ELEMENTS_FILE + "/" + content.getName();
        this.content = content;
        this.extension = getExtension();
        // -------------------------------------------------------------------------
        this.type = ClipBoardElementTypes.FILE;
        this.filename= uuid + "." + extension;
        setContent(content);
    }

    @Override
    public void setContent(File element) {
        this.content = element;
        setContentBase64(element);
    }

    @Override
    protected void setContentBase64(File contentElement) {
        byte[] byteFileContent;
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
    public File getContent() {
        return content;
    }

    private String getExtension(){
        String path = content.getAbsolutePath();
        return path.substring(
                path.lastIndexOf('.')+1
        );
    }
}
