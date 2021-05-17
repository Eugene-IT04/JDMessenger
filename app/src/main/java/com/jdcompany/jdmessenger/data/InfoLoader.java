package com.jdcompany.jdmessenger.data;

import com.jdcompany.jdmessenger.data.objects.User;

public class InfoLoader {
    public static String USER_DATA_FILE_NAME = "currentUser";
    private static InfoLoader infoLoader;

    private User currentUser;

    private InfoLoader(){}

    public static synchronized InfoLoader getInstance(){
        if(infoLoader == null){
            infoLoader = new InfoLoader();
        }
        return infoLoader;
    }

    public void setCurrentUser(User user){
        currentUser = user;
    }

    public User getCurrentUser(){
        return currentUser;
    }

}
