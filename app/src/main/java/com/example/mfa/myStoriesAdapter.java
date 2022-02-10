package com.example.mfa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class myStoriesAdapter extends RecyclerView.Adapter<myStoriesAdapter.MyViewHolder> {

    Context context;
    ArrayList <storiesClass> storiesList;
    SelectListener selectListener;

    public myStoriesAdapter(Context context, ArrayList<storiesClass> storiesList, SelectListener selectListener) {
        this.context = context;
        this.storiesList = storiesList;
        this.selectListener = selectListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        storiesClass storiesClass = storiesList.get(position);

        holder.name.setText(storiesClass.getProblemTitle());
        holder.condition.setText(storiesClass.getProblemDesc());
        holder.price.setText(storiesClass.getProblemPrice());

        Picasso.get().load(storiesClass.problemImage).into(holder.image);

        holder.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(storiesList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, condition, price;
        ImageView image;
        Button donate;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.name);
            condition = itemView.findViewById(R.id.condition);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            donate = itemView.findViewById(R.id.buy);

//            donate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    itemView.getContext().startActivity(new Intent(itemView.getContext(),DetailedView.class));
//                }
//            });

        }
    }
}
