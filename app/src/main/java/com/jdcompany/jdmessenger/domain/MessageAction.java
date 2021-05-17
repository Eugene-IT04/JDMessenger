package com.jdcompany.jdmessenger.domain;

import org.jetbrains.annotations.NotNull;

public enum MessageAction {
    TEXT("text"),
    DELETE_MESSAGE("delete"),
    UPDATE_INFO("update"),
    EDIT_MESSAGE("edit");

    final private String action;

    MessageAction(String action){
        this.action = action;
    }

    @NotNull
    @Override
    public String toString(){
        return action;
    }
}
