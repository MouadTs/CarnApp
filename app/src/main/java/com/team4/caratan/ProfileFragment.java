package com.team4.caratan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.jvm.internal.Ref;

public class ProfileFragment extends Fragment {

    private TextView txtNama, txtEmail;
    private Button btnFav, btnEdtProfil, btnTntApp, btnLogout;
    private String nama, email, pp;
    private ImageView imgProfile;
    private ImageLoader imageLoader;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            nama = data.getString("nama");
            email = data.getString("email");
            pp = data.getString("pp");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtNama = (TextView) requireView().findViewById(R.id.txtProfilNama);
        txtEmail = (TextView) requireView().findViewById(R.id.txtProfilEmail);

        btnFav = (Button) requireView().findViewById(R.id.pflBtnFav);
        btnEdtProfil = (Button) requireView().findViewById(R.id.pflBtnEdtPfl);
        btnTntApp = (Button) requireView().findViewById(R.id.pflBtnTntApp);
        btnLogout = (Button) requireView().findViewById(R.id.pflBtnLogout);
        imgProfile = (ImageView) requireView().findViewById(R.id.imgProfil);

        String imgurl = Constant.IMAGE_URL + pp;
        imageLoader = CustomVolleyRequest.getInstance(requireContext()).getImageLoader();
        imageLoader.get(imgurl,
                ImageLoader.getImageListener(imgProfile, R.drawable.defaultprofileicon, R.drawable.defaultprofileicon));

        txtNama.setText(nama);
        txtEmail.setText(email);

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnEdtProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });
        btnTntApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AboutAppActivity.class));
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) requireActivity()).showLogoutDialog();

            }
        });
    }
}