package com.jdcompany.jdmessenger.data;

import com.jdcompany.jdmessenger.database.AppDatabase;
import com.jdcompany.jdmessenger.database.UserDao;

import java.util.List;

public class InfoLoader {
    public static String USER_DATA_FILE_NAME = "currentUser";
    private static InfoLoader infoLoader;

    private User currentUser;
    private AppDatabase appDatabase;
    private UserDao userDao;

    private InfoLoader(AppDatabase appDatabase){
        this.appDatabase = appDatabase;
        this.userDao = appDatabase.userDao();
    }

    public static void InitInfoLoader(AppDatabase appDatabase){
        infoLoader = new InfoLoader(appDatabase);
    }

    public static InfoLoader getInstance(){
        if(infoLoader == null){
            throw new IllegalStateException("InfoLoader is not initialized");
        }
        return infoLoader;
    }

    public void setCurrentUser(User user){
        currentUser = user;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public List<User> getAllUsers(){
        return userDao.getAll();
    }

    public void addUser(User user){
        userDao.insert(user);
    }

    public void deleteUser(User user){
        userDao.delete(user);
    }
}
