package com.example.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.scanner.MainActivity;

public class ApiUtils {

    private ApiUtils() {
    }

    public static APIService getAPIService() {

        return RetrofitClient.getClient().create(APIService.class);
    }


    public static boolean checkNetwork(Context aContext) {
        ConnectivityManager ConnectionManager = (ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            return true;
        } else {
            return false;

        }
    }
}
