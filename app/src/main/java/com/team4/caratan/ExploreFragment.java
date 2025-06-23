package com.team4.caratan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private Button btnToyota, btnHonda, btnBMW, btnMoreMake, btnSearch, btnApplyFilters;
    private Spinner brandFilterSpinner, modelFilterSpinner, locationFilterSpinner, priceFilterSpinner;
    
    private List<String> makeList = new ArrayList<>();
    private List<String> modelList = new ArrayList<>();
    private List<String> locationList = new ArrayList<>();
    private String selectedMake = "";
    private String selectedModel = "";
    private String selectedLocation = "";
    private String selectedPriceRange = "";

    public ExploreFragment() {

    }

/*
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize buttons
        btnSearch = view.findViewById(R.id.button);
        btnToyota = view.findViewById(R.id.explore_btnToyota);
        btnHonda = view.findViewById(R.id.explore_btnHonda);
        btnBMW = view.findViewById(R.id.explore_btnBMW);
        btnMoreMake = view.findViewById(R.id.btnMoreMake);
        btnApplyFilters = view.findViewById(R.id.btnApplyFilters);

        // Initialize spinners
        brandFilterSpinner = view.findViewById(R.id.brand_filter_spinner);
        modelFilterSpinner = view.findViewById(R.id.model_filter_spinner);
        locationFilterSpinner = view.findViewById(R.id.location_filter_spinner);
        priceFilterSpinner = view.findViewById(R.id.price_filter_spinner);

        // Set up click listeners
        btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), SearchActivity.class));
        });

        btnToyota.setOnClickListener(view1 -> {
            navigateToCarMake("Toyota");
        });
        
        btnHonda.setOnClickListener(view1 -> {
            navigateToCarMake("Honda");
        });
        
        btnBMW.setOnClickListener(view1 -> {
            navigateToCarMake("BMW");
        });

        btnMoreMake.setOnClickListener(view1 -> {
            startActivity(new Intent(requireActivity(), CarMakeListActivity.class));
        });

        btnApplyFilters.setOnClickListener(v -> {
            applyFilters();
        });

        // Set up spinners
        setupSpinners();
        
        // Load data
        loadMakeList();
        loadLocationList();
    }

    private void setupSpinners() {
        // Price range options
        String[] priceRanges = {"Any Price", "2M - 10M MAD", "10M - 25M MAD", "25M - 50M MAD", "50M+ MAD"};
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, priceRanges);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceFilterSpinner.setAdapter(priceAdapter);

        // Brand filter listener
        brandFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMake = parent.getItemAtPosition(position).toString();
                if (!selectedMake.equals("Any Brand")) {
                    loadModelList(selectedMake);
                } else {
                    modelList.clear();
                    modelList.add("Any Model");
                    ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(getContext(), 
                            android.R.layout.simple_spinner_item, modelList);
                    modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modelFilterSpinner.setAdapter(modelAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMake = "";
            }
        });

        // Model filter listener
        modelFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedModel = "";
            }
        });

        // Location filter listener
        locationFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLocation = "";
            }
        });

        // Price filter listener
        priceFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriceRange = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPriceRange = "";
            }
        });
    }

    private void loadMakeList() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Constant.URL_GET_MAKE,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        makeList.clear();
                        makeList.add("Any Brand"); // Default option
                        
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String makeName = jsonArray.getString(i);
                            makeList.add(makeName);
                        }

                        ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(getContext(), 
                                android.R.layout.simple_spinner_item, makeList);
                        makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        brandFilterSpinner.setAdapter(makeAdapter);
                        
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error loading car brands", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error loading car brands: " + error.getMessage(), 
                                 Toast.LENGTH_SHORT).show();
                }
        );
        RequestHandler.getInstance(getContext()).addToRequestQueue(request);
    }

    private void loadModelList(String make) {
        // For now, we'll use a simple approach - in a real app, you'd have an endpoint for models by make
        modelList.clear();
        modelList.add("Any Model");
        
        // Add some common models based on make
        if (make.equals("Toyota")) {
            modelList.addAll(java.util.Arrays.asList("Camry", "Corolla", "RAV4", "Highlander", "Tacoma", "Tundra"));
        } else if (make.equals("Honda")) {
            modelList.addAll(java.util.Arrays.asList("Civic", "Accord", "CR-V", "Pilot", "Ridgeline"));
        } else if (make.equals("BMW")) {
            modelList.addAll(java.util.Arrays.asList("3 Series", "5 Series", "X3", "X5", "M3", "M5"));
        } else {
            modelList.add("Any Model");
        }

        ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_spinner_item, modelList);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelFilterSpinner.setAdapter(modelAdapter);
    }

    private void loadLocationList() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Constant.URL_GET_LOCATIONS,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        locationList.clear();
                        locationList.add("Any Location"); // Default option
                        
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String locationName = jsonArray.getString(i);
                            locationList.add(locationName);
                        }

                        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(getContext(), 
                                android.R.layout.simple_spinner_item, locationList);
                        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        locationFilterSpinner.setAdapter(locationAdapter);
                        
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Fallback to hardcoded locations if API fails
                        loadFallbackLocations();
                    }
                },
                error -> {
                    // Fallback to hardcoded locations if API fails
                    loadFallbackLocations();
                }
        );
        RequestHandler.getInstance(getContext()).addToRequestQueue(request);
    }

    private void loadFallbackLocations() {
        locationList.clear();
        locationList.add("Any Location");
        locationList.addAll(java.util.Arrays.asList("Tangier", "Casablanca", "Rabat", "Marrakech", "Fez", "Agadir"));
        
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_spinner_item, locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationFilterSpinner.setAdapter(locationAdapter);
    }

    private void applyFilters() {
        // Build search parameters
        StringBuilder searchParams = new StringBuilder();
        
        if (!selectedMake.equals("Any Brand")) {
            searchParams.append("make=").append(selectedMake);
        }
        
        if (!selectedModel.equals("Any Model")) {
            if (searchParams.length() > 0) searchParams.append("&");
            searchParams.append("model=").append(selectedModel);
        }
        
        if (!selectedLocation.equals("Any Location")) {
            if (searchParams.length() > 0) searchParams.append("&");
            searchParams.append("location=").append(selectedLocation);
        }
        
        if (!selectedPriceRange.equals("Any Price")) {
            if (searchParams.length() > 0) searchParams.append("&");
            // Add price range parameters
            switch (selectedPriceRange) {
                case "2M - 10M MAD":
                    searchParams.append("minPrice=2000000&maxPrice=10000000");
                    break;
                case "10M - 25M MAD":
                    searchParams.append("minPrice=10000000&maxPrice=25000000");
                    break;
                case "25M - 50M MAD":
                    searchParams.append("minPrice=25000000&maxPrice=50000000");
                    break;
                case "50M+ MAD":
                    searchParams.append("minPrice=50000000&maxPrice=200000000");
                    break;
            }
        }

        // Navigate to search results
        Intent intent = new Intent(requireActivity(), SearchActivity.class);
        if (searchParams.length() > 0) {
            intent.putExtra("search_params", searchParams.toString());
        }
        startActivity(intent);
    }

    private void navigateToCarMake(String makeName) {
        try {
            Intent i = new Intent(requireActivity(), CarByMakeListActivity.class);
            i.putExtra("make_name", makeName);
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error navigating to " + makeName + " cars: " + e.getMessage(), 
                         Toast.LENGTH_SHORT).show();
        }
    }
}