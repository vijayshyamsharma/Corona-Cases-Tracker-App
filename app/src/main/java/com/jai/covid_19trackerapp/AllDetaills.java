package com.jai.covid_19trackerapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class AllDetaills extends AppCompatActivity {

    String firstResponse;

    String updatedresponse;
    ArrayAdapter<String> ar;

    static TextView confirmed,deaths,recovered,todaycases,todaydeaths,activecases,criticalcases,deathspermillion;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.alldetails);

        firstResponse=getIntent().getExtras().get("dataall").toString();






        confirmed=findViewById(R.id.confirmedcount1);
        deaths=findViewById(R.id.deathcount1);
        recovered=findViewById(R.id.recoveredcount1);
        todaycases=findViewById(R.id.todayglobalcasescount);
        todaydeaths=findViewById(R.id.todayglobaldeathscount);
        activecases=findViewById(R.id.activeglobalcasescount);
        criticalcases=findViewById(R.id.criticalglobalcasescount);
        deathspermillion=findViewById(R.id.deathspermillioncount);

        if(firstResponse!=null)
        {
            try {

                JSONObject jsonObject=new JSONObject(firstResponse);

                confirmed.setText(jsonObject.getString("cases"));
                deaths.setText(jsonObject.getString("deaths"));
                recovered.setText(jsonObject.getString("recovered"));
                todaycases.setText(jsonObject.getString("todayCases"));
                todaydeaths.setText(jsonObject.getString("todayDeaths"));
                activecases.setText(jsonObject.getString("active"));
                criticalcases.setText(jsonObject.getString("critical"));
                deathspermillion.setText(""+(int)jsonObject.getDouble("deathsPerOneMillion"));
            }
            catch (Exception e){
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        }




    }




    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu,menu);


        MenuItem sort=menu.findItem(R.id.filter);


        MenuItem searchm=menu.findItem(R.id.search);


        sort.setVisible(false);
        searchm.setVisible(false);

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

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("all");

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




        return true;
    }



    void updateDataByTime(final String date)
    {
        if(date.equalsIgnoreCase("latest"))
        {
            dataUpdatefunc(firstResponse);
        }
        else
        {
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
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        }



    }

    void dataUpdatefunc(String response)
    {

        try {

            JSONObject jsonObject=new JSONObject(response);

            confirmed.setText(jsonObject.getString("cases"));
            deaths.setText(jsonObject.getString("deaths"));
            recovered.setText(jsonObject.getString("recovered"));
            todaycases.setText(jsonObject.getString("todayCases"));
            todaydeaths.setText(jsonObject.getString("todayDeaths"));
            activecases.setText(jsonObject.getString("active"));
            criticalcases.setText(jsonObject.getString("critical"));
            deathspermillion.setText(""+(int)jsonObject.getDouble("deathsPerOneMillion"));
        }
        catch (Exception e){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }


    }
}
