package sonu.kumar.jaquar.Activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import sonu.kumar.jaquar.Constants.CheckInternetCoonnection;
import sonu.kumar.jaquar.R;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    CoordinatorLayout coordinatorLayout;
    AlertDialog.Builder mbuilder;
    AlertDialog alertDialog;

    public static final String TAG = "SplashActivity";

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mbuilder = new AlertDialog.Builder(SplashActivity.this);

        Log.d(TAG, "onCreate: ");

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                setContentView(R.layout.activity_splash);
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();

                    }
                };
                handler.postDelayed(runnable, 2000);
            } else {
                boolean b = new CheckInternetCoonnection().CheckNetwork(SplashActivity.this);
                if (!b) {
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.internetconnectiondialog, null);
                    mbuilder.setView(view);
                    mbuilder.setCancelable(false);
                    mbuilder.create();
                    alertDialog = mbuilder.show();

                    view.findViewById(R.id.cancel_internet).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            alertDialog.dismiss();
                            finish();

                        }
                    });
                    view.findViewById(R.id.interner_settings).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//                        startActivity(intent);
                        }
                    });
                }

//                Snackbar.make(coordinatorLayout,"No Internet Connection",Snackbar.LENGTH_SHORT).show();
//                handler = new Handler();
//                runnable = new Runnable() {
//                    @Override
//                    public void run() {
////                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
//                        finish();
//
//                    }
//                };
//                handler.postDelayed(runnable,1000);
            }
        }

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {


                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();

                    }
                };
                handler.postDelayed(runnable, 2000);
            } else {
                boolean b = new CheckInternetCoonnection().CheckNetwork(SplashActivity.this);
                if (!b) {
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.internetconnectiondialog, null);
                    mbuilder.setView(view);
                    mbuilder.setCancelable(false);
                    mbuilder.create();
                    final AlertDialog alertDialog = mbuilder.show();

                    view.findViewById(R.id.cancel_internet).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            alertDialog.dismiss();
                            finish();

                        }
                    });
                    view.findViewById(R.id.interner_settings).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//                        startActivity(intent);
                        }
                    });
                }
                Log.d(TAG, "onRestart: ");
            }
        }
    }
}
