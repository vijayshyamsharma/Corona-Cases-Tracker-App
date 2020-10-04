package com.jai.covid_19trackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class States extends AppCompatActivity {

    String firstResponse;

    String updatedresponse;
    ArrayAdapter<String> ar;
    RecyclerView recyclerView;
    StateItemAdapter stateItemAdapter;
    ProgressBar progressBar;

    int lastsel=-1;

    SearchView searchView;

    AlertDialog alertDialog;




    String options[]={"Total Cases: high->low","Total Cases: low->high","Active Cases: high->low","Active Cases: low->high","Deaths: high->low","Deaths: low->high","Recovered: high->low","Recovered: low->high"};


    TextView totalStatestext;




    static ArrayList<StateData> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_states);

        arrayList=new ArrayList<>();
        String contname=getIntent().getExtras().get("contname").toString();

        getSupportActionBar().setTitle("States");


        recyclerView=findViewById(R.id.stateslist);
        totalStatestext=findViewById(R.id.totalstates);
        progressBar=findViewById(R.id.progressBar2);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getData();





    }


    void getData()
    {

        String url="https://api.covid19india.org/data.json";

        final RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                firstResponse=response.toString();


                progressBar.setVisibility(View.GONE);
                if(response!=null)
                {


                    try {


                        JSONObject jsonObject=new JSONObject(response);

                        JSONArray jsonArray=jsonObject.getJSONArray("statewise");

                        for(int i=1;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);





                            arrayList.add(new StateData(jsonObject1.getString("state"),jsonObject1.getString("recovered"),jsonObject1.getString("deaths"),jsonObject1.getString("active"),jsonObject1.getString("confirmed")));






                        }

                        totalStatestext.setText(jsonArray.length()-1+" states");











                    }
                    catch (Exception e){
                        Toast.makeText(States.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    showRecyclerView();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(States.this, "Error Occured", Toast.LENGTH_SHORT).show();


            }
        });

        requestQueue.add(stringRequest);
    }

    void showRecyclerView()
    {
         stateItemAdapter=new StateItemAdapter(arrayList,this);
        recyclerView.setAdapter(stateItemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem searchm=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchm);

        MenuItem item = menu.findItem(R.id.datewise);
        Spinner spinner = (Spinner) item.getActionView();



        ar=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                updateDataByTime(parent.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("indian states");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ar.clear();

                ar.add("latest");



                Iterable<DataSnapshot> dataSnapshotIterable=dataSnapshot.getChildren();

                for(DataSnapshot snapshot:dataSnapshotIterable)
                {
                    ar.add(snapshot.getKey().toString());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner.setAdapter(ar);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                try
                {
                    stateItemAdapter.getFilter().filter(newText);
                }
                catch (Exception e){

                }

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if(id==R.id.filter)
        {
            final AlertDialog.Builder builder=new AlertDialog.Builder(this).setTitle("sort using").


            setSingleChoiceItems(options, lastsel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    lastsel=which;
                    alertDialog.dismiss();
                    manipulate(which);


                }
            });


            alertDialog=builder.create();

            alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape));

            alertDialog.show();




        }
        else if(id==R.id.datewise)
        {
            final AlertDialog.Builder builder=new AlertDialog.Builder(this).setTitle("select date");




            alertDialog=builder.create();

            alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape));

            alertDialog.show();
        }

        return true;
    }


    void manipulate(int which)
    {
        switch (which)
        {
            case 0:

                Collections.sort(arrayList, new Comparator<StateData>() {
                    @Override
                    public int compare(StateData o1, StateData o2) {

                        long val1=Long.valueOf(o1.confirmed);
                        long val2=Long.valueOf(o2.confirmed);

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

                stateItemAdapter.notifyDataSetChanged();
                break;

            case 1:
                Collections.sort(arrayList, new Comparator<StateData>() {
                    @Override
                    public int compare(StateData o1, StateData o2) {

                        long val1=Long.valueOf(o1.confirmed);
                        long val2=Long.valueOf(o2.confirmed);

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

                stateItemAdapter.notifyDataSetChanged();
                break;

            case 2:
                Collections.sort(arrayList, new Comparator<StateData>() {
                    @Override
                    public int compare(StateData o1, StateData o2) {

                        long val1=Long.valueOf(o1.active);
                        long val2=Long.valueOf(o2.active);

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

                stateItemAdapter.notifyDataSetChanged();
                break;

            case 3:
                Collections.sort(arrayList, new Comparator<StateData>() {
                    @Override
                    public int compare(StateData o1, StateData o2) {

                        long val1=Long.valueOf(o1.active);
                        long val2=Long.valueOf(o2.active);

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

                stateItemAdapter.notifyDataSetChanged();
                break;

            case 4:
                Collections.sort(arrayList, new Comparator<StateData>() {
                    @Override
                    public int compare(StateData o1, StateData o2) {

                        long val1=Long.valueOf(o1.deaths);
                        long val2=Long.valueOf(o2.deaths);

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

                stateItemAdapter.notifyDataSetChanged();
                break;

            case 5:
                Collections.sort(arrayList, new Comparator<StateData>() {
                    @Override
                    public int compare(StateData o1, StateData o2) {

                        long val1=Long.valueOf(o1.deaths);
                        long val2=Long.valueOf(o2.deaths);

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

                stateItemAdapter.notifyDataSetChanged();
                break;

            case 6:



                stateItemAdapter.notifyDataSetChanged();

                Collections.sort(arrayList, new Comparator<StateData>() {
                    @Override
                    public int compare(StateData o1, StateData o2) {

                        long val1=Long.valueOf(o1.recovered);
                        long val2=Long.valueOf(o2.recovered);

                        if(val1==val2)
                        {
                            return 0;
                        }
                        else if(val1>val2)
                        {
                            return -1;
                        }
                        else {
                            return 1;
                        }


                    }
                });









                stateItemAdapter.notifyDataSetChanged();

                break;

            case 7:



                Collections.sort(arrayList, new Comparator<StateData>() {
                    @Override
                    public int compare(StateData o1, StateData o2) {

                        long val1=Long.valueOf(o1.recovered);
                        long val2=Long.valueOf(o2.recovered);

                        if(val1==val2)
                        {
                            return 0;
                        }
                        else if(val1>val2)
                        {
                            return 1;
                        }
                        else {
                            return -1;
                        }
                    }
                });


                stateItemAdapter.notifyDataSetChanged();
                break;

        }
    }

    void updateDataByTime(final String date)
    {
        if(date.equalsIgnoreCase("latest"))
        {
            dataUpdateFun(firstResponse);
        }
        else
        {
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("indian states").child(date);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                    updatedresponse=dataSnapshot.getValue().toString();



                    dataUpdateFun(updatedresponse.substring(updatedresponse.indexOf("{"),updatedresponse.lastIndexOf("}") + 1));


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }

    void dataUpdateFun(String reponse)
    {
        arrayList.clear();
         try {


                        JSONObject jsonObject=new JSONObject(reponse);

                        JSONArray jsonArray=jsonObject.getJSONArray("statewise");

                        for(int i=1;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            arrayList.add(new StateData(jsonObject1.getString("state"),jsonObject1.getString("recovered"),jsonObject1.getString("deaths"),jsonObject1.getString("active"),jsonObject1.getString("confirmed")));

                        }

                        stateItemAdapter.notifyDataSetChanged();

                        totalStatestext.setText(jsonArray.length()-1+" states");

                    }
                    catch (Exception e){

                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();

                    }

    }


}
