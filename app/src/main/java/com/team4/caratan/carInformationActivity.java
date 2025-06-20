package com.team4.caratan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class carInformationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton btnBack;
    private ImageLoader imageLoader;
    private GoogleMap map;
    private LatLng testLatLng;

    ViewPager viewPager;
    List<SliderUtils> sliderImg;
    ViewPagerAdapter viewPagerAdapter;

    private String usedCar_id, phone;

    private Button btnAdmin, btnWA;

    private TextView txtCarMake, txtCarModel, txtCarYear, txtCarMileage, txtCarFuel, txtCarTrans,
                        txtCarCC, txtCarCyl, txtCarColor, txtCarSeat, txtCarDesc, txtCarPrice,
                        txtViewCount, txtPostDate, txtCarPenjual;

    private ImageView imgCarLogo, imgCarSeller;

    private ProgressBar progressBar;
    private FrameLayout white_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_information);

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        btnAdmin = findViewById(R.id.info_btnAdmin);
        int a = 1;
        if (a == 1) {
            btnAdmin.setVisibility(View.VISIBLE);
        }

        Intent i = getIntent();
        usedCar_id = i.getStringExtra("usedcar_id");

        sliderImg = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.imagePager);

        txtCarMake = findViewById(R.id.info_txtMakeName);
        txtCarModel = findViewById(R.id.info_ModelTypeYear);
        txtCarYear = findViewById(R.id.info_txtYear);
        txtCarMileage = findViewById(R.id.info_txtMileage);
        txtCarFuel = findViewById(R.id.info_txtFuel);
        txtCarTrans = findViewById(R.id.info_txtTrans);
        txtCarCC = findViewById(R.id.info_txtCC);
        txtCarCyl = findViewById(R.id.info_txtCylinder);
        txtCarColor = findViewById(R.id.info_txtColor);
        txtCarSeat = findViewById(R.id.info_txtSeat);
        txtCarDesc = findViewById(R.id.info_txtDesc);
        txtCarPrice = findViewById(R.id.info_txtPrice);
        txtCarPenjual = findViewById(R.id.info_txtNamaPenjual);


        imgCarLogo = findViewById(R.id.info_imgLogo);
        imgCarSeller = findViewById(R.id.info_imgProfil);

        progressBar = findViewById(R.id.info_progressBar);
        white_bg = findViewById(R.id.info_loadingbg);

        getCarInformation();
        fetchCarImages();
        updateViews();

        btnBack = (ImageButton) findViewById(R.id.btnBack3);
        btnBack.setOnClickListener(view -> finish());

        btnWA = findViewById(R.id.info_btnWhatsApp);
        btnWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WA = new Intent(Intent.ACTION_VIEW, Uri.parse("http://api.whatsapp.com/send?phone=62" + phone + "&text=Halo%20saya%20tertarik%20dengan%20mobil%20yang%20anda%20pasang"));
                startActivity(WA);
            }
        });

        try {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_info);
            supportMapFragment.getMapAsync(this);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        testLatLng = new LatLng(-6.2546748, 106.6173419);

        map.addCircle(new CircleOptions().center(testLatLng).radius(400).strokeWidth(0).fillColor(Color.parseColor("#40FF9800")));
        map.moveCamera(CameraUpdateFactory.newLatLng(testLatLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(testLatLng, 14));
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(false);
    }

    public void fetchCarImages() {
        progressBar.setVisibility(View.VISIBLE);
        white_bg.setVisibility(View.VISIBLE);
        final String id_usedcar = usedCar_id;
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constant.URL_GETCARPHOTOS,
                response -> {

                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray jsonArray = obj.getJSONArray("usedcarphoto");

                        JSONObject main = jsonArray.getJSONObject(0);
                        SliderUtils slide1 = new SliderUtils();

                        slide1.setSliderImageURL(Constant.IMAGE_URL + main.getString("car_mainPhoto"));

                        sliderImg.add(slide1);

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            SliderUtils sliderUtils = new SliderUtils();

                            sliderUtils.setSliderImageURL(Constant.IMAGE_URL + object.getString("photos"));

                            sliderImg.add(sliderUtils);
                        }
                        viewPagerAdapter = new ViewPagerAdapter(sliderImg, carInformationActivity.this);
                        viewPager.setAdapter(viewPagerAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                    white_bg.setVisibility(View.GONE);
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(carInformationActivity.this, "Please check your internet connection!",
                            Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usedCar_id", id_usedcar);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getCarInformation() {
        progressBar.setVisibility(View.VISIBLE);
        white_bg.setVisibility(View.VISIBLE);
        final String id_usedcar = usedCar_id;

        StringRequest carRequest = new StringRequest(
                Request.Method.POST,
                Constant.URL_GETCARINFO,
                response -> {

                    try {
                        JSONObject main = new JSONObject(response);

                        String usedCar_id = main.getString("usedCar_id");
                        String category = main.getString("category_name");
                        String make = main.getString("make_name");
                        String model = main.getString("model_name");
                        String type = main.getString("model_type");
                        String cc = main.getString("model_cc");
                        String transmission = main.getString("model_transmission");
                        String cylinder = main.getString("model_cylinder");
                        String fuel = main.getString("model_fuel");
                        String seat = main.getString("model_seat");
                        String status = main.getString("car_status");
                        String color = main.getString("color");
                        String year = main.getString("year");
                        String mileage = main.getString("mileage");
                        String location = main.getString("location");
                        String price = main.getString("price");
                        String post_date = main.getString("post_date");
                        String views = main.getString("views");
                        String description = main.getString("description");
                        String user_id = main.getString("user_id");
                        String fullname = main.getString("fullname");
                        phone = main.getString("phone");
                        String make_logo = main.getString("make_logo");
                        String profile_pic = main.getString("profile_pic");

                        imageLoader = CustomVolleyRequest.getInstance(this).getImageLoader();

                        String carLogo = Constant.IMAGE_URL + make_logo;
                        String carSeller = Constant.IMAGE_URL + profile_pic;

                        imageLoader.get(carLogo,
                                ImageLoader.getImageListener(imgCarLogo, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

                        imageLoader.get(carSeller,
                                ImageLoader.getImageListener(imgCarSeller, R.drawable.defaultprofileicon, R.drawable.defaultprofileicon));


                        DecimalFormat decim = new DecimalFormat("###,###");
                        double km = Double.parseDouble(mileage);
                        double rp = Double.parseDouble(price);
                        double vw = Double.parseDouble(views);
                        String decim_km = decim.format(km);
                        String decim_rp = decim.format(rp);
                        String decim_vw = decim.format(vw);

                        txtCarMake.setText(make);
                        txtCarModel .setText(model + " " + type + " " + year);
                        txtCarYear.setText(year);
                        txtCarMileage.setText(decim_km + " KM");
                        txtCarFuel.setText(fuel);
                        txtCarTrans.setText(transmission);
                        txtCarCC.setText(cc + " CC");
                        txtCarCyl.setText(cylinder);
                        txtCarColor.setText(color);
                        txtCarSeat.setText(seat);
                        txtCarDesc.setText(description);
                        txtCarPrice.setText("Rp. " + decim_rp);
                        txtViewCount.setText("Dilihat " + decim_vw + " kali");
                        txtPostDate.setText("Diunggah pada " + post_date);
                        txtCarPenjual.setText(fullname);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                    white_bg.setVisibility(View.GONE);
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(carInformationActivity.this, "Please check your internet connection!",
                            Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usedCar_id", id_usedcar);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(carRequest);
    }

    private void updateViews() {

        final String id_usedcar = usedCar_id;

        StringRequest carRequest = new StringRequest(
                Request.Method.POST,
                Constant.URL_UPDATEVIEWS,
                response -> {
                },
                error -> Toast.makeText(carInformationActivity.this, "Please check your internet connection!",
                        Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usedCar_id", id_usedcar);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(carRequest);
    }
}