package com.hk203.dadn.ui.notifications;

import java.util.Date;

public class Notification {
    public Date time;
    public String message;
    public String id;

    public Notification(Date time, String message, String id) {
        this.time = time;
        this.message = message;
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
