package com.jai.covid_19trackerapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FeedFragment extends Fragment {

    ViewPager2 viewPager2;

    FeedAdapter feedAdapter;

    ArrayList<FeedData> arrayList;

    ProgressBar progressBar;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_feed, container, false);

        viewPager2=view.findViewById(R.id.viewpagerfeed);
        progressBar=view.findViewById(R.id.progressBar3);



        getData();



        return view;
    }
    void showRecyclerView() {


        feedAdapter = new FeedAdapter(getActivity(), arrayList);
        viewPager2.setAdapter(feedAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
    }


    void getData() {
        String url = "http://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=ee1cc19c977f415597eba56f74af36ef";

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                progressBar.setVisibility(View.GONE);
                if (response != null) {

                    try {
                        arrayList = new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(response.toString());

                        JSONArray jsonArray=jsonObject.getJSONArray("articles");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            JSONObject jsonObject2 = jsonObject1.getJSONObject("source");


                            arrayList.add(new FeedData(jsonObject1.getString("title"), jsonObject1.getString("urlToImage"), jsonObject1.getString("content"), jsonObject2.getString("name"), jsonObject1.getString("publishedAt"),jsonObject1.getString("url")));


                        }




                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    showRecyclerView();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();


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



}
