package com.example.desmond.clinc.Controller.Actions;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Desmond on 8/3/2017.
 */

public class FragmentUtils {


    public static void addFragment(FragmentActivity activity, int layout, boolean addToBackSatck, Fragment fragment) {

        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layout, fragment);
        if (addToBackSatck) {
            transaction.addToBackStack(null);
        }
        transaction.commit();


    }

    public static void replaceFragment(FragmentActivity activity, int layout, boolean addToBackSatck, Fragment fragment) {

        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(layout, fragment);
        if (addToBackSatck) {
            transaction.addToBackStack(null);
        }
        transaction.commit();

    }

    public static void removeFragment(FragmentActivity activity, Fragment fragment) {

        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();

    }


}
