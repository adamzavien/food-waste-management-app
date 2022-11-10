package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewFoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);
        setTitle("View Food");
    }
}