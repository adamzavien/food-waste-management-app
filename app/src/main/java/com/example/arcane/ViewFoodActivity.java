package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewFoodActivity extends AppCompatActivity {

    TextView viewFoodActivity;
    Button btn_goDelete, btn_goUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);
        setTitle("View List of Food Waste");

        viewFoodActivity = findViewById(R.id.viewFoodActivity);
        btn_goDelete = findViewById(R.id.btn_goDelete);
        btn_goUpdate = findViewById(R.id.btn_goUpdate);

        DBHelper db = new DBHelper(this);

        String data = db.viewFoodList();
        viewFoodActivity.setText(data);

        btn_goDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewFoodActivity.this, ManageActivity.class);
                startActivity(intent);
            }
        });

        btn_goUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewFoodActivity.this, ManageActivity.class);
                startActivity(intent);
            }
        });

    }
}