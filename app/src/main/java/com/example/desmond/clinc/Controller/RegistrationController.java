package com.example.desmond.clinc.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Toast;

import com.example.desmond.clinc.Model.Address;
import com.example.desmond.clinc.Model.Authentication;
import com.example.desmond.clinc.Model.CallBacks.CheckCallback;
import com.example.desmond.clinc.Model.Firebase.AuthenticationDB;
import com.example.desmond.clinc.Model.Firebase.DBFacade;
import com.example.desmond.clinc.Model.Firebase.UserDB;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.Model.Register;
import com.example.desmond.clinc.Model.User;
import com.example.desmond.clinc.View.HomeScreen;
import com.example.desmond.clinc.View.LoginScreen;
import com.example.desmond.clinc.View.RegisterActivity;

/**
 * Created by Desmond on 12/4/2017.
 */

public class RegistrationController {

    LoginScreen loginView;
    RegisterActivity registerView;
    Authentication authenticationModel;
    User registerModel;
    DBFacade db;
    Context context;


    public RegistrationController(LoginScreen loginView, Authentication authenticationModel) {
        this.loginView = loginView;
        this.context = loginView.getApplicationContext();
        this.loginView.addSignInBtnActionListener(loginVerifyingListener());
        this.loginView.addSignUpBtnActionListener(goToRegisterScreenListener());

        this.authenticationModel = authenticationModel;
        db = DBFacade.getINSTANCE(context);

    }

    public RegistrationController(RegisterActivity registerView, User registerModel) {
        this.registerView = registerView;
        this.registerModel = registerModel;
        this.context = this.registerView.getApplicationContext();
        this.registerView.addSignUpBtnActionListener(registerUserListener());
        db = DBFacade.getINSTANCE(context);

    }


    public void loginVerify() {


        db.signIn(context,authenticationModel, new CheckCallback() {
            @Override
            public void success() {
                goToHome();
                loginView.finish();
            }


            @Override
            public void fail() {
                Toast.makeText(context, "invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void checkLoginData() {

        String uesrname = loginView.getUsername().getText().toString();
        String password = loginView.getPassword().getText().toString();

        if (uesrname.trim().equals("") || password.trim().equals("")) {
            Toast.makeText(context, "invalid username/password", Toast.LENGTH_SHORT).show();
        } else {
            authenticationModel.setUsername(uesrname);
            authenticationModel.setPassword(password);
            loginVerify();


        }


    }

    public View.OnClickListener loginVerifyingListener() {


        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginData();
            }
        };
    }

    public View.OnClickListener goToRegisterScreenListener() {


        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };
    }

    public View.OnClickListener registerUserListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRegisterData();
            }
        };
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void checkUserSession(Context context) {

        boolean logged = db.checkUserSession(context);
        if (logged) {
            goToHome();
        }

    }


    public void checkRegisterData() {

        String firstName = registerView.getFirstName().getText().toString();
        String lastName = registerView.getLastName().getText().toString();
        String email = registerView.getEmail().getText().toString();
        String country = registerView.getCountry().getText().toString();
        String city = registerView.getCity().getText().toString();
        String phoneNum = registerView.getPhoneNumber().getText().toString();
        String username = registerView.getUesrname().getText().toString();
        String password = registerView.getPassword().getText().toString();

        if (firstName.trim().equals("") || lastName.trim().equals("") || email.trim().equals("") || country.trim().equals("")
                || city.trim().equals("") || phoneNum.trim().equals("") || username.trim().equals("") || password.trim().equals("")) {


            Toast.makeText(context, "Form Data Incomplete", Toast.LENGTH_SHORT).show();

        } else {

            registerModel.setFirstName(firstName);
            registerModel.setLastName(lastName);
            registerModel.setEmail(email);
            Address address = ModelFacade.createAddress();
            address.setCountry(country);
            address.setCity(city);
            registerModel.setAddress(address);
            registerModel.setPhoneNum(phoneNum);
            ((Register) registerModel).setUsername(username);
            ((Register) registerModel).setPassword(password);
            registerVerify();
        }


    }

    public void registerVerify() {

        db.register((Register) registerModel, new CheckCallback() {
            @Override
            public void success() {
                goToHome();
                registerView.finish();

            }

            @Override
            public void fail() {
                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void goToHome() {
        Intent intent = new Intent(context, HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);


    }

    public void fillRegistration() {

        registerView.getFirstName().setText("Omar");
        registerView.getLastName().setText("Hammad");
        registerView.getEmail().setText("omarhammad767@gmail.com");
        registerView.getCountry().setText("Palestine");
        registerView.getCity().setText("Gaza");
        registerView.getPhoneNumber().setText("0595892260");
        registerView.getUesrname().setText("omarhammad97");
        registerView.getPassword().setText("123321");

    }


}
