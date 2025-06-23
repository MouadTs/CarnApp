package com.team4.caratan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellCar_InfoFragment extends Fragment {

    private Button btnNext1;
    private EditText edtYear, edtMileage, edtPrice, edtDesc, edtModel, edtType, edtColor;
    private Spinner spinnerBrand, spinnerLocation;

    private ArrayList<String> makeList = new ArrayList<>();
    private String selectedMake, model, type, color, year, mileage, price, desc, selectedLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell_car__info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtYear = requireView().findViewById(R.id.edtYear);
        edtMileage = requireView().findViewById(R.id.edtMileage);
        edtPrice = requireView().findViewById(R.id.edtPrice);
        edtDesc = requireView().findViewById(R.id.edtDesc);
        edtModel = requireView().findViewById(R.id.edtModel);
        edtType = requireView().findViewById(R.id.edtType);
        edtColor = requireView().findViewById(R.id.edtColor);
        spinnerBrand = requireView().findViewById(R.id.spinnerBrand);
        spinnerLocation = requireView().findViewById(R.id.spinnerLocation);
        btnNext1 = requireView().findViewById(R.id.SELL_btnNext1);

        getMakeSpinnerData();
        spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMake = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        String[] locations = {"Tangier", "Casablanca", "Rabat", "Marrakech", "Fez", "Agadir"};
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLocation = "";
            }
        });

        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                year = edtYear.getText().toString().trim();
                mileage = edtMileage.getText().toString().trim();
                price = edtPrice.getText().toString().trim();
                desc = edtDesc.getText().toString().trim();
                model = edtModel.getText().toString().trim();
                type = edtType.getText().toString().trim();
                color = edtColor.getText().toString().trim();

                // Validate car brand selection
                if (selectedMake == null || selectedMake.isEmpty()) {
                    Toast.makeText(requireContext(), "Please select a car brand!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate car model
                if (model.isEmpty()) {
                    edtModel.setError("Car Model Must Be Filled!");
                    edtModel.requestFocus();
                    return;
                }

                // Validate car type
                if (type.isEmpty()) {
                    edtType.setError("Car Type Must Be Filled!");
                    edtType.requestFocus();
                    return;
                }

                // Validate car color
                if (color.isEmpty()) {
                    edtColor.setError("Car Color Must Be Filled!");
                    edtColor.requestFocus();
                    return;
                }

                if (year.isEmpty()) {
                    edtYear.setError("Car Year Must Be Filled!");
                    edtYear.requestFocus();
                    return;
                }
                try {
                    int yearValue = Integer.parseInt(year);
                    if (yearValue < 1900 || yearValue > 2024) {
                        edtYear.setError("Car Year Is Not Valid!");
                        edtYear.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    edtYear.setError("Car Year Must Be A Valid Number!");
                    edtYear.requestFocus();
                    return;
                }
                if (mileage.isEmpty()) {
                    edtMileage.setError("Car Mileage Must Be Filled!");
                    edtMileage.requestFocus();
                    return;
                }
                try {
                    int mileageValue = Integer.parseInt(mileage);
                    if (mileageValue < 0 || mileageValue > 999999) {
                        edtMileage.setError("Car Mileage Is Not Valid!");
                        edtMileage.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    edtMileage.setError("Car Mileage Must Be A Valid Number!");
                    edtMileage.requestFocus();
                    return;
                }
                if (price.isEmpty()) {
                    edtPrice.setError("Car Price Must Be Filled!");
                    edtPrice.requestFocus();
                    return;
                }
                try {
                    int priceValue = Integer.parseInt(price);
                    if (priceValue < 2000000) {
                        edtPrice.setError("Cannot sell car below 2,000,000 MAD!");
                        edtPrice.requestFocus();
                        return;
                    }
                    if (priceValue > 200000000) {
                        edtPrice.setError("Cannot sell car above 200,000,000 MAD!");
                        edtPrice.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    edtPrice.setError("Car Price Must Be A Valid Number!");
                    edtPrice.requestFocus();
                    return;
                }
                if (desc.isEmpty()) {
                    edtDesc.setError("Car Description Must Be Filled!");
                    edtDesc.requestFocus();
                    return;
                }

                // Validate location
                if (selectedLocation == null || selectedLocation.isEmpty()) {
                    Toast.makeText(requireContext(), "Please select a location!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ((SellCarActivity)getActivity()).carMake = selectedMake;
                ((SellCarActivity)getActivity()).carModel = model;
                ((SellCarActivity)getActivity()).carType = type;
                ((SellCarActivity)getActivity()).carColor = color;
                ((SellCarActivity)getActivity()).carYear = year;
                ((SellCarActivity)getActivity()).carMileage = mileage;
                ((SellCarActivity)getActivity()).carPrice = price;
                ((SellCarActivity)getActivity()).carDesc = desc;
                ((SellCarActivity)getActivity()).carLocation = selectedLocation;

                ((SellCarActivity)getActivity()).openSellCar_Photo();
            }
        });
    }

    private void getMakeSpinnerData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.URL_GET_MAKE,
                response -> {
                    try {
                        makeList.clear();
                        makeList.add("Select Brand"); // Default option
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String makeName = jsonArray.getString(i);
                            makeList.add(makeName);
                        }
                        ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_spinner_item, makeList);
                        makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerBrand.setAdapter(makeAdapter);
                        // Set default selection
                        spinnerBrand.setSelection(0);
                        selectedMake = "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error parsing car makes data: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        setDefaultBrandSpinner();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Unexpected error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        setDefaultBrandSpinner();
                    }
                },
                error -> {
                    String errorMessage = "Network error";
                    if (error != null) {
                        errorMessage = "Error: " + error.getMessage();
                        if (error.networkResponse != null) {
                            errorMessage += " (Status: " + error.networkResponse.statusCode + ")";
                        }
                    }
                    Toast.makeText(requireContext(), errorMessage,
                            Toast.LENGTH_LONG).show();
                    setDefaultBrandSpinner();
                }
        );
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void setDefaultBrandSpinner() {
        makeList.clear();
        makeList.add("Select Brand");
        ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, makeList);
        makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrand.setAdapter(makeAdapter);
        spinnerBrand.setSelection(0);
        selectedMake = "";
    }
}