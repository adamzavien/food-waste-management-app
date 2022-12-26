package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewFoodActivity extends AppCompatActivity {

    TextView viewFoodActivity;
    Button btn_goDelete, btn_goUpdate, btn_goSearch;

    public void intent(){
        Intent intent = new Intent(ViewFoodActivity.this, ManageActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);
        setTitle("List of Food Waste Record");

        viewFoodActivity = findViewById(R.id.viewFoodActivity);
        btn_goDelete = findViewById(R.id.btn_goDelete);
        btn_goUpdate = findViewById(R.id.btn_goUpdate);
        btn_goSearch = findViewById(R.id.btn_goSearch);

        DBHelper db = new DBHelper(this);

        String data = db.viewFoodList();
        viewFoodActivity.setText(data);

        // all the button below will go to the next activity

        btn_goDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent();
            }
        });

        btn_goUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent();
            }
        });

        btn_goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent();
            }
        });
    }
}