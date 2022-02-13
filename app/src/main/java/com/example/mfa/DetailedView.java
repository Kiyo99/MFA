package com.example.mfa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;

public class DetailedView extends AppCompatActivity {

    TextView fullname, name, condition, detailedDesc, price;
    ImageView image;
    Button donate, chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        name = findViewById(R.id.name);
        fullname = findViewById(R.id.fullname);
        condition = findViewById(R.id.condition);
        detailedDesc = findViewById(R.id.dView);
        chat = findViewById(R.id.chat);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image);
        donate = findViewById(R.id.buy);

        String title, desc, detail, pricee, imagee, fullnamee;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            title = extras.getString("title");
            desc = extras.getString("desc");
            detail = extras.getString("detail");
            pricee = extras.getString("price");
            imagee = extras.getString("image");
            fullnamee= extras.getString("fullname");

            name.setText(title);
            fullname.setText(fullnamee);
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

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getmail = getIntent();
                Bundle extras = getmail.getExtras();
                if(extras != null) {
                    String email = extras.getString("email");

                    String mail[] = {email};
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, mail);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Donation Inquiry from MFA");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I have viewed your profile and I am interested in your situation. Reply me as soon as you can");

                    try {
                        startActivity(emailIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(DetailedView.this,
                                "Email app not installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}