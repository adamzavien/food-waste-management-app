package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ManageFoodWasteActivity extends AppCompatActivity {

    EditText et_foodCategory, et_foodName, et_FoodDescription;
    Button btn_composeFoodWaste;
    String foodCategory, foodName, foodDescription, key_value;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_food_waste);
        setTitle("Manage Food Waste");

        // initialize
        et_foodCategory         = findViewById(R.id.et_foodCategory);
        et_foodName             = findViewById(R.id.et_foodName);
        et_FoodDescription      = findViewById(R.id.et_foodDescription);
        btn_composeFoodWaste    = findViewById(R.id.btn_composeFoodWaste);

        db = new DBHelper(this);

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
                        Toast.makeText(ManageFoodWasteActivity.this, "please fill all the information", Toast.LENGTH_SHORT).show();
                    else{
                        Bundle extras = getIntent().getExtras();
                        if (extras != null) {
                            key_value = extras.getString("key");
                            //The key argument here must match that used in the other activity
                        }
                        db.addFood(foodCategory,foodName,foodDescription, key_value);

                        Intent intent = new Intent(ManageFoodWasteActivity.this, ViewFoodActivity.class);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    Toast.makeText(ManageFoodWasteActivity.this, "error found during food information insertion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}