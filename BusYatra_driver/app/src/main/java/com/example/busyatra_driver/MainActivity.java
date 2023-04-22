package com.example.busyatra_driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.button);
        imageView=findViewById(R.id.imageView);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bustext = "Vellore to vit";
                String busno = "1234";
                String message = bustext + "|" + busno;
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode(message, BarcodeFormat.QR_CODE, 1000, 1000);
                    BarcodeEncoder encoder=new BarcodeEncoder();
                    Bitmap bitmap=encoder.createBitmap(matrix);
                    imageView.setImageBitmap(bitmap);
                    //InputMethodManager methodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                } catch (WriterException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }
}