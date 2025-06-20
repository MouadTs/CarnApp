package com.team4.caratan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class SellCar_PhotoFragment extends Fragment {

    private Button btnNext;
    private ImageView photo1, photo2, photo3;
    private CardView cv2, cv3;
    private Bitmap bmp_photo1 = null, bmp_photo2 = null, bmp_photo3 = null;

    public SellCar_PhotoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sell_car__photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnNext = requireView().findViewById(R.id.SELL_btnNext2);

        photo1 = requireView().findViewById(R.id.sell_carPhoto1);
        photo2 = requireView().findViewById(R.id.sell_carPhoto2);
        photo3 = requireView().findViewById(R.id.sell_carPhoto3);

        cv2 = requireView().findViewById(R.id.cardView2);
        cv3 = requireView().findViewById(R.id.cardView3);

        ActivityResultLauncher<Intent> activityResultLauncher1 =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {

                                if (result.getResultCode() == Activity.RESULT_OK) {

                                    Intent data = result.getData();
                                    Uri uri = data.getData();
                                    try {
                                        bmp_photo1 = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                                        cv2.setVisibility(View.VISIBLE);
                                        requireActivity().runOnUiThread(() -> photo1.setImageBitmap(bmp_photo1));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
        ActivityResultLauncher<Intent> activityResultLauncher2 =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {

                                if (result.getResultCode() == Activity.RESULT_OK) {

                                    Intent data = result.getData();
                                    Uri uri = data.getData();
                                    try {
                                        bmp_photo2 = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                                        cv3.setVisibility(View.VISIBLE);
                                        requireActivity().runOnUiThread(() -> photo2.setImageBitmap(bmp_photo2));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
        ActivityResultLauncher<Intent> activityResultLauncher3 =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {

                                if (result.getResultCode() == Activity.RESULT_OK) {

                                    Intent data = result.getData();
                                    Uri uri = data.getData();
                                    try {
                                        bmp_photo3 = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);

                                        requireActivity().runOnUiThread(() -> photo3.setImageBitmap(bmp_photo3));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher1.launch(i);
            }
        });
        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher2.launch(i);
            }
        });
        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher3.launch(i);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bmp_photo1 == null) {
                    Toast.makeText(requireContext(), "Photo 1 cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(bmp_photo2 == null) {
                    Toast.makeText(requireContext(), "Photo 2 cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(bmp_photo3 == null) {
                    Toast.makeText(requireContext(), "Photo 3 cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }

                ((SellCarActivity)getActivity()).set_photo(bmp_photo1, bmp_photo2, bmp_photo3);

                ((SellCarActivity)getActivity()).add_progress(33);
                ((SellCarActivity)getActivity()).openSellCar_Final();
            }
        });
    }
}