package com.example.scanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnTakePicture, btnScanBarcode;
    private static final int REQUEST_CAMERA_PERMISSION = 201;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnTakePicture.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnScanBarcode:

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, ScannedBarcodeActivity.class));

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new
                            String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                }


                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        startActivity(new Intent(MainActivity.this, ScannedBarcodeActivity.class));


                    } else
                        Toast.makeText(getApplicationContext(), "Denied. Go to setting and enable camera option", Toast.LENGTH_SHORT).show();

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
