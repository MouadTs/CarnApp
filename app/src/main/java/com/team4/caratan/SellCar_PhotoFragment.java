package com.team4.caratan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SellCar_PhotoFragment extends Fragment {

    private Button btnNext;
    private ImageView photo1, photo2, photo3;
    private CardView cv2, cv3;
    private Bitmap bmp_photo1 = null, bmp_photo2 = null, bmp_photo3 = null;
    
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String currentPhotoPath;
    private int currentPhotoIndex = 0; // 1, 2, or 3 for which photo is being selected

    // Activity result launchers
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    public SellCar_PhotoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize permission launcher
        permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    showPhotoSourceDialog(currentPhotoIndex);
                } else {
                    Toast.makeText(requireContext(), "Permission required to access photos", Toast.LENGTH_SHORT).show();
                }
            }
        );

        // Initialize gallery launcher
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        processSelectedImage(uri, currentPhotoIndex);
                    }
                }
            }
        );

        // Initialize camera launcher
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    File file = new File(currentPhotoPath);
                    if (file.exists()) {
                        Uri uri = Uri.fromFile(file);
                        processSelectedImage(uri, currentPhotoIndex);
                    }
                }
            }
        );
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

        // Set click listeners for photos
        photo1.setOnClickListener(v -> {
            currentPhotoIndex = 1;
            checkPermissionAndShowDialog();
        });
        
        photo2.setOnClickListener(v -> {
            currentPhotoIndex = 2;
            checkPermissionAndShowDialog();
        });
        
        photo3.setOnClickListener(v -> {
            currentPhotoIndex = 3;
            checkPermissionAndShowDialog();
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if at least one photo is selected
                if(bmp_photo1 == null) {
                    Toast.makeText(requireContext(), "Please select at least one photo!", Toast.LENGTH_LONG).show();
                    return;
                }

                // If photos 2 and 3 are not selected, use photo 1 as default
                if(bmp_photo2 == null) {
                    bmp_photo2 = bmp_photo1;
                }
                if(bmp_photo3 == null) {
                    bmp_photo3 = bmp_photo1;
                }

                ((SellCarActivity)getActivity()).set_photo(bmp_photo1, bmp_photo2, bmp_photo3);

                ((SellCarActivity)getActivity()).add_progress(33);
                ((SellCarActivity)getActivity()).openSellCar_Final();
            }
        });
    }

    private void checkPermissionAndShowDialog() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            showPhotoSourceDialog(currentPhotoIndex);
        }
    }

    private void showPhotoSourceDialog(int photoIndex) {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Photo Source for Photo " + photoIndex);
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Camera
                openCamera();
            } else {
                // Gallery
                openGallery();
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(requireContext(), "Error creating image file", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (photoFile != null) {
                try {
                    Uri photoURI = FileProvider.getUriForFile(requireContext(),
                            "com.team4.caratan.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    cameraLauncher.launch(takePictureIntent);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Camera not available, please use gallery", Toast.LENGTH_SHORT).show();
                    openGallery();
                }
            }
        } else {
            Toast.makeText(requireContext(), "Camera not available, please use gallery", Toast.LENGTH_SHORT).show();
            openGallery();
        }
    }

    private void openGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            galleryLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Gallery not available", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void processSelectedImage(Uri uri, int photoIndex) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
            
            // Compress the image to reduce file size
            Bitmap compressedBitmap = compressImage(bitmap);
            
            // Set the bitmap based on photo index
            switch (photoIndex) {
                case 1:
                    bmp_photo1 = compressedBitmap;
                    requireActivity().runOnUiThread(() -> photo1.setImageBitmap(compressedBitmap));
                    cv2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    bmp_photo2 = compressedBitmap;
                    requireActivity().runOnUiThread(() -> photo2.setImageBitmap(compressedBitmap));
                    cv3.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    bmp_photo3 = compressedBitmap;
                    requireActivity().runOnUiThread(() -> photo3.setImageBitmap(compressedBitmap));
                    break;
            }
            
            Toast.makeText(requireContext(), "Photo " + photoIndex + " added successfully!", Toast.LENGTH_SHORT).show();
            
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap compressImage(Bitmap originalBitmap) {
        // Calculate new dimensions (max 1024px width/height)
        int maxDimension = 1024;
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        
        if (width > maxDimension || height > maxDimension) {
            float scale = Math.min((float) maxDimension / width, (float) maxDimension / height);
            int newWidth = Math.round(width * scale);
            int newHeight = Math.round(height * scale);
            
            originalBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        }
        
        // Compress quality
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        byte[] compressedBytes = outputStream.toByteArray();
        
        return BitmapFactory.decodeByteArray(compressedBytes, 0, compressedBytes.length);
    }
}