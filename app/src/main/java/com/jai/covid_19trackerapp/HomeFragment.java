package com.jai.covid_19trackerapp;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeFragment extends Fragment {

    static String updatedresponse;

    static FragmentActivity fragmentActivity;




    static TextView totalconfirmed,totaldeaths,totalrecovered,updatestatus;
    ProgressBar progressBar;
    TextView allDetail;

    static String dataresponse;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getData();

        fragmentActivity=getActivity();

        View view= inflater.inflate(R.layout.fragment_home, container, false);

        totalconfirmed=view.findViewById(R.id.confirmedcount1);
        totaldeaths=view.findViewById(R.id.deathcount1);
        totalrecovered=view.findViewById(R.id.recoveredcount1);
        progressBar=view.findViewById(R.id.progressBar);
        updatestatus=view.findViewById(R.id.updatedtext);
        allDetail=view.findViewById(R.id.alldetails);

        allDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(),AllDetaills.class);
                intent.putExtra("dataall",dataresponse);

                startActivity(intent);

            }
        });

        return view;

    }

    static String getDate(long millis)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss aaa");

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        return simpleDateFormat.format(calendar.getTime());
    }

    void getData()
    {
        String url="https://corona.lmao.ninja/v2/all";

        final RequestQueue requestQueue= Volley.newRequestQueue(getActivity());

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataresponse=response.toString();



                progressBar.setVisibility(View.GONE);
                if(response!=null)
                {
                    try {
                        JSONObject jsonObject=new JSONObject(response.toString());

                        totalconfirmed.setText(jsonObject.getString("cases"));
                        totaldeaths.setText(jsonObject.getString("deaths"));
                        totalrecovered.setText(jsonObject.getString("recovered"));

                        updatestatus.setText("Last Updated\n"+getDate(jsonObject.getLong("updated")));
                    }
                    catch (Exception e){}


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);

            }
        });

        requestQueue.add(stringRequest);
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {

        MenuItem menuItem=menu.findItem(R.id.search);
        MenuItem menuItem1=menu.findItem(R.id.filter);

        menuItem1.setVisible(false);
        menuItem.setVisible(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    static void updateDataByTime(String date)
    {
        if(date.equalsIgnoreCase("latest"))
        {
            dataUpdatefunc(dataresponse);
        }
        else {

            try
            {
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("all").child(date);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            updatedresponse=dataSnapshot.getValue().toString();



                            dataUpdatefunc(updatedresponse.substring(updatedresponse.indexOf("{"),updatedresponse.lastIndexOf("}") + 1));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            }
            catch (Exception e){
                Toast.makeText(fragmentActivity, "error", Toast.LENGTH_SHORT).show();
            }


        }




    }


    static void dataUpdatefunc(String response)
    {
        try {
            JSONObject jsonObject=new JSONObject(response.toString());

            totalconfirmed.setText(jsonObject.getString("cases"));
            totaldeaths.setText(jsonObject.getString("deaths"));
            totalrecovered.setText(jsonObject.getString("recovered"));

            updatestatus.setText("Last Updated\n"+getDate(jsonObject.getLong("updated")));
        }
        catch (Exception e){
            Toast.makeText(fragmentActivity, "error", Toast.LENGTH_SHORT).show();
        }


    }

}

