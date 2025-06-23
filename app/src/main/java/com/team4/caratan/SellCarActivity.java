package com.team4.caratan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SellCarActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ProgressBar progress_h;
    private TextView title_sell;

    public String carMake, carModel, carType, carColor, carYear, carMileage, carPrice, carDesc, carLocation;
    public Bitmap photo1, photo2, photo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_car);

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        title_sell = findViewById(R.id.title_sell);

        btnBack = findViewById(R.id.SELL_btnBack);
        btnBack.setOnClickListener(view -> finish());

        progress_h = findViewById(R.id.progress_sell);
        progress_h.setMax(99);

        openSellCar_Info();
    }

    public void openSellCar_Info() {

        title_sell.setText("Complete your car information");
        SellCar_InfoFragment CarInfo = new SellCar_InfoFragment();

        FragmentTransaction fragTrans = getSupportFragmentManager()
                .beginTransaction();

        fragTrans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        fragTrans.replace(R.id.SELL_frame, CarInfo).commit();
    }

    public void openSellCar_Photo() {

        title_sell.setText("Upload your car photos");
        SellCar_PhotoFragment CarPhoto = new SellCar_PhotoFragment();

        FragmentTransaction fragTrans = getSupportFragmentManager()
                .beginTransaction();

        fragTrans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        fragTrans.replace(R.id.SELL_frame, CarPhoto).commit();
    }

    public void openSellCar_Final() {

        title_sell.setText("Check your car information again");
        SellCar_CoFragment CarCo = new SellCar_CoFragment();

        FragmentTransaction fragTrans = getSupportFragmentManager()
                .beginTransaction();

        fragTrans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        fragTrans.replace(R.id.SELL_frame, CarCo).commit();
    }

    public void set_photo(Bitmap a, Bitmap b, Bitmap c) {
        this.photo1 = a;
        this.photo2 = b;
        this.photo3 = c;
    }

    public String getSelectedMake() {
        return carMake;
    }

    public String getSelectedModel() {
        return carModel;
    }

    public String getSelectedType() {
        return carType;
    }

    public String getSelectedColor() {
        return carColor;
    }

    public String getYear() {
        return carYear;
    }

    public String getMileage() {
        return carMileage;
    }

    public String getPrice() {
        return carPrice;
    }

    public String getDesc() {
        return carDesc;
    }

    public String getLocation() {
        return carLocation;
    }

    public Bitmap getPhoto1() {
        return photo1;
    }

    public Bitmap getPhoto2() {
        return photo2;
    }

    public Bitmap getPhoto3() {
        return photo3;
    }

    public void add_progress(int n) {
        int o = progress_h.getProgress() + n;
        progress_h.setProgress(o);
    }
}