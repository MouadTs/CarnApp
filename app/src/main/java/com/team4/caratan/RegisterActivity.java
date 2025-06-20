package com.team4.caratan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //private FirebaseAuth mAuth;

    private EditText edtNama, edtEmail, edtPw;
    private Button daftar, masuk;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        //mAuth = FirebaseAuth.getInstance();

        edtNama = findViewById(R.id.reg_edtNama);
        edtEmail = findViewById(R.id.reg_edtEmail);
        edtPw = findViewById(R.id.reg_edtPw);
        progressBar = findViewById(R.id.reg_progressBar);

        daftar = findViewById(R.id.reg_btnReg);
        daftar.setOnClickListener(view ->
                registerNewUser()
        );

        masuk = findViewById(R.id.reg_btnLogin);
        masuk.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }

    private void registerNewUser() {
        String nama = edtNama.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pw = edtPw.getText().toString().trim();

        if (nama.isEmpty()) {
            edtNama.setError(getString(R.string.error_username_required));
            edtNama.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError(getString(R.string.error_email_required));
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

        if (pw.length()<6) {
            edtPw.setError(getString(R.string.error_password_too_short));
            edtPw.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.URL_REGISTER,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"),
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), getString(R.string.error_internet_connection),
                            Toast.LENGTH_LONG).show();
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fullname",nama);
                params.put("email",email);
                params.put("password",pw);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
/*
    private void registerUser() {
        String nama = edtNama.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pw = edtPw.getText().toString().trim();

        if (nama.isEmpty()) {
            edtNama.setError("Username perlu diisi!");
            edtNama.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Email perlu diisi!");
            edtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email tidak sesuai!");
            edtEmail.requestFocus();
            return;
        }

        if (pw.isEmpty()) {
            edtPw.setError("Password perlu diisi!");
            edtPw.requestFocus();
            return;
        }

        if (pw.length()<6) {
            edtPw.setError("Password terlalu pendek, minimal 6 karakter!");
            edtPw.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Berhasil mendaftarkan anda!",
                                    Toast.LENGTH_LONG).show();

                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nama).build();
                            user.updateProfile(profileUpdates);

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }, 1000);

                        } else {
                            Toast.makeText(getApplicationContext(), "Terjadi kesalahan mendaftarkan anda!",
                                    Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

 */
}