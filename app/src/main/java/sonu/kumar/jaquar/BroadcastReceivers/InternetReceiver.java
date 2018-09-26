package sonu.kumar.jaquar.BroadcastReceivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import sonu.kumar.jaquar.Activity.LoginActivity;
import sonu.kumar.jaquar.R;

/**
 * Created by sonu on 1/9/18.
 */

public class InternetReceiver extends BroadcastReceiver {
    private static final String TAG ="INternetReceiver" ;
    private AlertDialog.Builder mbuilder;
    AlertDialog alertDialog;

    @Override
    public void onReceive(final Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected())
        {
            mbuilder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.internetconnectiondialog, null);
            mbuilder.setView(view);
            mbuilder.setCancelable(false);
            mbuilder.create();
            alertDialog = mbuilder.show();

            view.findViewById(R.id.cancel_internet).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick:Cancel ");
                    alertDialog.dismiss();
                    ((Activity)context).finishAffinity();


                }
            });
            view.findViewById(R.id.interner_settings).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: InternetSettings");
                    context.startActivity(new Intent(Settings.ACTION_SETTINGS));
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//                        startActivity(intent);
                }
            });
        }
        else
        {
            if (alertDialog!=null) {
                alertDialog.dismiss();
            }
        }

    }
}
