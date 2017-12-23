package com.example.desmond.clinc.Model;

/**
 * Created by Desmond on 11/26/2017.
 */

public class UserFactory {

    static final String PATH = "com.example.desmond.clinc.Model.";

    public static User createUser(String type) throws IllegalAccessException, InstantiationException, ClassNotFoundException {

        Class c = Class.forName(PATH + type);
        User user = (User) c.newInstance();
        return user;

    }


}
