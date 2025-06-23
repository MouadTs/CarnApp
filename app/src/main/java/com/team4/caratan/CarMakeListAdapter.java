package com.team4.caratan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class CarMakeListAdapter extends ArrayAdapter<carMakeGetSet> {

    private ImageLoader imageLoader;
    ImageView car_logo;

    Context context;
    List<carMakeGetSet> arrayListCarMake;

    public CarMakeListAdapter(@NonNull Context context, List<carMakeGetSet> arrayListCarMake) {
        super(context, R.layout.carmake_row, arrayListCarMake);

        this.context = context;
        this.arrayListCarMake = arrayListCarMake;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carmake_row, null, true);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        TextView txtMakeName = (TextView) view.findViewById(R.id.carMakeList_name);
        TextView txtCounts = (TextView) view.findViewById(R.id.carMakeList_counts);
        car_logo = (ImageView) view.findViewById(R.id.carMakeList_logo);

        /*
        InputStream inputStream;

        Bitmap carLogo = null;

        String CarLogo_URL = Constant.IMAGE_URL + arrayListCarMake.get(position).getMake_logo();
        try {
            inputStream = new URL(CarLogo_URL).openStream();
            carLogo = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (carLogo != null) {
            car_logo.setImageBitmap(carLogo);
        }
         */

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(Constant.IMAGE_URL + arrayListCarMake.get(position).getMake_logo(),
                ImageLoader.getImageListener(car_logo, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        if(Objects.equals(arrayListCarMake.get(position).getCounts(), "0")) {
            txtCounts.setTextColor(Color.parseColor("#BFBFBF"));
        }

        txtMakeName.setText(arrayListCarMake.get(position).getMake_name());
        txtCounts.setText(arrayListCarMake.get(position).getCounts());

        return view;
    }
}
