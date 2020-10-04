package com.jai.covid_19trackerapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ImageViewHolder> {

    Date date;

    Context context;
    ArrayList<FeedData> feedData;

    FeedAdapter(Context context, ArrayList<FeedData> feedData)
    {
        this.context=context;
        this.feedData=feedData;
    }
    @NonNull
    @Override
    public FeedAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customfeed,parent,false);

        ImageViewHolder imageViewHolder=new ImageViewHolder(view);

        return imageViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        final ImageViewHolder holder1=holder;

        final FeedData feedData1=feedData.get(position);

        holder.feedtitle.setText(feedData1.getFeedtitle());
        if(feedData1.getFeedcontent().isEmpty()==false | feedData1.getFeedcontent()!=null | feedData1.getFeedcontent().length()>0)
        {
            holder.feedcontent.setText(feedData1.getFeedcontent()+"...");

        }
        else {

            holder.feedcontent.setText("read by tapping at below link");
        }

        holder.feedsource.setText(feedData1.getFeedsource()+"-");

        String s=feedData1.getFeedtime().replace("T"," ").replace("Z"," ");


        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try
        {
             date=simpleDateFormat.parse(s);
        }
        catch (Exception e){}





        holder.feedtime.setText(date.toString());

        holder.originallink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(feedData1.getOriginallink()));
                context.startActivity(intent);

            }
        });

        Picasso.with(context).load(feedData1.getFeedimage()).fit().centerInside().error(context.getResources().getDrawable(android.R.drawable.alert_light_frame)).into(holder.feedimage, new Callback() {
            @Override
            public void onSuccess() {
                holder1.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

                holder1.progressBar.setVisibility(View.GONE);

                Toast.makeText(context, "Image Can't load", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public int getItemCount() {
        return feedData.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder
    {

        TextView feedtitle,feedcontent,feedsource,feedtime,originallink;
        ImageView feedimage;
        ProgressBar progressBar;

        ConstraintLayout constraintLayout;




        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);


            feedtitle=itemView.findViewById(R.id.titlefeed);
            feedcontent=itemView.findViewById(R.id.contentfeed);
            feedsource=itemView.findViewById(R.id.sourcefeed);
            feedtime=itemView.findViewById(R.id.timefeed);
            feedimage=itemView.findViewById(R.id.imagefeed);

            originallink=itemView.findViewById(R.id.readmore);
            progressBar=itemView.findViewById(R.id.progressBar4);

        }
    }
}
