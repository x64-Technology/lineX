package com.x64technology.linex.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.x64technology.linex.MainActivity;
import com.x64technology.linex.databinding.ActivityAuthBinding;
import com.x64technology.linex.services.AppPreference;
import com.x64technology.linex.services.UserPreference;

import org.json.JSONException;
import org.json.JSONObject;

public class Auth extends AppCompatActivity {
    ActivityAuthBinding activityAuthBinding;
    ProgressDialog progressDialog;
    UserPreference userPreference;
    boolean login = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAuthBinding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(activityAuthBinding.getRoot());

        userPreference = new UserPreference(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setTitle("authenticating");

        setCallbacks();
    }


    private void setCallbacks() {
        activityAuthBinding.btnAuthChange.setOnClickListener(view -> {
            login = !login;
            activityAuthBinding.heading.setText(login? "Welcome Back" : "Hello There");
            activityAuthBinding.subheading.setText(login? "nice to see you again" : "thanks for choosing us.");
            activityAuthBinding.btnAuthChange.setText(login? "signup" : "login");
        });

        activityAuthBinding.btnContinue.setOnClickListener(view -> {
            String uname = activityAuthBinding.inpUsername.getEditableText().toString();
            String password = activityAuthBinding.inpPassword.getEditableText().toString();

            makeCall(uname, password);

        });
    }

    private void makeCall(String uname, String password) {
        progressDialog.show();
        String url = login ? "http://192.168.43.30:3000/auth/signin" : "http://192.168.43.30:3000/auth/signup";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", uname);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    try {
                        userPreference.saveUserData(response.getString("username"),
                                response.getString("id"),
                                "some random name",
                                "some dp link");
                        userPreference.saveToken(response.getString("token"));

                        progressDialog.dismiss();
                        startActivity(new Intent(Auth.this, MainActivity.class));
                        finish();
                    } catch (JSONException e) {
                        Toast.makeText(Auth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.dismiss();
                    activityAuthBinding.inpUsername.setText("");
                    String err = new String(error.networkResponse.data);
                    if (error.networkResponse.statusCode == 404)
                        activityAuthBinding.unameLayout.setError(err);
                    else if (error.networkResponse.statusCode == 403)
                        activityAuthBinding.pwordLayout.setError(err);
                    else Toast.makeText(Auth.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

}