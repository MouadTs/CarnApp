package com.team4.caratan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarMakeListActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ProgressBar progressBar;

    ListView listView;
    CarMakeListAdapter smallAdapter;
    public static ArrayList<carMakeGetSet> carMakeArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_make_list);

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        progressBar = findViewById(R.id.carmakelist_progressBar);

        btnBack = findViewById(R.id.btnBack4);
        btnBack.setOnClickListener(view -> finish());

        listView = (ListView) findViewById(R.id.carMakeListView);
        smallAdapter = new CarMakeListAdapter(this, carMakeArrayList);
        listView.setAdapter(smallAdapter);

        getCarMakeListData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parenView, View childView, int position, long id) {
                TextView CML_txtName = childView.findViewById(R.id.carMakeList_name);
                String MakeName = CML_txtName.getText().toString().trim();

                Intent i = new Intent(CarMakeListActivity.this, CarByMakeListActivity.class);
                i.putExtra("make_name", MakeName);
                startActivity(i);
            }
        });
    }

    private void getCarMakeListData() {

        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constant.URL_GETMAKE_LIST,
                response -> {

                    carMakeArrayList.clear();
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray jsonArray = obj.getJSONArray("carmake");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String make_logo = object.getString("make_logo");
                            String make_name = object.getString("make_name");
                            String counts = object.getString("counts");

                            carMakeGetSet car = new carMakeGetSet(make_logo, make_name, counts);

                            carMakeArrayList.add(car);
                            smallAdapter.notifyDataSetChanged();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Please check your internet connection!",
                            Toast.LENGTH_LONG).show();
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}