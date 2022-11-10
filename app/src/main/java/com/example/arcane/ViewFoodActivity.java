package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ViewFoodActivity extends AppCompatActivity {

    TextView viewFoodActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);
        setTitle("View Food");

        viewFoodActivity = findViewById(R.id.viewFoodActivity);

        DBHelper db = new DBHelper(this);

        String data = db.viewFoodList();
        viewFoodActivity.setText(data);

    }
}