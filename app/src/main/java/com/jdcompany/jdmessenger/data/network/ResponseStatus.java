package com.jdcompany.jdmessenger.data.network;

import org.jetbrains.annotations.NotNull;

public enum ResponseStatus {
    FAIL("fail"),
    SUCCESS("success"),
    TAG_IS_TAKEN("tagIsTaken");


    private final String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    @NotNull
    @Override
    public String toString(){
        return status;
    }
}
