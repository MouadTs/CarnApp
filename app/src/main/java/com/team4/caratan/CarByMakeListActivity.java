package com.team4.caratan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CarByMakeListActivity extends AppCompatActivity {

    ListView listView;
    HomeListAdapter smallAdapter;
    public static ArrayList<carSmallCard> carSmallCardArrayList = new ArrayList<>();

    private ImageButton btnBack;
    private Button btnSite;
    private ImageView imgMainMakeLogo;
    private TextView txtMainMakeName, txtMakeDesc;
    private Bitmap bmpMakeLogo;
    private ImageLoader imageLoader;

    private String make_name, make_site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_by_make_list);

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        Intent i = getIntent();
        make_name = i.getStringExtra("make_name");

        btnBack = findViewById(R.id.btnBack5);
        btnBack.setOnClickListener(view -> {
            listView.setAdapter(null);
            finish();
        });

        imgMainMakeLogo = findViewById(R.id.carbymake_makeLogo);

        txtMainMakeName = findViewById(R.id.carbymake_makeName);
        txtMainMakeName.setText(make_name);

        txtMakeDesc = findViewById(R.id.carbymake_makeDesc);
        txtMakeDesc.setText("Here is a collection of used cars from " + make_name);

        getMakeLogo();

        btnSite = findViewById(R.id.carbymake_btnSite);
        btnSite.setOnClickListener(view -> {
            Intent j = new Intent(CarByMakeListActivity.this, CarMakeWebViewActivity.class);
            j.putExtra("make_site", make_site);
            startActivity(j);
        });

        listView = (ListView) findViewById(R.id.carByMakeListView);
        smallAdapter = new HomeListAdapter(this, carSmallCardArrayList);
        listView.setAdapter(smallAdapter);

        getCarSmallListData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parenView, View childView, int position, long id) {
                TextView txtUsedCarID = childView.findViewById(R.id.txtUsedCarID);
                String UsedCarID = txtUsedCarID.getText().toString().trim();

                Intent i = new Intent(CarByMakeListActivity.this, carInformationActivity.class);
                i.putExtra("usedcar_id", UsedCarID);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        listView.setAdapter(null);
        finish();
    }

    private void getMakeLogo() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Constant.URL_CAR_MAKES,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        make_site = obj.getString("make_site");

                        imageLoader = CustomVolleyRequest.getInstance(this).getImageLoader();
                        imageLoader.get(Constant.IMAGE_URL + obj.getString("make_logo"),
                                ImageLoader.getImageListener(imgMainMakeLogo, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Please check your internet connection!",
                            Toast.LENGTH_LONG).show();
                }
        );
        RequestHandler.getInstance(this).addToRequestQueue(request);
    }

    private void getCarSmallListData() {

        //progressBar.setVisibility(View.VISIBLE);

        JwtAuthenticatedRequest request = new JwtAuthenticatedRequest(
                Request.Method.GET,
                Constant.URL_CARS_BY_MAKE + make_name,
                response -> {

                    carSmallCardArrayList.clear();
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray jsonArray = obj.getJSONArray("usedcar");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String usedcar_id = object.getString("usedCar_id");
                            String make_name = object.getString("make_name");
                            String model_name = object.getString("model_name");
                            String model_type = object.getString("model_type");
                            String year = object.getString("year");
                            String mileage = object.getString("mileage");
                            String model_transmission = object.getString("model_transmission");
                            String location = object.getString("location");
                            String price = object.getString("price");
                            String car_mainPhoto = object.getString("car_mainPhoto");
                            String make_logo = object.getString("make_logo");

                            carSmallCard car = new carSmallCard(usedcar_id, make_name, model_name, model_type, year,
                                    mileage, model_transmission, location, price, car_mainPhoto, make_logo);

                            carSmallCardArrayList.add(car);
                            smallAdapter.notifyDataSetChanged();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //progressBar.setVisibility(View.GONE);
                },
                error -> {
                    Toast.makeText(this, "Please check your internet connection!",
                            Toast.LENGTH_LONG).show();
                    //progressBar.setVisibility(View.GONE);
                },
                this
        );
        RequestHandler.getInstance(this).addToRequestQueue(request);
    }
}