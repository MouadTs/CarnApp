package com.team4.caratan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private mDBhandler dbHandler;

    private ImageButton btnBack;
    private Button btnSave, btnEditPic, btnRotate;
    private EditText edtFNama, edtTelp;
    private TextView txtEmail, txtUserID;
    private ProgressBar progressBar;
    private ImageView profileImage;

    private String UserID;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        edtFNama = (EditText) findViewById(R.id.edtNama_profil);
        edtTelp = (EditText) findViewById(R.id.edtTelp_profil);
        txtEmail = (TextView) findViewById(R.id.txtEmail_edtProfil);
        txtUserID = (TextView) findViewById(R.id.txtIdUser_edtProfil);
        progressBar = (ProgressBar) findViewById(R.id.edtprofil_progressBar);
        profileImage = (ImageView) findViewById(R.id.imgProfil);

        dbHandler = new mDBhandler(this);

        try {
            dbHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Users users = dbHandler.getUsers();

        UserID = users.getUser_id();
        String userID_ = "USER ID: " + UserID;
        txtUserID.setText(userID_);
        txtEmail.setText(users.getUser_email());
        edtFNama.setText(users.getUser_fullname());
        edtTelp.setText(users.getUser_phone());

        String imgurl = Constant.IMAGE_URL + users.getUser_profilepic();
        new fetchImage(imgurl).start();

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {

                                if (result.getResultCode() == Activity.RESULT_OK) {

                                    Intent data = result.getData();
                                    Uri uri = data.getData();
                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                                        runOnUiThread(() -> profileImage.setImageBitmap(bitmap));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

        btnEditPic = findViewById(R.id.btnEdtFotoProfil);
        btnEditPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(i);
            }
        });

        btnRotate = findViewById(R.id.btnRotateFotoProfil);
        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profileImage.setImageBitmap(bitmap);
                        }
                    });
                } else {
                    Toast.makeText(EditProfileActivity.this, "No image to rotate!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave = findViewById(R.id.btnSaveProfile);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveProfileUpdates();
            }
        });
    }

    private void SaveProfileUpdates() {
        final String idUser = UserID;
        final String fnameUser = edtFNama.getText().toString().trim();
        final String phoneUser = edtTelp.getText().toString().trim();
        final String emailUser = txtEmail.getText().toString().trim();
        final String base64Image;

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        } else {
            Toast.makeText(EditProfileActivity.this, "Image is still empty!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (fnameUser.isEmpty()) {
            edtFNama.setError("Name must be filled!");
            edtFNama.requestFocus();
            return;
        }

        if (fnameUser.length() > 30) {
            edtFNama.setError("Name is too long!");
            edtFNama.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.URL_UPDATEUSER,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.getBoolean("error")) {

                            Users updateUsers = new Users();

                            updateUsers.setUser_id(obj.getString("user_id"));
                            updateUsers.setUser_fullname(obj.getString("fullname"));
                            updateUsers.setUser_email(obj.getString("email"));
                            updateUsers.setUser_phone(obj.getString("phone"));
                            updateUsers.setUser_profilepic(obj.getString("profile_pic"));
                            updateUsers.setUser_admin(obj.getString("admin"));

                            dbHandler.updateUsers(updateUsers);
                            /*
                            SharedPrefManager.getInstance(getApplicationContext())
                                    .loginUser(
                                            obj.getString("user_id"),
                                            obj.getString("email"),
                                            obj.getString("fullname"),
                                            obj.getString("phone"),
                                            obj.getString("profile_pic")
                                    );
                             */

                            Toast.makeText(getApplicationContext(), "Successfully updated account details!",
                                    Toast.LENGTH_LONG).show();

                            startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("message"),
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Please check your internet connection!",
                            Toast.LENGTH_SHORT).show();
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", idUser);
                params.put("fullname", fnameUser);
                params.put("phone", phoneUser);
                params.put("email", emailUser);
                params.put("profile_pic", base64Image);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    class fetchImage extends Thread {

        String URL;

        fetchImage(String URL) {
            this.URL = URL;
        }

        @Override
        public void run() {

            InputStream inputStream;
            try {
                inputStream = new java.net.URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (bitmap != null) {
                        profileImage.setImageBitmap(bitmap);
                    }
                }
            });
        }
    }
}