package com.jdcompany.jdmessenger.data;

public class InfoLoader {
    private static InfoLoader infoLoader;
    private InfoLoader(){}

    public synchronized static InfoLoader getInstance(){
        if(infoLoader == null){
            infoLoader = new InfoLoader();
        }
        return infoLoader;
    }

    public User getCurrentUser(){
        User user = new User();
        user.setName("Eugene");
        user.setId(345);
        user.setPhoto("somePhoto");
        user.setPublicRsa("public RSA");
        user.setTag("some tag");
        return user;
    }
}
