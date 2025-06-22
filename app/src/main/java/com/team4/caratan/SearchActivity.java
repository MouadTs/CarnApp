package com.team4.caratan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private ListView listView;
    private ProgressBar progressBar;
    private HomeListAdapter adapter;
    private ArrayList<carSmallCard> carList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view);
        listView = findViewById(R.id.search_list_view);
        progressBar = findViewById(R.id.search_progress_bar);

        adapter = new HomeListAdapter(this, carList);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // You can implement live search here if desired
                return false;
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            carSmallCard selectedCar = carList.get(position);
            Intent intent = new Intent(SearchActivity.this, carInformationActivity.class);
            intent.putExtra("usedcar_id", selectedCar.getCar_id());
            startActivity(intent);
        });
    }

    private void performSearch(String query) {
        progressBar.setVisibility(View.VISIBLE);
        carList.clear();
        adapter.notifyDataSetChanged();

        String url = Constant.API_URL + "cars/search?q=" + query;

        // Endpoint is now public, no auth needed
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = new JSONArray(response); // Response is a direct array
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String usedcar_id = object.getString("id");
                            String make_name = object.getString("make");
                            String model_name = object.getString("model");
                            String model_type = object.getString("type");
                            String year = object.getString("year");
                            String mileage = object.getString("mileage");
                            String model_transmission = object.getString("transmission");
                            String location = object.getString("location");
                            String price = object.getString("price");
                            
                            String mainPhoto = "images/default_car.png";
                            if (object.has("photos") && !object.isNull("photos") && !object.getString("photos").isEmpty()) {
                                mainPhoto = object.getString("photos").split(",")[0];
                            }
                            String make_logo = "images/" + make_name.toLowerCase() + "_logo.png";

                            carSmallCard car = new carSmallCard(usedcar_id, make_name, model_name, model_type, year,
                                    mileage, model_transmission, location, price, mainPhoto, make_logo);
                            carList.add(car);
                        }
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SearchActivity.this, "Error parsing search results", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SearchActivity.this, "Search failed: " + (error.getMessage() != null ? error.getMessage() : "No response"), Toast.LENGTH_SHORT).show();
                }
        );

        RequestHandler.getInstance(this).addToRequestQueue(request);
    }
} 