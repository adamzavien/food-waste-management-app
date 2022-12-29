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

public class ManageFoodWasteActivity extends AppCompatActivity {

    EditText et_foodCategory, et_foodName, et_FoodDescription;
    Button btn_composeFoodWaste, btn_goToImageClassify;
    String foodCategory, foodName, foodDescription, key_value;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_food_waste);
        setTitle("Manage Food Waste");

        // grab constraint layout and store it into the variable
        ConstraintLayout constraintLayout = findViewById(R.id.manageFoodWasteLayout);

        // create animation drawable object
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        // initialize
        et_foodCategory         = findViewById(R.id.et_foodCategory);
        et_foodName             = findViewById(R.id.et_foodName);
        et_FoodDescription      = findViewById(R.id.et_foodDescription);
        btn_composeFoodWaste    = findViewById(R.id.btn_composeFoodWaste);
        btn_goToImageClassify   = findViewById(R.id.btn_goToImageClassify);

        db = new DBHelper(this);

        // receive data from another activity : homepage activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key_value = extras.getString("key");
            //The key argument here must match that used in the other activity
        }
        // pass data between activities
        String value = key_value; // this key value represent username
        Intent i = new Intent(ManageFoodWasteActivity.this, ImageClassify.class);
        i.putExtra("key",value);


        btn_composeFoodWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // convert input to string
                foodCategory    = et_foodCategory.getText().toString();
                foodName        = et_foodName.getText().toString();
                foodDescription = et_FoodDescription.getText().toString();

                // check if input is null / empty
                try{
                    // to ensure all the fields are not blank
                    if(foodCategory.equals("") || foodName.equals("") || foodDescription.equals(""))
                        Toast.makeText(ManageFoodWasteActivity.this, "Please fill all the information", Toast.LENGTH_SHORT).show();
                    else{

                        db.addFood(foodCategory,foodName,foodDescription, key_value);

                        //Intent intent = new Intent(ManageFoodWasteActivity.this, CompostPit.class);
                        //startActivity(intent);

                        compostTech(foodCategory);
                    }
                }catch(Exception e){
                    Toast.makeText(ManageFoodWasteActivity.this, "Error found during food information insertion", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_goToImageClassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }

    // Generate food decompositon technique based on food category
    public void compostTech(String category){
        if(category.toLowerCase().equals("fruits") || category.toLowerCase().equals("vegetables")){
            Intent aaa = new Intent(ManageFoodWasteActivity.this, CompostPit.class);
            startActivity(aaa);
        }else if(category.toLowerCase().equals("grains")){
            Intent aaa = new Intent(ManageFoodWasteActivity.this, CompostVermi.class);
            startActivity(aaa);
        }else{
            Intent aaa = new Intent(ManageFoodWasteActivity.this, Alternative.class);
            startActivity(aaa);
        }
    }
}