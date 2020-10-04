package com.jai.covid_19trackerapp;

import android.content.Context;
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

public class StateItemAdapter  extends RecyclerView.Adapter<StateItemAdapter.ImageViewHolder> implements Filterable {




    ArrayList<StateData> data;
    ArrayList<StateData> filteredList;



    Context context;




    public StateItemAdapter(ArrayList<StateData> arrayList,Context context)
    {


        data=arrayList;
        filteredList=new ArrayList<>(arrayList);

        this.context=context;

    }

    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.state_customlist,parent,false);
        ImageViewHolder imageViewHolder=new ImageViewHolder(view);

        return  imageViewHolder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {


        StateData stateData=data.get(position);

        holder.stateName.setText(stateData.getStateName());
        holder.stateTotalCases.setText(stateData.getConfirmed());
        holder.stateActive.setText(stateData.getActive());
        holder.stateDeaths.setText(stateData.getDeaths());
        holder.stateRecovered.setText(stateData.getRecovered());

        holder.constraintLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.list_anim));







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

                ArrayList<StateData> filtered=new ArrayList<>();

                String text=constraint.toString().toLowerCase().trim();

                if(text==null || text.isEmpty() || text.length()==0)
                {
                    filtered.addAll(filteredList);
                }
                else {

                    for (StateData stateData:data)
                    {
                        if(stateData.getStateName().toLowerCase().contains(text))
                        {
                            filtered.add(stateData);
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
                data.addAll((ArrayList<StateData>)results.values);
                notifyDataSetChanged();
            }
        };
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder
    {

        TextView stateName,stateTotalCases,stateDeaths,stateActive,stateRecovered;

        ConstraintLayout constraintLayout;




        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            stateName=itemView.findViewById(R.id.stateName);
            stateTotalCases=itemView.findViewById(R.id.stateConfirmedCount);
            stateDeaths=itemView.findViewById(R.id.stateDeathsCount);
            stateActive=itemView.findViewById(R.id.activeStateCasesCount);
            stateRecovered=itemView.findViewById(R.id.stateRecoveredCount);

            constraintLayout=itemView.findViewById(R.id.stateContainerItem);











        }
    }


}
