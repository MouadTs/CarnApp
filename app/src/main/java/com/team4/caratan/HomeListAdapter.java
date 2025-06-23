package com.team4.caratan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.toolbox.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

public class HomeListAdapter extends ArrayAdapter<carSmallCard> {

    private ImageLoader imageLoader;
    ImageView car_logo, car_mainPhoto;

    Context context;
    List<carSmallCard> arrayListCarSmallCard;

    public HomeListAdapter(@NonNull Context context, List<carSmallCard> arrayListCarSmallCard) {
        super(context, R.layout.car_row_h, arrayListCarSmallCard);

        this.context = context;
        this.arrayListCarSmallCard = arrayListCarSmallCard;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_row_h, null, true);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //InputStream inputStream;

        TextView txtID = (TextView) view.findViewById(R.id.txtUsedCarID);
        TextView txtMake = (TextView) view.findViewById(R.id.txtCarMake);
        TextView txtModels = (TextView) view.findViewById(R.id.txtCarModel);
        TextView txtMileage = (TextView) view.findViewById(R.id.txtCarMileage);
        TextView txtTransmission = (TextView) view.findViewById(R.id.txtCarTransmission);
        TextView txtLocation = (TextView) view.findViewById(R.id.txtCarLocation);
        TextView txtPrice = (TextView) view.findViewById(R.id.txtCarPrice);

        txtID.setText(arrayListCarSmallCard.get(position).getCar_id());

        //Bitmap carLogo = null, carMainPhoto = null;

        car_logo = (ImageView) view.findViewById(R.id.imgCarLogo);
        car_mainPhoto = (ImageView) view.findViewById(R.id.imgCarMainPhoto);

        String CarLogo_URL = Constant.IMAGE_URL + arrayListCarSmallCard.get(position).getCar_makeLogo();
        /*
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

        String CarMainPhoto_URL = Constant.IMAGE_URL + arrayListCarSmallCard.get(position).getCar_mainPhoto();
        /*
        try {
            inputStream = new URL(CarMainPhoto_URL).openStream();
            carMainPhoto = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (carMainPhoto != null) {
            car_mainPhoto.setImageBitmap(carMainPhoto);
        }

         */

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(CarLogo_URL,
                ImageLoader.getImageListener(car_logo, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        imageLoader.get(CarMainPhoto_URL,
                ImageLoader.getImageListener(car_mainPhoto, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        txtMake.setText(arrayListCarSmallCard.get(position).getCar_make());

        String model = arrayListCarSmallCard.get(position).getCar_model() +
                " " + arrayListCarSmallCard.get(position).getCar_type() +
                " " + arrayListCarSmallCard.get(position).getCar_year();

        txtModels.setText(model);

        DecimalFormat decim = new DecimalFormat("###,###");
        double km = Double.parseDouble(arrayListCarSmallCard.get(position).getCar_mileage());
        String decim_km = decim.format(km);
        txtMileage.setText(decim_km);

        txtTransmission.setText(arrayListCarSmallCard.get(position).getCar_transmission());
        txtLocation.setText(arrayListCarSmallCard.get(position).getCar_location());

        double rp = Double.parseDouble(arrayListCarSmallCard.get(position).getCar_price());
        String decim_rp = decim.format(rp);
        txtPrice.setText(decim_rp + " MAD");

        return view;
    }
}