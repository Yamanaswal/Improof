package com.app.improof_project.fragments.home_fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.improof_project.R;
import com.app.improof_project.activities.activities.login_activity.Login;
import com.app.improof_project.models.homepage_model.home_request.HomePageRequest;
import com.app.improof_project.models.homepage_model.home_response.HomePageResponseResult;
import com.app.improof_project.models.homepage_model.home_response.HomePageResponseWrapper;
import com.app.improof_project.models.login_model.login_response.LoginResponseResult;
import com.app.improof_project.utils.AppController;
import com.app.improof_project.utils.CONSTANTS;
import com.app.improof_project.utils.PreferenceEntities;
import com.app.improof_project.utils.Preferences;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.improof_project.utils.CONSTANTS.APPKEY;

public class HomePageFragment extends Fragment {

    private static final String TAG = "HomePageFragment";
    private Button logout;
    private View view;
    private CircleImageView placeholderImage;
    private TextView personName, personDesignation, evalutions_id, overall_rating, company_name, allStar;
    private JSONObject jsonObjectHomePage;
    RatingBar ratingbar_id;
    Dialog dialog;
    private LoginResponseResult loginResponseResult;
    List<String> coolestSkills = new ArrayList<>();
    private DecimalFormat decimalFormat = new DecimalFormat("#.#");

    //The RecyclerView
    private RecyclerView recyclerView, recyclerViewCoolest;

    public HomePageFragment() {
        // Required empty public constructor
    }

    public void setIds(View view) {
        recyclerView = view.findViewById(R.id.performance_skills_recycler);
        recyclerViewCoolest = view.findViewById(R.id.coolest_skills);
        placeholderImage = view.findViewById(R.id.placeholder_image_id);
        personName = view.findViewById(R.id.person_id);
        personDesignation = view.findViewById(R.id.person_designation);
        logout = view.findViewById(R.id.logout);
        ratingbar_id = view.findViewById(R.id.ratingbar_id);
        evalutions_id = view.findViewById(R.id.evalutions_id);
        overall_rating = view.findViewById(R.id.overall_rating);
        company_name = view.findViewById(R.id.company_name);
        allStar = view.findViewById(R.id.allstar_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if(isConnected(getActivity())){
            view = inflater.inflate(R.layout.fragment_home_page, container, false);
            setIds(view);
            dialog = new Dialog(getActivity());

            startAnim();

            HomePageRequest homePageRequest = new HomePageRequest();
            loginResponseResult = new Gson().fromJson(Preferences.getPreference(getActivity(), PreferenceEntities.USER_DETAILS), LoginResponseResult.class);
            homePageRequest.setC_id(loginResponseResult.getC_id());
            homePageRequest.setApp_key(APPKEY);
            homePageRequest.setId(loginResponseResult.getId());

            apiCallHome(homePageRequest);

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLogOutClick();
                }
            });

        }else {
            new AlertDialog.Builder(getActivity()).setTitle("Check Internet Connection !!").setCancelable(false).setPositiveButton("ok",null).show();
        }
        return view;
    }

    //Set data From RecyclerView in Performance Skills
    private void initRecyclerViewSkills(HomePageResponseResult homePageResponseResult) {

        //Lower recyclerView
        CoolestSkillsAdapter coolestSkillsAdapter = new CoolestSkillsAdapter(getActivity(), coolestSkills);
        recyclerViewCoolest.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewCoolest.setHasFixedSize(true);
        recyclerViewCoolest.setAdapter(coolestSkillsAdapter);

        //Upper RecyclerView
        if (homePageResponseResult.getSkills() != null && homePageResponseResult.getSkills().size() > 0) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            SkillsAdapter adapter = new SkillsAdapter(getActivity(), homePageResponseResult.getSkills());
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "List Found Null", Toast.LENGTH_SHORT).show();
        }

        //logic
        int people = 0;
        double rating = 0;
        for (int i = 0; i < homePageResponseResult.getSkills().size(); i++) {
            people = homePageResponseResult.getSkills().get(i).getCount();
            rating = homePageResponseResult.getSkills().get(i).getRate();
            double y = Double.parseDouble(rating(rating, people));
            Log.d("Size of List", String.valueOf(homePageResponseResult.getSkills().size()));
            if (y > 4 && y <= 5) {
                coolestSkills.add(homePageResponseResult.getSkills().get(i).getTitle());
            }
            Log.i("Y value", y + "");
        }

    }

    //click the logout button
    private void onLogOutClick() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Log Out");
        builder.setMessage("Do You Want to Sign Out !! ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Preferences.removePreference(getActivity(), PreferenceEntities.USER_DETAILS);
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    //Api Call Method for HomePageWrapper Model
    private void apiCallHome(HomePageRequest homePageRequest) {

        try {
            Gson gson = new Gson();
            jsonObjectHomePage = new JSONObject(gson.toJson(homePageRequest));

        } catch (Exception e) {
            Log.i("LoginResponseData", e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, CONSTANTS.BASE_URL + CONSTANTS.MEMBERDATA, jsonObjectHomePage, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            //getting data from API
                            stopAnim();
                            Gson gson = new Gson();
                            Log.i(TAG, "hitting api: response : " + new Gson().toJson(response));

                            HomePageResponseWrapper homePageWrapper = gson.fromJson(response.toString(), HomePageResponseWrapper.class);
                            setHomePageData(homePageWrapper.getData().getResult());

                            //set data in Recycler
                            initRecyclerViewSkills(homePageWrapper.getData().getResult());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG + "error", error.getMessage());
                        //Dismiss Dialog
                        stopAnim();

                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    //Set Data on Home Page
    private void setHomePageData(HomePageResponseResult homePageResponseResult) {
         /*
        Image to Profile Pic
         */
        if (isAdded()) {
            Glide.with(getActivity()).load("http://13.126.99.14/improof/storage/app/" + homePageResponseResult.getPic_path()).placeholder(R.mipmap.placeholder).into(placeholderImage);
        }
        /*
        Profile Data
         */
        company_name.setText(homePageResponseResult.getCompany_name());
        personName.setText(homePageResponseResult.getName());
        personDesignation.setText(homePageResponseResult.getDesignation());

        int totalEval = 0;
        float totalrate = 0;
        for (int i = 0; i < homePageResponseResult.getSkills().size(); i++) {
            totalEval += homePageResponseResult.getSkills().get(i).getCount();
            totalrate += homePageResponseResult.getSkills().get(i).getRate();
        }

        float rate = (totalrate / totalEval);
        overall_rating.setText(String.valueOf(rate));
        ratingbar_id.setRating(rate);
        allStar.setText(allStar(rate));
        evalutions_id.setText("(" + totalEval + " Evaluations)");
    }

    private String rating(double rating, int people) {
        double res = rating / people;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        nf.setGroupingUsed(false);
        return decimalFormat.format(res);
    }

    private String allStar(double x) {
        Log.i("allset", x + "");
        if (x == 0) {
            return "No Rating";
        } else if (x > 0 && x < 1)
            return "Work harder!";
        else if (x >= 1.0 && x < 2.0) {
            return "Don\\'t give up";
        } else if (x >= 2.0 && x < 2.0)
            return "Good";
        else if (x >= 3.0 && x < 4.0)
            return "Excellent";
        else
            return "All Star!";
    }

    //Build dialog
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

    private void startAnim() {
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void stopAnim(){
        dialog.dismiss();
    }

}
