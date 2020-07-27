package com.example.scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.model.ScannerReq;
import com.example.model.ScannerResp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ScanInfoDetailActivity extends AppCompatActivity {

    private AppCompatTextView mProductId, mProductName, mProductTime, mProductDetailTXT, mScanCountTXT, mUserIDTXT;
    private String mValue;
    private AppCompatImageView mBackIM;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_info);
        initViews();
        getBundleValue();
    }

    private void getBundleValue() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            mValue = null;
        } else {
            mValue = extras.getString("Data");
            ScannerResp aData = new Gson().fromJson(mValue, ScannerResp.class);
            toSetValue(aData);
            mProductDetailTXT.setText(mValue);
        }
    }

    private void toSetValue(ScannerResp aData) {

        mProductId.setText(" " + aData.getProductId().trim());
        mProductName.setText(" " + aData.getProductName().trim());
        mProductTime.setText(" " + aData.getTimeZone());
        mScanCountTXT.setText(" " + aData.getScancount());
        mProductTime.setText(" " + aData.getTimeZone());
        mUserIDTXT.setText(" " + aData.getUniqueId());

    }

    private void initViews() {
        mContext = ScanInfoDetailActivity.this;
        mProductId = (AppCompatTextView) findViewById(R.id.productIDTXT);
        mProductName = (AppCompatTextView) findViewById(R.id.productNameTXT);
        mProductTime = (AppCompatTextView) findViewById(R.id.productTimeZoneTXT);
        mProductDetailTXT = (AppCompatTextView) findViewById(R.id.productDetailInfoTXT);
        mUserIDTXT = (AppCompatTextView) findViewById(R.id.userIDTXT);
        mScanCountTXT = (AppCompatTextView) findViewById(R.id.scanCountTXT);
        mBackIM = (AppCompatImageView) findViewById(R.id.backIM);
        mBackIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
