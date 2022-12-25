package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    EditText et_username, et_password;
    Button btn_signIn;
    DBHelper db;
    String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Sign In");

        et_username = findViewById(R.id.et_logUsername);
        et_password = findViewById(R.id.et_logPassword);
        btn_signIn = findViewById(R.id.btn_logIn);

        db = new DBHelper(this);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // convert input to string
                username = et_username.getText().toString();
                password = et_password.getText().toString();

                try{
                    // to ensure all the fields are not blank
                    if(username.equals("") || password.equals(""))
                        Toast.makeText(SignInActivity.this, "Please fill in all the information", Toast.LENGTH_SHORT).show();
                    else{
                        try{
                            Boolean checkUser = db.checkBoth(username, password);

                            if(checkUser == true){
                                Toast.makeText(SignInActivity.this, "Sign In Successfully", Toast.LENGTH_SHORT).show();

                                // session id
                                // pass data (username) between activities
                                String value = et_username.getText().toString();
                                Intent i = new Intent(SignInActivity.this, HomepageActivity.class);
                                i.putExtra("key",value);
                                startActivity(i);
                            }else
                                Toast.makeText(SignInActivity.this, "Invalid Sign In Credentials", Toast.LENGTH_SHORT).show();
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