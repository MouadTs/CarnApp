package com.team4.caratan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PembayaranActivity extends AppCompatActivity {

    private ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        btnClose = findViewById(R.id.PAY_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        openPilih_Pembayaran();
    }

    public void openPilih_Pembayaran() {

        Pilih_pembayranFragment PF = new Pilih_pembayranFragment();

        FragmentTransaction fragTrans = getSupportFragmentManager()
                .beginTransaction();

        fragTrans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        fragTrans.replace(R.id.PAY_frame, PF).commit();
    }
}