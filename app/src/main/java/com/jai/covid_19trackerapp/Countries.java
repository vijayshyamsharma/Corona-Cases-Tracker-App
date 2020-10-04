package com.jai.covid_19trackerapp;


import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.appcompat.widget.SearchView;



public class Countries extends Fragment {

    static String updatedresponse;

    static String firstResponse;

    static FragmentActivity fragmentActivity;


    SearchView.OnQueryTextListener onQueryTextListener;

    SearchView searchView;






    static TextView totalcont;

    RecyclerView recyclerView;

    ProgressBar progressBar;

    static ArrayList<CountryData> arrayList;

    static RecyclerAdapter recyclerAdapter;


    public Countries() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        arrayList = new ArrayList<>();
        getData();

        fragmentActivity=getActivity();


        View view = inflater.inflate(R.layout.fragment_countries, container, false);
        recyclerView = view.findViewById(R.id.contitems);
        progressBar = view.findViewById(R.id.progressBar1);
        totalcont = view.findViewById(R.id.totalcont);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));





        return view;
    }


    void showRecyclerView() {


        recyclerAdapter = new RecyclerAdapter(arrayList, getActivity());
        recyclerView.setAdapter(recyclerAdapter);
    }


    void getData() {
        String url = "https://corona.lmao.ninja/v2/countries";

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                progressBar.setVisibility(View.GONE);
                if (response != null) {

                    firstResponse=response;

                    try {


                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            JSONObject jsonObject1 = jsonObject.getJSONObject("countryInfo");


                            arrayList.add(new CountryData(jsonObject.getString("todayCases"), jsonObject.getString("cases"), jsonObject.getString("deaths"), jsonObject.getString("recovered"), jsonObject.getString("country"), jsonObject1.getString("flag")));


                        }

                        totalcont.setText(jsonArray.length() + " countries");


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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
    }


    static void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                try {
                    recyclerAdapter.getFilter().filter(newText);
                } catch (Exception e) {

                }

                return true;
            }
        });
    }


    static void manipulate(int which) {
        switch (which) {
            case 0:

                Collections.sort(arrayList, new Comparator<CountryData>() {
                    @Override
                    public int compare(CountryData o1, CountryData o2) {

                        long val1=Long.valueOf(o1.totalCases);
                        long val2=Long.valueOf(o2.totalCases);

                        if(val1==val2)
                        {
                            return 0;
                        }
                        else if(val1>val2)
                        {
                            return -1;

                        }
                        else
                        {
                            return 1;
                        }




                    }


                });

                recyclerAdapter.notifyDataSetChanged();



                break;

            case 1:


                Collections.sort(arrayList, new Comparator<CountryData>() {
                    @Override
                    public int compare(CountryData o1, CountryData o2) {

                        long val1=Long.valueOf(o1.totalCases);
                        long val2=Long.valueOf(o2.totalCases);

                        if(val1==val2)
                        {
                            return 0;
                        }
                        else if(val1>val2)
                        {
                            return 1;

                        }
                        else
                        {
                            return -1;
                        }




                    }


                });

                recyclerAdapter.notifyDataSetChanged();
                break;

            case 2:

                Collections.sort(arrayList, new Comparator<CountryData>() {
                    @Override
                    public int compare(CountryData o1, CountryData o2) {

                        long val1=Long.valueOf(o1.todayCases);
                        long val2=Long.valueOf(o2.todayCases);

                        if(val1==val2)
                        {
                            return 0;
                        }
                        else if(val1>val2)
                        {
                            return -1;

                        }
                        else
                        {
                            return 1;
                        }




                    }


                });

                recyclerAdapter.notifyDataSetChanged();
                break;

            case 3:

                Collections.sort(arrayList, new Comparator<CountryData>() {
                    @Override
                    public int compare(CountryData o1, CountryData o2) {

                        long val1=Long.valueOf(o1.todayCases);
                        long val2=Long.valueOf(o2.todayCases);

                        if(val1==val2)
                        {
                            return 0;
                        }
                        else if(val1>val2)
                        {
                            return 1;

                        }
                        else
                        {
                            return -1;
                        }




                    }


                });

                recyclerAdapter.notifyDataSetChanged();
                break;

            case 4:

                Collections.sort(arrayList, new Comparator<CountryData>() {
                    @Override
                    public int compare(CountryData o1, CountryData o2) {

                        long val1=Long.valueOf(o1.totalDeaths);
                        long val2=Long.valueOf(o2.totalDeaths);

                        if(val1==val2)
                        {
                            return 0;
                        }
                        else if(val1>val2)
                        {
                            return -1;

                        }
                        else
                        {
                            return 1;
                        }




                    }


                });

                recyclerAdapter.notifyDataSetChanged();
                break;

            case 5:

                Collections.sort(arrayList, new Comparator<CountryData>() {
                    @Override
                    public int compare(CountryData o1, CountryData o2) {

                        long val1=Long.valueOf(o1.totalDeaths);
                        long val2=Long.valueOf(o2.totalDeaths);

                        if(val1==val2)
                        {
                            return 0;
                        }
                        else if(val1>val2)
                        {
                            return 1;

                        }
                        else
                        {
                            return -1;
                        }




                    }


                });

                recyclerAdapter.notifyDataSetChanged();
                break;

            case 6:

                Collections.sort(arrayList, new Comparator<CountryData>() {
                    @Override
                    public int compare(CountryData o1, CountryData o2) {

                        long val1=Long.valueOf(o1.totalRecovered);
                        long val2=Long.valueOf(o2.totalRecovered);

                        if(val1==val2)
                        {
                            return 0;
                        }
                        else if(val1>val2)
                        {
                            return -1;

                        }
                        else
                        {
                            return 1;
                        }




                    }


                });



                recyclerAdapter.notifyDataSetChanged();
                break;

            case 7:

                Collections.sort(arrayList, new Comparator<CountryData>() {
                    @Override
                    public int compare(CountryData o1, CountryData o2) {

                        long val1=Long.valueOf(o1.totalRecovered);
                        long val2=Long.valueOf(o2.totalRecovered);

                        if(val1==val2)
                        {
                            return 0;
                        }
                        else if(val1>val2)
                        {
                            return 1;

                        }
                        else
                        {
                            return -1;
                        }




                    }


                });

                recyclerAdapter.notifyDataSetChanged();
                break;

        }
    }

    static void updateDataByTime(String date)
    {
        if(date.equalsIgnoreCase("latest"))
        {
            updateDataFun(firstResponse);
        }
        else {

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("countries").child(date);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    updatedresponse=dataSnapshot.getValue().toString();



                    updateDataFun(updatedresponse.substring(updatedresponse.indexOf("["),updatedresponse.lastIndexOf("]") + 1));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


    static  void updateDataFun(String response)
    {
        arrayList.clear();

        try {


            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                JSONObject jsonObject1 = jsonObject.getJSONObject("countryInfo");


                arrayList.add(new CountryData(jsonObject.getString("todayCases"), jsonObject.getString("cases"), jsonObject.getString("deaths"), jsonObject.getString("recovered"), jsonObject.getString("country"), jsonObject1.getString("flag")));


            }

            recyclerAdapter.notifyDataSetChanged();

            totalcont.setText(jsonArray.length() + " countries");


        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
