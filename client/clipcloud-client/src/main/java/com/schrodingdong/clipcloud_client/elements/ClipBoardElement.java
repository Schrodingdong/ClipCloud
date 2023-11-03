package com.schrodingdong.clipcloud_client.elements;

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

    public abstract ElType getContent();
    public abstract void setContent(ElType element);
}
