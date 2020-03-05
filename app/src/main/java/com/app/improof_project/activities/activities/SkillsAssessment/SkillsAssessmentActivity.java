package com.app.improof_project.activities.activities.SkillsAssessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.improof_project.R;
import com.app.improof_project.activities.activities.homepage_activity.Home;
import com.app.improof_project.activities.activities.login_activity.Login;
import com.app.improof_project.models.connection_model.connection_response.ConnectionResponseResult;
import com.app.improof_project.models.homepage_model.home_response.HomePageResponseResult;
import com.app.improof_project.models.login_model.login_response.LoginResponseResult;
import com.app.improof_project.models.rating_model.rating_request.RatingList;
import com.app.improof_project.models.rating_model.rating_request.RatingRequest;
import com.app.improof_project.models.skills_assessment_model.skills_assessment_request.SkillsAssessmentRequest;
import com.app.improof_project.models.skills_assessment_model.skills_assessment_response.SkillsAssessmentWrapper;
import com.app.improof_project.utils.AppController;
import com.app.improof_project.utils.CONSTANTS;
import com.app.improof_project.utils.PreferenceEntities;
import com.app.improof_project.utils.Preferences;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SkillsAssessmentActivity extends AppCompatActivity implements Serializable, View.OnClickListener, SkillsAssessmentAdapter.SendData {

    private static final String TAG = "SkillsAssessment";
    TextView person_title, profileName, designation, submit_button;
    RatingBar person_rating;
    SkillsAssessmentRequest skillsAssessmentRequest;
    HomePageResponseResult homePageResponseResult;
    RecyclerView recyclerView;
    private ConnectionResponseResult connectionResponseResult;
    LoginResponseResult loginResponseResult;
    Dialog dialog;
    ImageView profileImage;
    Button backButton;
    RatingRequest ratingRequest = new RatingRequest();
    JSONObject jsonObject;
    Button goToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_assessment);
        setIds();
        getSupportActionBar().hide();
        dialog = new Dialog(SkillsAssessmentActivity.this);
        recyclerView = findViewById(R.id.recyclerSkills);

        startAnim();
        skillsAssessmentRequest = new SkillsAssessmentRequest();
        connectionResponseResult = (ConnectionResponseResult) getIntent().getSerializableExtra("adapterdata");
        loginResponseResult = new Gson().fromJson(Preferences.getPreference(this, PreferenceEntities.USER_DETAILS), LoginResponseResult.class);
        SkillsAssessmentRequest skillsAssessmentRequest = new SkillsAssessmentRequest();
        skillsAssessmentRequest.setApp_key(CONSTANTS.APPKEY);
        skillsAssessmentRequest.setC_id(String.valueOf(connectionResponseResult.getC_id()));
        skillsAssessmentRequest.setGiven_by(loginResponseResult.getId());
        skillsAssessmentRequest.setGiven_to(String.valueOf(connectionResponseResult.getId()));

        Gson gson = new Gson();
        homePageResponseResult = gson.fromJson(Preferences.getPreference(this, PreferenceEntities.USER_DETAILS), HomePageResponseResult.class);
        //called API
        apiCallSkillsAssessment(skillsAssessmentRequest);

        backButton.setOnClickListener(this);
    }

    @Override
    public void onClickSubmitButton(List<RatingList> assessmentList) {
        for (int i = 0; i < assessmentList.size(); i++) {
            if (assessmentList.get(i).getRate() > 0) {
                submit_button.setEnabled(true);
                submit_button.setBackgroundColor(Color.BLUE);
                submit_button.setOnClickListener(this);
                break;
            } else {
                submit_button.setEnabled(false);
                submit_button.setBackgroundColor(Color.GRAY);
            }
        }
    }

    public void setIds() {
        person_title = findViewById(R.id.person_title);
        person_rating = findViewById(R.id.person_rating);
        profileName = findViewById(R.id.profileName);
        designation = findViewById(R.id.profileDesination);
        backButton = findViewById(R.id.backButton);
        profileImage = findViewById(R.id.profileImage);
        submit_button = findViewById(R.id.submit_button);
        goToHome = findViewById(R.id.goToHome);
    }

    private void apiCallSkillsAssessment(SkillsAssessmentRequest skillsAssessmentRequest) {

        Gson gson = new Gson();
        final String assessmentRequestJson = gson.toJson(skillsAssessmentRequest);
        /*
        String to Json Object for Request
         */
        JSONObject jsonObjectAssessment = null;
        try {
            jsonObjectAssessment = new JSONObject(assessmentRequestJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, CONSTANTS.BASE_URL + CONSTANTS.MEMBER_RATING_SUMMARY, jsonObjectAssessment, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            stopAnim();
                            Gson gson = new Gson();
                            SkillsAssessmentWrapper skillsAssessmentWrapper = gson.fromJson(response.toString(), SkillsAssessmentWrapper.class);
                            //set Recycler
                            initRecyclerViewSkillsAssessment(skillsAssessmentWrapper);
                            //set User data
                            setData();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stopAnim();
                        Log.i(TAG + "error", error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void initRecyclerViewSkillsAssessment(SkillsAssessmentWrapper skillsAssessmentWrapper) {

        ratingRequest.setApp_key(CONSTANTS.APPKEY);
        ratingRequest.setC_id(String.valueOf(homePageResponseResult.getC_id()));
        ratingRequest.setGiven_by(homePageResponseResult.getId());
        ratingRequest.setGiven_to(String.valueOf(connectionResponseResult.getId()));

        List<RatingList> list = new ArrayList<>();
        for (int i = 0; i < skillsAssessmentWrapper.getData().getResult().size(); i++) {
            RatingList ratingList = new RatingList();
            ratingList.setRate(0.0f);
            ratingList.setSkill_id(String.valueOf(skillsAssessmentWrapper.getData().getResult().get(i).getSkill_id()));
            list.add(ratingList);
        }

        ratingRequest.setRating(list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        SkillsAssessmentAdapter adapter = new SkillsAssessmentAdapter(this, skillsAssessmentWrapper.getData().getResult(), ratingRequest, this);
        recyclerView.setAdapter(adapter);
    }

    //Rating API send data
    private void apiCallRating(RatingRequest ratingRequest) {
        try {
            Gson gson = new Gson();
            jsonObject = new JSONObject(gson.toJson(ratingRequest));

        } catch (Exception e) {
            Log.i("Rating Data", e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, CONSTANTS.BASE_URL + CONSTANTS.RATING, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (Integer.parseInt(response.getJSONObject("data").getString("status")) == 1) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "Error" + response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error on api", error.toString());
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    //set profile data
    private void setData() {
        profileName.setText(getIntent().getStringExtra("personName"));
        designation.setText(getIntent().getStringExtra("designation"));
        Glide.with(SkillsAssessmentActivity.this).load("http://13.126.99.14/improof/storage/app/" + getIntent().getStringExtra("ImageUrl")).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(profileImage);
    }

    //Back Button
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                //Back Button
                Intent i = new Intent(SkillsAssessmentActivity.this, Home.class);
                startActivity(i);
                finish();
                break;
            case R.id.submit_button:
                //create Custom dialog
                if (isConnected(this)) {
                    apiCallRating(ratingRequest);
                    createDialog();
                    break;
                }
                else {
                    Toast.makeText(this, "No Internet ! Please Turn on Internet", Toast.LENGTH_SHORT).show();
                }
        }

    }

    //set Dialog
    private void createDialog() {
        final Dialog homeDialog = new Dialog(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.rate_dailog, null, false);
        //set title
        homeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        homeDialog.setCancelable(false);
        homeDialog.setContentView(view);
        homeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        homeDialog.show();
        //set Button
        Button btnGoHome = homeDialog.findViewById(R.id.goToHome);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goTOHome = new Intent(SkillsAssessmentActivity.this, Home.class);
                goTOHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goTOHome);
                finish();
                homeDialog.dismiss();
            }
        });
    }

    //Check Internet
    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    void startAnim() {
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    void stopAnim(){
        dialog.dismiss();
    }
}
