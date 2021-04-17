package com.jdcompany.jdmessenger.data.callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallBackInfo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
