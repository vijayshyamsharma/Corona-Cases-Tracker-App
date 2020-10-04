package com.jai.covid_19trackerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> implements Filterable {




    ArrayList<CountryData> data;
    ArrayList<CountryData> filteredList;



    Context context;




    public RecyclerAdapter(ArrayList<CountryData> arrayList,Context context)
    {

       data=arrayList;
       filteredList=new ArrayList<>(arrayList);


       this.context=context;

    }

    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_covid,parent,false);
        ImageViewHolder imageViewHolder=new ImageViewHolder(view);

        return  imageViewHolder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {


        final CountryData countryData=data.get(position);

        holder.contName.setText(countryData.getContName());
        holder.totalcases.setText(countryData.getTotalCases());
        holder.todaycases.setText(countryData.getTodayCases());
        holder.deaths.setText(countryData.getTotalDeaths());
        holder.recovered.setText(countryData.getTotalRecovered());

        //holder.constraintLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.list_anim));

        if(countryData.getContName().equalsIgnoreCase("India"))
        {
            holder.states.setText("(available) show all");

            holder.states.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context,States.class);
                    intent.putExtra("contname",countryData.getContName());
                    context.startActivity(intent);
                }
            });
        }
        else {
            holder.states.setText("Not Available");
        }

        holder.constraintLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.list_anim));

        Glide.with(context).load(countryData.getFlag()).apply(new RequestOptions().override(240,160)).into(holder.flag);



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                ArrayList<CountryData> filtered=new ArrayList<>();

                String text=constraint.toString().toLowerCase().trim();

                if(text==null || text.isEmpty() || text.length()==0)
                {
                        filtered.addAll(filteredList);
                }
                else {

                    for (CountryData countryData:data)
                    {
                        if(countryData.getContName().toLowerCase().contains(text))
                        {
                            filtered.add(countryData);
                        }
                    }
                }

                FilterResults filterResults=new FilterResults();
                filterResults.values=filtered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data.clear();
                data.addAll((ArrayList<CountryData>)results.values);
                notifyDataSetChanged();
            }
        };
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder
    {

        TextView contName,totalcases,todaycases,deaths,recovered,states;
        ImageView flag;
        ConstraintLayout constraintLayout;




        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            contName=itemView.findViewById(R.id.stateName);
            totalcases=itemView.findViewById(R.id.stateConfirmedCount);
            todaycases=itemView.findViewById(R.id.activeCasesCount);
            deaths=itemView.findViewById(R.id.stateDeathsCount);
            recovered=itemView.findViewById(R.id.stateRecoveredCount);
            states=itemView.findViewById(R.id.textView13);
            flag=itemView.findViewById(R.id.imageView);

            constraintLayout=itemView.findViewById(R.id.containeritem);








        }
    }


}
