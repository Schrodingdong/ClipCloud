package com.schrodingdong.clipcloud_client.elements;

import java.util.Date;

public abstract class ClipBoardElement<ElType> {
    Date created;
    String origin;

    public abstract ElType getContent();
    public abstract void setContent(ElType element);


    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
