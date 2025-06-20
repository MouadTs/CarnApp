package com.team4.caratan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

public class HomeFragment extends Fragment {

    ListView listView;
    HomeListAdapter smallAdapter;
    public static ArrayList<carSmallCard> carSmallCardArrayList = new ArrayList<>();

    private TextView txtGreet;
    private String nama;
    private ProgressBar progressBar;
    private Button btnJual;
/*
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(Color.WHITE);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        Bundle data = getArguments();
        if (data != null) {
            nama = data.getString("nama");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtGreet = (TextView) requireView().findViewById(R.id.txtGreet);
        progressBar = (ProgressBar) requireView().findViewById(R.id.home_progressBar);
        txtGreet.setText("Halo, " + nama + "!");

        btnJual = (Button) requireView().findViewById(R.id.home_btnJual);
        btnJual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), SellCarActivity.class));
            }
        });

        listView = (ListView) requireView().findViewById(R.id.homeListView);
        smallAdapter = new HomeListAdapter(requireContext(), carSmallCardArrayList);
        listView.setAdapter(smallAdapter);

        getCarSmallListData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parenView, View childView, int position, long id) {
                TextView txtUsedCarID = childView.findViewById(R.id.txtUsedCarID);
                String UsedCarID = txtUsedCarID.getText().toString().trim();

                Intent i = new Intent(requireActivity(), carInformationActivity.class);
                i.putExtra("usedcar_id", UsedCarID);
                startActivity(i);
            }
        });
    }

    private void getCarSmallListData() {

        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constant.URL_GETCAR_LIST,
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
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Please check your internet connection!",
                            Toast.LENGTH_LONG).show();
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(request);
    }
}