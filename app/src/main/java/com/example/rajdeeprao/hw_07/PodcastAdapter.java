/*
    HomeWork 07
    PodcastAdapter.java
    Yateen Kedare | Rajdeep Rao
 */
package com.example.rajdeeprao.hw_07;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajdeeprao on 3/11/17.
 */

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.ViewHolder>{
    IData activity;



    Context context;
    int resource;
    List<PodcastItem> objects;

    public PodcastAdapter(Context context, int resource, ArrayList<PodcastItem> podcastItemsList, IData activity) {
        this.context = context;
        this.resource = resource;
        objects=podcastItemsList;
        this.activity=activity;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1,tv2;
        ImageView imageView, play;
        int position;
        public ViewHolder(View itemView) {
            super(itemView);
            tv1= (TextView) itemView.findViewById(R.id.title);
            tv2= (TextView) itemView.findViewById(R.id.date);
            imageView= (ImageView) itemView.findViewById(R.id.imageView);
            play = (ImageView) itemView.findViewById(R.id.playIcon);
        }


    }

    @Override
    public PodcastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resource, parent, false);
        ViewHolder vh=new ViewHolder(itemView);
        return vh;

    }

    @Override
    public void onBindViewHolder(PodcastAdapter.ViewHolder holder, final int position) {
        final PodcastItem item= objects.get(position);
        holder.tv1.setText(item.getTitle());
        holder.tv2.setText(item.getPublicationDate());
        holder.position = position;
        Picasso.with(context)
                .load(item.getImageURL())
                .resize(50, 50)
                .into(holder.imageView);
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    activity.returnedValues(objects.get(position).getMP3URL());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EpisodeActivity.class);
                ArrayList<String> objDetails=new ArrayList<String>();
                objDetails.add(objects.get(position).getTitle());
                objDetails.add(objects.get(position).getImageURL());
                objDetails.add(objects.get(position).getDescription());
                objDetails.add(objects.get(position).getPublicationDate());
                objDetails.add(objects.get(position).getDuration());
                objDetails.add(objects.get(position).getMP3URL());
                intent.putStringArrayListExtra("PODCAST",objDetails);
                context.startActivity(intent);

            }
        });
        holder.tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EpisodeActivity.class);
                ArrayList<String> objDetails=new ArrayList<String>();
                objDetails.add(objects.get(position).getTitle());
                objDetails.add(objects.get(position).getImageURL());
                objDetails.add(objects.get(position).getDescription());
                objDetails.add(objects.get(position).getPublicationDate());
                objDetails.add(objects.get(position).getDuration());
                objDetails.add(objects.get(position).getMP3URL());
                intent.putStringArrayListExtra("PODCAST",objDetails);
                context.startActivity(intent);

            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EpisodeActivity.class);
                ArrayList<String> objDetails=new ArrayList<String>();
                objDetails.add(objects.get(position).getTitle());
                objDetails.add(objects.get(position).getImageURL());
                objDetails.add(objects.get(position).getDescription());
                objDetails.add(objects.get(position).getPublicationDate());
                objDetails.add(objects.get(position).getDuration());
                objDetails.add(objects.get(position).getMP3URL());
                intent.putStringArrayListExtra("PODCAST",objDetails);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
    public static interface IData{
        public void returnedValues(String URL) throws IOException;
    }
}
