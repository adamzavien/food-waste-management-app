package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_signIn;
    DBHelper db;
    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Sign In");

        et_email = findViewById(R.id.et_logEmail);
        et_password = findViewById(R.id.et_logPassword);
        btn_signIn = findViewById(R.id.btn_logIn);

        db = new DBHelper(this);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // convert input to string
                email       = et_email.getText().toString();
                password    = et_password.getText().toString();

                try{
                    // to ensure all the fields are not blank
                    if(email.equals("") || password.equals(""))
                        Toast.makeText(SignInActivity.this, "please fill all the information", Toast.LENGTH_SHORT).show();
                    else{
                        try{
                            Boolean checkUser = db.checkBoth(email, password);

                            if(checkUser == true){
                                Toast.makeText(SignInActivity.this, "sign in successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SignInActivity.this, HomepageActivity.class);
                                startActivity(intent);
                            }else
                                Toast.makeText(SignInActivity.this, "invalid sign in credentials", Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            Toast.makeText(SignInActivity.this, "error section 003", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch(Exception e){
                    Toast.makeText(SignInActivity.this, "error section 002", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}