package com.example.mfa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StoriesView extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SelectListener {

    RecyclerView recyclerView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    myStoriesAdapter myStoriesAdapter;
    ArrayList<storiesClass> storiesClassArrayList;
    ProgressDialog pd;
    String problemType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_view);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Sections, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        pd = new ProgressDialog(this);
//        pd.setCancelable(false);
        pd.setMessage("Loading Stories, please wait...");
        pd.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        storiesClassArrayList = new ArrayList<>();
        myStoriesAdapter = new myStoriesAdapter(this, storiesClassArrayList, this);
        recyclerView.setAdapter(myStoriesAdapter);

    }

    private void EventChangeListener(String problemType) {

        pd.show();
            db.collection(problemType).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    if (error != null){
                        pd.dismiss();
                        Log.e("Error", error.getMessage());
                        Toast.makeText(StoriesView.this, "Error loading stories, please try again", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    for (DocumentChange dc : value.getDocumentChanges()){
                        if (dc.getType() == DocumentChange.Type.ADDED){

                            String problemTitle, problemDesc, problemPrice;
                            Uri productImageUri;

                            storiesClassArrayList.add(dc.getDocument().toObject(storiesClass.class));

                        }

                        myStoriesAdapter.notifyDataSetChanged();
                        pd.dismiss();
                    }
                }
            });
        }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        problemType = adapterView.getItemAtPosition(i).toString();
        if (problemType.equalsIgnoreCase("Education")){
            storiesClassArrayList.clear();
            EventChangeListener(problemType);
        }
        else if (problemType.equalsIgnoreCase("Business")){
            storiesClassArrayList.clear();
            EventChangeListener(problemType);
        }
        else if (problemType.equalsIgnoreCase("Medical")){
            storiesClassArrayList.clear();
            EventChangeListener(problemType);
        }
        else if (problemType.equalsIgnoreCase("Orphan")){
            storiesClassArrayList.clear();
            EventChangeListener(problemType);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClicked(storiesClass storiesClass) {

//        Toast.makeText(this, storiesClass.getProblemDetail(), Toast.LENGTH_SHORT).show();
        Intent emma = new Intent(StoriesView.this, DetailedView.class);

        emma.putExtra("title", storiesClass.getProblemTitle());
        emma.putExtra("detail", storiesClass.getProblemDetail());
        emma.putExtra("price", storiesClass.getProblemPrice());
        emma.putExtra("image", storiesClass.getProblemImage());
        emma.putExtra("desc", storiesClass.getProblemDesc());
        emma.putExtra("email", storiesClass.getEmail());
        emma.putExtra("fullname", storiesClass.getFullname());

        startActivity(emma);
    }
}