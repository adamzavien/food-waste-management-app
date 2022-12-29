package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomepageActivity extends AppCompatActivity {

    TextView txt_greetUser;
    Button btn_manageFoodWaste, btn_viewActivity;
    DBHelper db;
    String key_value;
    int xp = 0;
    TextView currentXP, goalXP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        setTitle("Homepage");

        txt_greetUser       = findViewById(R.id.txt_greetUser);
        btn_manageFoodWaste = findViewById(R.id.btn_manageFoodWaste);
        btn_viewActivity    = findViewById(R.id.btn_viewActivity);
        currentXP           = findViewById(R.id.txt_currentXP);
        goalXP              = findViewById(R.id.txt_goalXP);


        // grab constraint layout and store it into the variable
        ConstraintLayout constraintLayout = findViewById(R.id.homepageLayout);

        // create animation drawable object
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        db = new DBHelper(this);

        goalXP.setText(db.highestXP());

        //session id
        // receive data from another activity : login activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key_value = extras.getString("key");
            //The key argument here must match that used in the other activity
        }
        // pass data between activities
        String value = key_value; // this key value represent username
        Intent i = new Intent(HomepageActivity.this, ManageFoodWasteActivity.class);
        i.putExtra("key",value);

        txt_greetUser.setText("Hello, " + key_value);

        btn_manageFoodWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                xp = xp + 3;
                db.insertXP(key_value, xp);
                currentXP.setText("Your Point : " + xp + " XP");
                goalXP.setText(db.highestXP());

                startActivity(i);
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