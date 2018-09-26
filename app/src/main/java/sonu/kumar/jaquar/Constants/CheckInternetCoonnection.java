package sonu.kumar.jaquar.Constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by sonu on 1/9/18.
 */

public class CheckInternetCoonnection {
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    private AlertDialog.Builder mbuilder;

    public boolean CheckNetwork(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}