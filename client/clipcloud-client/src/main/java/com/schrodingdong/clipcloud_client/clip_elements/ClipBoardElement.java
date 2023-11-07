package com.schrodingdong.clipcloud_client.clip_elements;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class ClipBoardElement<ElType> implements Serializable {
    protected Date created;
    protected UUID uuid;
    protected String osVersion;
    protected String osName;
    protected String osArch;
    protected String userName;

    public ClipBoardElement() {
        this.created = Date.from(java.time.Instant.now());
        this.uuid = UUID.randomUUID();
        this.osVersion = System.getProperty("os.version");
        this.osName = System.getProperty("os.name");
        this.osArch = System.getProperty("os.arch");
        this.userName = System.getProperty("user.name");
    }

    public abstract ElType getContent();
    public abstract void setContent(ElType element);

    public Date getCreated() {
        return created;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsArch() {
        return osArch;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "ClipBoardElement{" +
                "created=" + created +
                ", uuid=" + uuid +
                ", osVersion='" + osVersion + '\'' +
                ", osName='" + osName + '\'' +
                ", osArch='" + osArch + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
