package com.example.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.model.ScannerReq;
import com.example.model.ScannerResp;
import com.example.retrofit.APIService;
import com.example.retrofit.ApiUtils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannedBarcodeActivity extends AppCompatActivity {


    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;
    private String mUniqueID = "";
    private APIService mAPIService;
    private String TAG = ScannedBarcodeActivity.class.toString();
    boolean isNetworkCall = true;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        mProgressBar = findViewById(R.id.progressBar);


    }

    private void initialiseDetectorsAndSources() {


        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                //Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
                            } else {
                                isEmail = false;
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);
                                // Getting value from Scanner
                                //Product ID: 10278
                                //Product Name: Gate 12
                                //Timezone: ET
                                ScannerReq aReq = new ScannerReq();
                                JsonObject postParam = new JsonObject();

                                if (intentData.contains("\n")) {

                                    String[] aSperate = intentData.split("\n");
                                    if (aSperate.length > 0) {
                                        aReq.setUniqueId(mUniqueID);
                                        postParam.addProperty("uniqueId", mUniqueID);

                                        for (int i = 0; i < aSperate.length; i++) {
                                            if (aSperate[i].contains(":")) {
                                                String[] aInnerSep = aSperate[i].split(":");
                                                if (i == 0) {
                                                    aReq.setProductId(aInnerSep[1]);
                                                    postParam.addProperty("productId", aInnerSep[1]);
                                                } else if (i == 1) {
                                                    aReq.setProductName(aInnerSep[1]);
                                                    postParam.addProperty("productName", aInnerSep[1]);

                                                } else if (i == 2) {
                                                    aReq.setTimeZone(aInnerSep[1]);
                                                    postParam.addProperty("timeZone", aInnerSep[1]);

                                                }
                                            }
                                        }

                                    }
                                }


                                if (ApiUtils.checkNetwork(ScannedBarcodeActivity.this)) {
                                    //  barcodeDetector.release();
                                    Log.e("JSON", postParam.toString());
                                    //  sendData(postParam.toString());
                                    if (isNetworkCall) {
                                        sendData(aReq);
                                        barcodeDetector.release();
                                    }


                                } else {
                                    Toast.makeText(ScannedBarcodeActivity.this, "No Network please check network connection", Toast.LENGTH_SHORT).
                                            show();
                                }
                                //showResponse(intentData);

                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
        deterUniqueID();
        txtBarcodeValue.setText("No Barcode Detected");


    }

    private void toCallSurface() {
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

    }

    private void deterUniqueID() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String advertId = null;
                try {
                    advertId = idInfo.getId();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
                mUniqueID = advertId;
                //  Toast.makeText(getApplicationContext(), mUniqueID, Toast.LENGTH_SHORT).show();

            }

        };
        task.execute();

    }


    public void sendData(ScannerReq aPostData) {

        mProgressBar.setVisibility(View.VISIBLE);

        isNetworkCall = false;

        mAPIService = ApiUtils.getAPIService();

        mAPIService.sendData(aPostData).enqueue(new Callback<ScannerResp>() {
            @Override
            public void onResponse(Call<ScannerResp> call, Response<ScannerResp> aResp) {
                if (aResp.isSuccessful()) {
                    mProgressBar.setVisibility(View.GONE);
                    ScannerResp aRespData = aResp.body();
                    showResponse(aRespData);
                    Log.i(TAG, "post submitted to API." + aResp.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ScannerResp> call, Throwable t) {
                Log.e("URL", call.request().url().toString());
                mProgressBar.setVisibility(View.GONE);

                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void showResponse(ScannerResp aResponse) {
        isNetworkCall = true;
        Intent aIntent = new Intent(ScannedBarcodeActivity.this, ScanInfoDetailActivity.class);
        Gson aGson = new Gson();
        aIntent.putExtra("Data", aGson.toJson(aResponse));
        startActivity(aIntent);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {


                    } else
                        Toast.makeText(getApplicationContext(), "Denied. Go to setting and enable camera option", Toast.LENGTH_SHORT).show();

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
