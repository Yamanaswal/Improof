package com.app.improof_project.activities.activities.login_activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.improof_project.R;
import com.app.improof_project.activities.activities.homepage_activity.Home;
import com.app.improof_project.models.login_model.LoginValidations;
import com.app.improof_project.models.login_model.login_request.LoginRequest;
import com.app.improof_project.models.login_model.login_response.LoginResponseWrapper;
import com.app.improof_project.utils.AppController;
import com.app.improof_project.utils.CONSTANTS;
import com.app.improof_project.utils.PreferenceEntities;
import com.app.improof_project.utils.Preferences;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    EditText email, password;
    Button login_button;
    LoginButton loginFacebook;

    LoginValidations loginValidation;
    Gson gson;
    String emailAddress;
    String passwordValue;
    LoginRequest loginRequest;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Close App ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        setIds();
        loginValidation = new LoginValidations();
        loginRequest = new LoginRequest();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the data
                if (isConnected(Login.this)) {
                    emailAddress = email.getText().toString();
                    passwordValue = password.getText().toString();
                    //Form Validation
                    if (isValidation(emailAddress, passwordValue)) {
                        loginRequest.setApp_key(CONSTANTS.APPKEY);
                        loginRequest.setEmail(emailAddress);
                        loginRequest.setPassword(passwordValue);

                        //Hit the Api
                        apiCall(loginRequest);
                    } else {
                        Toast.makeText(Login.this, "Please Fills Some Correct Details !!", Toast.LENGTH_SHORT).show();
                    }
                    //no internet
                } else {
                    buildDialog(Login.this).show();
                }
            }
        });

        /*
        Facebook Integration
         */
        CallbackManager callbackManager = CallbackManager.Factory.create();
        loginFacebook = findViewById(R.id.login_facebook);
        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    //Method to Set Ids
    public void setIds() {
        email = findViewById(R.id.email_login_id);
        password = findViewById(R.id.password_login_id);
        login_button = findViewById(R.id.login_button_id);
    }

    //Method to Validations
    public boolean isValidation(String emailAddress, String passwordValue) {
        //Form Validations
        boolean email_check = true, pass_check = true;
        int pass_length = passwordValue.length();
        if (emailAddress.length() == 0)
            email.setError("Please enter email");
        else if (LoginValidations.isEmail(emailAddress)) {
            email_check = true;
        }
        if (pass_length == 0) {
            password.setError("Please enter password");
        } else if (LoginValidations.isPassword(password.getText().toString())) {
            pass_check = true;
        } else {
            pass_check = false;
            password.setError("Password must contain at least 7 character");
        }
        if (pass_check && email_check) {
            return true;
        } else
            return false;
    }

    //API call Method
    public void apiCall(LoginRequest loginRequest) {
        //start dialog
        startAnim();
        gson = new Gson();
        String loginRequestJson = gson.toJson(loginRequest);

        JSONObject jsonLoginRQST = null;
        try {
            jsonLoginRQST = new JSONObject(loginRequestJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, CONSTANTS.BASE_URL + CONSTANTS.LOGIN, jsonLoginRQST, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            stopAnim();
                            LoginResponseWrapper loginResponseWrapper = gson.fromJson(response.toString(), LoginResponseWrapper.class);
                            try {
                                if (response.getJSONObject("data").getString("status").equals("1")) {

                                    Preferences.setPreference(getApplicationContext(), PreferenceEntities.USER_DETAILS, loginResponseWrapper.getBaseData().getResult());

                                    Intent i = new Intent(Login.this, Home.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Please Check Your Details !", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG + "error", error.getMessage());
                      stopAnim();
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    //Check Internet Connection
    //Build dialog
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Login.this, "Please Turn On Internet !!", Toast.LENGTH_SHORT).show();
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

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    void startAnim() {
        Dialog dialog = new Dialog(Login.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.show();
    }

    void stopAnim(){
        Dialog dialog = new Dialog(Login.this);
        dialog.dismiss();
    }
    //End of Activity
}
