package com.app.improof_project.activities.activities.homepage_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.improof_project.R;
import com.app.improof_project.fragments.connection_fragment.ConnectionsFragment;
import com.app.improof_project.fragments.home_fragments.HomePageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Close App ?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //terminate application

                finish();
            }

        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //terminate dialog
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Close Application");
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        if (!isNetworkAvailable(Home.this)){
            Toast.makeText(this, "No Internet !!", Toast.LENGTH_SHORT).show();
        }else {
            //Bottom Navigation
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);

            //I added this if statement to keep the selected fragment when rotating the device
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                        new HomePageFragment()).commit();
            }
        }
    }
    

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment selectedFragment = null;
        switch (item.getItemId()){
            case R.id.nav_home:
                selectedFragment = new HomePageFragment();
                break;
            case R.id.nav_connection:
                selectedFragment = new ConnectionsFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
        return true;
    }

    //Check Internet
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
