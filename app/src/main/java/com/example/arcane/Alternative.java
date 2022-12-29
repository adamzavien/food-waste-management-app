package com.example.arcane;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Alternative extends AppCompatActivity {

    TextView alternativeText;
    Button dispose, share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative);
        setTitle("Alternative");

        alternativeText = findViewById(R.id.txt_alternativeDescription);
        dispose = findViewById(R.id.btn_dispose);
        share = findViewById(R.id.btn_share);

        // grab constraint layout and store it into the variable
        ConstraintLayout constraintLayout = findViewById(R.id.alternativeLayout);

        // create animation drawable object
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        dispose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alternativeText.setText(R.string.alternativeTextDispose);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alternativeText.setText(R.string.alternativeTextShare);
            }
        });
    }
}