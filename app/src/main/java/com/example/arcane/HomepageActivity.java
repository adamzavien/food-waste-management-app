package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomepageActivity extends AppCompatActivity {

    TextView txt_greetUser;
    Button btn_manageFoodWaste, btn_viewActivity;
    DBHelper db;
    String key_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        setTitle("Homepage");

        txt_greetUser       = findViewById(R.id.txt_greetUser);
        btn_manageFoodWaste = findViewById(R.id.btn_manageFoodWaste);
        btn_viewActivity    = findViewById(R.id.btn_viewActivity);

        db = new DBHelper(this);

        btn_manageFoodWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, ManageFoodWasteActivity.class);
                startActivity(intent);

                // session id
                {
                    // receive data from another activity
                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        key_value = extras.getString("key");
                        //The key argument here must match that used in the other activity
                    }
                    // pass data between activities
                    String value = key_value;
                    Intent i = new Intent(HomepageActivity.this, ManageFoodWasteActivity.class);
                    i.putExtra("key",value);
                    startActivity(i);
                }
            }
        });

        btn_viewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, ViewFoodActivity.class);
                startActivity(intent);
            }
        });
    }
}