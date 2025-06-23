package com.team4.caratan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPw;
    private Button masuk, daftar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        edtEmail = findViewById(R.id.login_edtEmail);
        edtPw = findViewById(R.id.login_edtPw);
        progressBar = findViewById(R.id.login_progressBar);

        masuk = findViewById(R.id.login_btnLogin);
        masuk.setOnClickListener(view -> userLogin());

        daftar = findViewById(R.id.login_btnDaftar);
        daftar.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });
    }

    private void userLogin() {

        final String email = edtEmail.getText().toString().trim();
        final String pw = edtPw.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.setError(getString(R.string.error_username_required));
            edtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError(getString(R.string.error_email_invalid));
            edtEmail.requestFocus();
            return;
        }

        if (pw.isEmpty()) {
            edtPw.setError(getString(R.string.error_password_required));
            edtPw.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.URL_LOGIN,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_login_success),
                                    Toast.LENGTH_LONG).show();

                            final mDBhandler dbHandler = new mDBhandler(this);

                            try {
                                dbHandler.open();
                            } catch (SQLException e) {
                                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                            }

                            // Extract user data from the new response format
                            JSONObject userObj = obj.getJSONObject("user");
                            String token = obj.getString("token");
                            
                            Log.d("LoginActivity", "JWT Token received: " + token.substring(0, Math.min(20, token.length())) + "...");
                            Log.d("LoginActivity", "User ID: " + userObj.getString("id"));
                            Log.d("LoginActivity", "User Email: " + userObj.getString("email"));
                            
                            dbHandler.createUsers(
                                userObj.getString("id"), 
                                userObj.getString("fullName"),
                                userObj.getString("email"), 
                                userObj.optString("phone", ""), 
                                userObj.optString("profilePic", ""), 
                                userObj.optString("isAdmin", "false")
                            );

                            SharedPrefManager.getInstance(getApplicationContext())
                                    .loginUser(
                                        userObj.getString("email"),
                                        token,
                                        userObj.getString("id"),
                                        userObj.getString("fullName")
                                    );

                            Log.d("LoginActivity", "JWT Token stored successfully");

                            dbHandler.close();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("message"),
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing response: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), getString(R.string.error_internet_connection),
                            Toast.LENGTH_LONG).show();
                }
        ){
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("email", edtEmail.getText().toString().trim());
                    jsonBody.put("password", edtPw.getText().toString().trim());
                    return jsonBody.toString().getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}