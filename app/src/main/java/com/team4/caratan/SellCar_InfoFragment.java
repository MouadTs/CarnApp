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
    private Spinner spinnerBrand;

    private ArrayList<String> makeList = new ArrayList<>();
    private String selectedMake, model, type, color, year, mileage, price, desc;

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

                if (year.isEmpty()) {
                    edtYear.setError("Car Year Must Be Filled!");
                    edtYear.requestFocus();
                    return;
                }
                if (Integer.parseInt(year) < 1900 || Integer.parseInt(year) > 2024) {
                    edtYear.setError("Car Year Is Not Valid!");
                    edtYear.requestFocus();
                    return;
                }
                if (mileage.isEmpty()) {
                    edtMileage.setError("Car Mileage Must Be Filled!");
                    edtMileage.requestFocus();
                    return;
                }
                if (Integer.parseInt(mileage) < 0 || Integer.parseInt(mileage) > 999999) {
                    edtMileage.setError("Car Mileage Is Not Valid!");
                    edtMileage.requestFocus();
                    return;
                }
                if (price.isEmpty()) {
                    edtPrice.setError("Car Price Must Be Filled!");
                    edtPrice.requestFocus();
                    return;
                }
                if (Integer.parseInt(price) < 2000000) {
                    edtPrice.setError("Cannot sell car below Rp 2,000,000!");
                    edtPrice.requestFocus();
                    return;
                }
                if (desc.isEmpty()) {
                    edtDesc.setError("Car Description Must Be Filled!");
                    edtDesc.requestFocus();
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

                ((SellCarActivity)getActivity()).openSellCar_Photo();
            }
        });
    }

    private void getMakeSpinnerData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.URL_GET_MAKE,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("makes");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String makeName = object.getString("make_name");
                                makeList.add(makeName);
                            }

                            ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_item, makeList);
                            makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinnerBrand.setAdapter(makeAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(requireContext(), "Please check your internet connection!",
                            Toast.LENGTH_LONG).show();
                    //progressBar.setVisibility(View.GONE);
                }
        );

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}