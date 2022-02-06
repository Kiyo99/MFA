package com.example.mfa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DestinationActivity extends AppCompatActivity {

    CardView donor, donee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        donor = findViewById(R.id.donor);
        donee = findViewById(R.id.donee);

        donee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emma = new Intent(DestinationActivity.this, MainActivity.class);
                startActivity(emma);
            }
        });

        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emma = new Intent(DestinationActivity.this, MainActivity2.class);
                startActivity(emma);
            }
        });
    }
}