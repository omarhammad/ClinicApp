package com.example.desmond.clinc.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.desmond.clinc.Controller.RegistrationController;
import com.example.desmond.clinc.Model.Authentication;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.Model.User;
import com.example.desmond.clinc.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText country;
    private EditText city;
    private EditText phoneNumber;
    private EditText uesrname;
    private EditText password;
    private Button signUpBtn;
  //  private Button signInBtn;
    RegistrationController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        User register;

        firstName = findViewById(R.id.fname);
        lastName = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        phoneNumber = findViewById(R.id.phonenum);
        uesrname = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        signUpBtn = findViewById(R.id.sign_up);
       // signInBtn = findViewById(R.id.sign_in);
        try {
            Log.i("Yes :", "Arrived");
            register = ModelFacade.createUser("Register");
            controller = new RegistrationController(this, register);
            controller.fillRegistration();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }



    }


    public EditText getFirstName() {
        return firstName;
    }

    public EditText getLastName() {
        return lastName;
    }

    public EditText getEmail() {
        return email;
    }

    public EditText getCountry() {
        return country;
    }

    public EditText getCity() {
        return city;
    }

    public EditText getPhoneNumber() {
        return phoneNumber;
    }

    public EditText getUesrname() {
        return uesrname;
    }

    public EditText getPassword() {
        return password;
    }


    public void addSignUpBtnActionListener(View.OnClickListener listener) {
        signUpBtn.setOnClickListener(listener);
    }

//    public void addSignInBtnActionListener(View.OnClickListener listener) {
//        signInBtn.setOnClickListener(listener);
//    }


}
