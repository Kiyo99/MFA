package com.example.mfa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailedView extends AppCompatActivity {

    TextView name, condition, detailedDesc, price;
    ImageView image;
    Button donate, chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        name = findViewById(R.id.name);
        condition = findViewById(R.id.condition);
        detailedDesc = findViewById(R.id.dView);
        chat = findViewById(R.id.chat);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image);
        donate = findViewById(R.id.buy);

        String title, desc, detail, pricee, imagee;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            title = extras.getString("title");
            desc = extras.getString("desc");
            detail = extras.getString("detail");
            pricee = extras.getString("price");
            imagee = extras.getString("image");

            name.setText(title);
            condition.setText(desc);
            detailedDesc.setText(detail);
            price.setText(pricee);

            Picasso.get().load(imagee).into(image);
        }

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emma = new Intent(DetailedView.this, DonateActivity.class);
                startActivity(emma);
            }
        });
    }
}