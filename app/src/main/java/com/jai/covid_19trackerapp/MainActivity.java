package com.jai.covid_19trackerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    FrameLayout placeholederfragment;



    ArrayAdapter<String> ar;

    int lastsel=-1;

    ArrayList<String> arrayList;

    static boolean check=false;

    BottomNavigationView bnview;

    HomeFragment homeFragment;
    Countries countriesFragment;
    FeedFragment feedFragment;

    AlertDialog alertDialog;

    String options[]={"Total Cases: high->low","Total Cases: low->high","Today Cases: high->low","Today Cases: low->high","Deaths: high->low","Deaths: low->high","Recovered: high->low","Recovered: low->high"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList=new ArrayList<>();








        placeholederfragment=findViewById(R.id.fmlayout);
        bnview=findViewById(R.id.bnview);

        homeFragment=new HomeFragment();
        countriesFragment=new Countries();
        feedFragment=new FeedFragment();


        showFragment(homeFragment,"home fragment");


        bnview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                switch (id)
                {
                    case R.id.home:
                        check=false;
                        getSupportActionBar().show();
                        getSupportActionBar().setTitle("Covid-19 TrackerApp");
                        showFragment(homeFragment,"home fragment");

                        break;

                    case R.id.world:
                        check=true;
                        getSupportActionBar().show();
                        getSupportActionBar().setTitle("Countries");
                        showFragment(countriesFragment,"countries fragment");
                        break;

                        case R.id.feed:
                            getSupportActionBar().hide();
                            showFragment(feedFragment,"news fragment");
                            break;

                }

                return true;
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu,menu);

        final MenuItem searchm=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)MenuItemCompat.getActionView(searchm);
        MenuItem item = menu.findItem(R.id.datewise);
        Spinner spinner = (Spinner) item.getActionView();








         ar=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                HomeFragment homeFragment=(HomeFragment)getSupportFragmentManager().findFragmentByTag("home fragment");
                Countries countries=(Countries) getSupportFragmentManager().findFragmentByTag("countries fragment");

                if(homeFragment!=null && homeFragment.isVisible())
                {
                    HomeFragment.updateDataByTime(parent.getSelectedItem().toString());
                }
                else if(countries!=null && countries.isVisible())
                {
                    Countries.updateDataByTime(parent.getSelectedItem().toString());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        HomeFragment homeFragment1=(HomeFragment)getSupportFragmentManager().findFragmentByTag("home fragment");
        Countries countries1=(Countries) getSupportFragmentManager().findFragmentByTag("countries fragment");

        if(homeFragment1!=null && homeFragment1.isVisible())
        {

            final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("all");

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
        }
        else if(countries1!=null && countries1.isVisible())
        {

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("countries");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ar.clear();
                    ar.add("latest");

                    Iterable<DataSnapshot> dataSnapshotIterable=dataSnapshot.getChildren();

                    for(DataSnapshot snapshot:dataSnapshotIterable)
                    {
                        ar.add(snapshot.getKey());

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



            spinner.setAdapter(ar);



        }



        Countries.search(searchView);

        return true;
    }

    void showFragment(Fragment fragment,String fragmentName)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fmlayout,fragment,fragmentName).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.filter) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setTitle("sort using").


                    setSingleChoiceItems(options, lastsel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            lastsel=which;
                            alertDialog.dismiss();


                            Countries.manipulate(which);
                        }
                    });

            alertDialog = builder.create();



            alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape));


            alertDialog.show();


        }


        return true;
    }
}
