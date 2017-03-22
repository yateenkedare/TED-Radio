/*
    HomeWork 07
    PodcastGridAdapter.java
    Yateen Kedare | Rajdeep Rao
 */
package com.example.rajdeeprao.hw_07;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajdeeprao on 3/12/17.
 */

public class PodcastGridAdapter extends RecyclerView.Adapter<PodcastGridAdapter.ViewHolder> {
IData1 activity;
    Context context;
    int resource;
    List<PodcastItem> objects;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1;
        ImageView imageViewGrid,mediaButton;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1= (TextView) itemView.findViewById(R.id.textView);
            imageViewGrid= (ImageView) itemView.findViewById(R.id.imageView3);
            mediaButton= (ImageView) itemView.findViewById(R.id.imageView2);

        }
    }

    public PodcastGridAdapter(Context context, int resource, ArrayList<PodcastItem> podcastItemsList, IData1 activity) {
        this.context = context;
        this.resource = resource;
        objects=podcastItemsList;
        this.activity=activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resource, parent, false);
        ViewHolder vh=new PodcastGridAdapter.ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PodcastItem item= objects.get(position);
        holder.tv1.setText(item.getTitle());
        Log.d("IMAGEURL: ",item.getImageURL());
        Picasso.with(context)
                .load(item.getImageURL())
                .into(holder.imageViewGrid);
        holder.imageViewGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EpisodeActivity.class);
                ArrayList<String> objDetails=new ArrayList<String>();
                objDetails.add(item.getTitle());
                objDetails.add(item.getImageURL());
                objDetails.add(item.getDescription());
                objDetails.add(item.getPublicationDate());
                objDetails.add(item.getDuration());
                objDetails.add(item.getMP3URL());
                intent.putStringArrayListExtra("PODCAST",objDetails);
                context.startActivity(intent);
            }
        });

        holder.mediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    activity.returnedValuesGrid(objects.get(position).getMP3URL());
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
                objDetails.add(item.getTitle());
                objDetails.add(item.getImageURL());
                objDetails.add(item.getDescription());
                objDetails.add(item.getPublicationDate());
                objDetails.add(item.getDuration());
                objDetails.add(item.getMP3URL());
                intent.putStringArrayListExtra("PODCAST",objDetails);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static interface IData1{
        public void returnedValuesGrid(String URL) throws IOException;
    }
}