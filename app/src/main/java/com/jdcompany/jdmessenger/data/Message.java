package com.jdcompany.jdmessenger.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("fromId")
    @Expose
    private long fromId;
    @SerializedName("toId")
    @Expose
    private long toId;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("body")
    @Expose
    private String body;

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
