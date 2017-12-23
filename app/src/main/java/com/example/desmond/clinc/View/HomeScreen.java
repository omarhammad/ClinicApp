package com.example.desmond.clinc.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.example.desmond.clinc.Controller.HomeController;
import com.example.desmond.clinc.Controller.Actions.OnBackPressedListener;
import com.example.desmond.clinc.R;

import java.io.IOException;

public class HomeScreen extends AppCompatActivity {


    private NavigationView navigationView;
    private HomeController controller;
    private DrawerLayout drawerLayout;
    private OnBackPressedListener onBackPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        controller = new HomeController(this);
        controller.saveUserData();
        try {
            controller.setDrawerHeaderData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.startWithHomeFragment();

    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.doBack();
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }

    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void addNavigationViewListener(NavigationView.OnNavigationItemSelectedListener listener) {

        navigationView.setNavigationItemSelectedListener(listener);

    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
}
