package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    EditText et_username, et_email, et_password;
    Button btn_signUp2;
    DBHelper db;
    String username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        // grab constraint layout and store it into the variable
        ConstraintLayout constraintLayout = findViewById(R.id.signUpLayout);

        // create animation drawable object
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        et_username = findViewById(R.id.et_regUsername);
        et_email = findViewById(R.id.et_regEmail);
        et_password = findViewById(R.id.et_regPassword);
        btn_signUp2 = findViewById(R.id.btn_signUp2);

        db = new DBHelper(this);

        btn_signUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // convert input to string
                username    = et_username.getText().toString();
                email       = et_email.getText().toString();
                password    = et_password.getText().toString();

                try{
                    // to ensure all the fields are not blank
                    if(username.equals("") || email.equals("") || password.equals(""))
                        Toast.makeText(SignUpActivity.this, "please fill all the information", Toast.LENGTH_SHORT).show();
                    else{
                        // insert data into user table
                        db.addUser(username, email, password);

                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    Toast.makeText(SignUpActivity.this, "error section 001", Toast.LENGTH_SHORT).show();
                    // possible error : database's table initialization
                }
            }
        });

    }
}