package com.app.improof_project.activities.activities.splash_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.app.improof_project.R;
import com.app.improof_project.activities.activities.homepage_activity.Home;
import com.app.improof_project.activities.activities.login_activity.Login;
import com.app.improof_project.utils.PreferenceEntities;
import com.app.improof_project.utils.Preferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isConnected(MainActivity.this)){ buildDialog(MainActivity.this).show();}
        else {
            setContentView(R.layout.activity_main);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!Preferences.getPreference(getApplicationContext(), PreferenceEntities.USER_DETAILS).isEmpty()) {
                        Intent i = new Intent(MainActivity.this, Home.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(MainActivity.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, 1000);
        }
    }

    //Build dialog
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }

    //Check Internet
    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }
}
