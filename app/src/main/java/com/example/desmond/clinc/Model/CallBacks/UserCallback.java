package com.example.desmond.clinc.Model.CallBacks;

import com.example.desmond.clinc.Model.User;
import java.util.ArrayList;

/**
 * Created by Desmond on 12/7/2017.
 */

public interface UserCallback {

     void currentUser(User user);

     void getUseres(ArrayList<User> users) ;

}
