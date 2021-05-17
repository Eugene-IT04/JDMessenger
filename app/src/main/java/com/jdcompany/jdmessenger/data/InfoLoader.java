package com.jdcompany.jdmessenger.data;

import com.jdcompany.jdmessenger.data.objects.User;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

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

    public boolean readUserData(FileInputStream fileInputStream) {
        User user;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            user = (User) objectInputStream.readObject();
            InfoLoader.getInstance().setCurrentUser(user);
            objectInputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
