package com.example.desmond.clinc.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.desmond.clinc.Controller.RegistrationController;
import com.example.desmond.clinc.Model.Authentication;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.R;

public class LoginScreen extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private Button loginBtn;
    private Button registerBtn;

    private RegistrationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        username = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.signup_btn);


        Authentication auth = ModelFacade.createAuthntication();
        controller = new RegistrationController(this, auth);
        controller.checkUserSession(this.getBaseContext());

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public EditText getUsername() {
        return username;
    }

    public EditText getPassword() {
        return password;
    }

    public void addSignInBtnActionListener(View.OnClickListener listener) {
        loginBtn.setOnClickListener(listener);

    }

    public void addSignUpBtnActionListener(View.OnClickListener listener) {
        registerBtn.setOnClickListener(listener);

    }
}
