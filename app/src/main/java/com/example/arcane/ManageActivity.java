package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ManageActivity extends AppCompatActivity {

    DBHelper db;
    EditText edit_foodID, edit_foodCategory, edit_foodName, edit_foodDescription;
    Button btn_insert, btn_view, btn_delete, btn_update, btn_search;
    String foodID, foodCategory, foodName, foodDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        setTitle("Manage Food Activity");

        db = new DBHelper(this);

        edit_foodID = findViewById(R.id.edit_foodID);
        edit_foodCategory = findViewById(R.id.edit_foodCategory);
        edit_foodName = findViewById(R.id.edit_foodName);
        edit_foodDescription = findViewById(R.id.edit_foodDescription);

        btn_insert = findViewById(R.id.button_insert);
        btn_view = findViewById(R.id.button_view);
        btn_delete = findViewById(R.id.button_delete);
        btn_update = findViewById(R.id.button_update);
        btn_search = findViewById(R.id.button_search);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodID = edit_foodID.getText().toString();
                try{
                    if(foodID.equals(""))
                        Toast.makeText(ManageActivity.this, "please fill the id", Toast.LENGTH_SHORT).show();
                    else{
                        long l = Long.parseLong(foodID);
                        db.deleteFood(l);
                        Toast.makeText(ManageActivity.this, "food info deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ManageActivity.this, ViewFoodActivity.class);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    Toast.makeText(ManageActivity.this, "error found during food information deletion", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodID          = edit_foodID.getText().toString();
                foodCategory    = edit_foodCategory.getText().toString();
                foodName        = edit_foodName.getText().toString();
                foodDescription = edit_foodDescription.getText().toString();

                try{
                    if(foodID.equals("") | foodCategory.equals("") |  foodName.equals("") | foodDescription.equals(""))
                        Toast.makeText(ManageActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    else{
                        long l = Long.parseLong(foodID);

                        db.updateFood(l,foodCategory, foodName, foodDescription);

                        edit_foodID.setText("");
                        edit_foodCategory.setText("");
                        edit_foodName.setText("");
                        edit_foodDescription.setText("");

                        Toast.makeText(ManageActivity.this, "food info updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ManageActivity.this, ViewFoodActivity.class);
                        startActivity(intent);
                        }
                }catch(Exception e){
                    Toast.makeText(ManageActivity.this, "error to update food info", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodID = edit_foodID.getText().toString();
                try{
                    if(foodID.equals(""))
                        Toast.makeText(ManageActivity.this, "please fill the id", Toast.LENGTH_SHORT).show();
                    else{
                        long l = Long.parseLong(foodID);

                        edit_foodCategory.setText(db.getFoodCategory(l));
                        edit_foodName.setText(db.getFoodName(l));
                        edit_foodDescription.setText(db.getFoodDescription(l));

                        Toast.makeText(ManageActivity.this, "food info found", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(ManageActivity.this, "error found during food information search", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}