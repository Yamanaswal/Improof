package com.app.improof_project.fragments.connection_fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.improof_project.R;
import com.app.improof_project.models.connection_model.connection_request.ConnectionRequest;
import com.app.improof_project.models.connection_model.connection_response.ConnectionResponseResult;
import com.app.improof_project.models.connection_model.connection_response.ConnectionResponseWrapper;
import com.app.improof_project.models.homepage_model.home_response.HomePageResponseResult;
import com.app.improof_project.utils.AppController;
import com.app.improof_project.utils.CONSTANTS;
import com.app.improof_project.utils.PreferenceEntities;
import com.app.improof_project.utils.Preferences;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import static com.app.improof_project.utils.CONSTANTS.APPKEY;

public class ConnectionsFragment extends Fragment {

    private static final String TAG = "ConnectionsFragment";
    private View view;
    private HomePageResponseResult homePageResponseResult;
    private JSONObject jsonObjectConnection;
    private Dialog dialog;
    private SearchView searchView;
    ConnectionAdapter adapter;
    EditText searchEditText;

    public ConnectionsFragment() {
        // Required empty public constructor
    }

    //The RecyclerView
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_connections, container, false);
        recyclerView = view.findViewById(R.id.connection_recycler_id);
        searchView = view.findViewById(R.id.searchView);
        //Search by Edit text
        searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Search by SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        dialog = new Dialog(getActivity());
        startAnim();
        homePageResponseResult = new HomePageResponseResult();

        ConnectionRequest connectionRequest = new ConnectionRequest();

        homePageResponseResult = new Gson().fromJson(Preferences.getPreference(getActivity(), PreferenceEntities.USER_DETAILS), HomePageResponseResult.class);

        connectionRequest.setCompany_id(String.valueOf(homePageResponseResult.getC_id()));
        connectionRequest.setMember_id(homePageResponseResult.getId());
        connectionRequest.setApp_key(APPKEY);

        apiCall(connectionRequest);

        return view;
    }

    private void apiCall(ConnectionRequest connectionRequest) {

        try {
            Gson gson = new Gson();
            jsonObjectConnection = new JSONObject(gson.toJson(connectionRequest));

        } catch (Exception e) {
            Log.i("LoginResponseData", e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, CONSTANTS.BASE_URL + CONSTANTS.CONNECTIONS, jsonObjectConnection, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            stopAnim();
                            Gson gson = new Gson();
                            ConnectionResponseWrapper connectionResponseWrapper = gson.fromJson(response.toString(), ConnectionResponseWrapper.class);
                            initRecyclerViewConnection(connectionResponseWrapper.getData().getResult());

                        } else {
                            Toast.makeText(getContext(), "response found null ", Toast.LENGTH_SHORT).show();
                            //Dismiss Dialog
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stopAnim();
                        new AlertDialog.Builder(getActivity()).setTitle("Check Internet Connection !!").setCancelable(false).setPositiveButton("ok",null).show();
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void initRecyclerViewConnection(List<ConnectionResponseResult> resultList) {
        if (resultList.size() > 0) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ConnectionAdapter(getActivity(), resultList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "list found null", Toast.LENGTH_SHORT).show();
        }
    }

    void startAnim() {
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    void stopAnim() {
        dialog.dismiss();
    }


}
