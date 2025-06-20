package com.team4.caratan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;

import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.team4.caratan.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private mDBhandler dbHandler;
    ActivityMainBinding binding;

    private String email, fullname, pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        dbHandler = new mDBhandler(this);

        try {
            dbHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Users users = dbHandler.getUsers();

        fullname = users.getUser_fullname();
        email = users.getUser_email();
        pp = users.getUser_profilepic();

        dbHandler.close();

        openHome();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.mnHome) {
                openHome();
            } else if (itemId == R.id.mnExplore) {
                openExplore();
            } else if (itemId == R.id.mnTrade) {
                openSell();
            } else if (itemId == R.id.mnProfile) {
                openProfile();
            }

            return true;
        });

    }

    private void openHome() {
        HomeFragment home = new HomeFragment();

        FragmentTransaction fragTrans = getSupportFragmentManager()
                .beginTransaction();

        Bundle data = new Bundle();
        data.putString("nama", fullname);

        home.setArguments(data);
        fragTrans.replace(R.id.mainFrame, home).commit();
    }

    private void openExplore() {
        ExploreFragment explore = new ExploreFragment();

        FragmentTransaction fragTrans = getSupportFragmentManager()
                .beginTransaction();

        fragTrans.replace(R.id.mainFrame, explore).commit();
    }

    private void openSell() {
    }

    private void openProfile() {
        ProfileFragment profile = new ProfileFragment();

        FragmentTransaction fragTrans = getSupportFragmentManager()
                .beginTransaction();

        Bundle data = new Bundle();
        data.putString("nama", fullname);
        data.putString("email", email);
        data.putString("pp", pp);

        profile.setArguments(data);
        fragTrans.replace(R.id.mainFrame, profile).commit();
    }

    public void showLogoutDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog .setCancelable(false);
        dialog.setContentView(R.layout.logout_dialog);

        Button btnLogoutIYA = dialog.findViewById(R.id.btnYes_logout);
        Button btnLogoutTIDAK = dialog.findViewById(R.id.btnNo_logout);

        btnLogoutIYA.setOnClickListener(view -> {
            dialog.dismiss();

            SharedPrefManager.getInstance(getApplicationContext()).logout();

            try {
                dbHandler.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            dbHandler.deleteUsers();
            dbHandler.close();

            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            finish();
        });

        btnLogoutTIDAK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}