package com.example.arcane;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.arcane.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.schema.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImageClassify extends AppCompatActivity {

    TextView result, confidence;
    ImageView foodImage;
    Button openCamera, openGallery, openFoodDecompositon;
    int imageSize = 224;
    DBHelper db = new DBHelper(this);
    String key_value, foodCATEGORY = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_classify);

        setTitle("Classify Food Waste");

        // grab constraint layout and store it into the variable
        RelativeLayout constraintLayout = findViewById(R.id.imageClassifyLayout);

        // create animation drawable object
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        result      = findViewById(R.id.txt_result);
        confidence  = findViewById(R.id.txt_confidence);
        foodImage   = findViewById(R.id.iv_foodImage);
        openCamera  = findViewById(R.id.btn_openCamera);
        openGallery = findViewById(R.id.btn_openGallery);
        openFoodDecompositon = findViewById(R.id.btn_composeFoodWaste2);


        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch camera if has permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);
                    }else{
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    }
                }
            }
        });

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 2);
                    }else{
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    }
                }
            }
        });

        openFoodDecompositon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(foodCATEGORY != null)
                        compostTech(foodCATEGORY);
                    else
                        Toast.makeText(ImageClassify.this, "Food Category is not classified", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(ImageClassify.this, "Food Category is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    } // End of onCreate method

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){

        // Take picture with camera
        if(requestCode == 1 && resultCode == RESULT_OK){
            // Contains the image that user took from the camera
            Bitmap image = (Bitmap) data.getExtras().get("data");

            // Crop the image to feed the square image into the model
            int dimension = Math.min(image.getWidth(), image.getHeight());

            // Perform center crop
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            foodImage.setImageBitmap(image);

            // Rescale the image to 224x224
            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);

        }else{ // Upload picture from gallery
            Uri dat = data.getData();
            Bitmap image = null;

            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Error on Image Classify - Upload Picture", Toast.LENGTH_SHORT).show();
            }

            foodImage.setImageBitmap(image);
            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void classifyImage(Bitmap image){
        try {
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            //Create input for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // Get the pixel values from the image
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            // to keep track which pixel is currently on
            int pixel = 0;

            // Go through all 224x224 pixels of image
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB

                    // Perform bitwise operation in order to extract RGB values from pixels
                    byteBuffer.putFloat(((val >> 16 ) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8 ) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val & 0xFF ) & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();

            // Iterate through the confidences in the array
            // Find the position with the highest  values
            int maxPos = 0; float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            // Display the accurate class. Refer to teachable machine labels
            String [] classes = {"Milk", "Apple", "Banana", "Cabbage", "Bread", "Rice"};
            result.setText(classes[maxPos]);

            // Display confidence for all classes
            /*String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            confidence.setText(s);*/
            confidence.setText(classes[maxPos] + " : " + (maxConfidence * 100) + " %");

            String foodCategory = db.classifyFoodCategory(classes[maxPos]);
            // receive data from another activity
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                key_value = extras.getString("key");
                //The key argument here must match that used in the other activity
            }
            db.addFood(foodCategory,classes[maxPos],"Image Classified", key_value);
            foodCATEGORY = foodCategory;
            //compostTech(foodCategory);

            // Releases model resources if no longer used.
            model.close();

        }catch (IOException e){
            Toast.makeText(this, "Error on Data Model", Toast.LENGTH_SHORT).show();
        }
    }

    // Generate food decompositon technique based on food category
    public void compostTech(String category){
        if(category.toLowerCase().equals("fruits") || category.toLowerCase().equals("vegetables")){
            Intent aaa = new Intent(ImageClassify.this, CompostPit.class);
            startActivity(aaa);
        }else if(category.toLowerCase().equals("grains")){
            Intent aaa = new Intent(ImageClassify.this, CompostVermi.class);
            startActivity(aaa);
        }else{
            Intent aaa = new Intent(ImageClassify.this, Alternative.class);
            startActivity(aaa);
        }
    }

}