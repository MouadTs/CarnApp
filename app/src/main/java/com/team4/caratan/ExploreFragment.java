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
import android.widget.Button;

public class ExploreFragment extends Fragment {

    private Button btnToyota, btnHonda, btnBMW, btnMoreMake, btnSearch;

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
            getActivity().getWindow().setStatusBarColor(Color.parseColor("#FFC107"));
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

        btnSearch = requireActivity().findViewById(R.id.button);
        btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), SearchActivity.class));
        });

        btnToyota = requireActivity().findViewById(R.id.explore_btnToyota);
        btnToyota.setOnClickListener(view1 -> {
            Intent i = new Intent(requireActivity(), CarByMakeListActivity.class);
            i.putExtra("make_name", "Toyota");
            startActivity(i);
        });
        btnHonda = requireActivity().findViewById(R.id.explore_btnHonda);
        btnHonda.setOnClickListener(view1 -> {
            Intent i = new Intent(requireActivity(), CarByMakeListActivity.class);
            i.putExtra("make_name", "Honda");
            startActivity(i);
        });
        btnBMW = requireActivity().findViewById(R.id.explore_btnBMW);
        btnBMW.setOnClickListener(view1 -> {
            Intent i = new Intent(requireActivity(), CarByMakeListActivity.class);
            i.putExtra("make_name", "BMW");
            startActivity(i);
        });

        btnMoreMake = requireActivity().findViewById(R.id.btnMoreMake);
        btnMoreMake.setOnClickListener(view1 -> startActivity(new Intent(requireActivity(), CarMakeListActivity.class)));
    }
}