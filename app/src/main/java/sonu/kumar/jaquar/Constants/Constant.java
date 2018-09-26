package sonu.kumar.jaquar.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import sonu.kumar.jaquar.Activity.HomeActivity;
import sonu.kumar.jaquar.Activity.LoginActivity;
import sonu.kumar.jaquar.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sonu on 1/9/18.
 */

public class Constant {
    public static String USERNAME ="";
    public static String  USER_ID;
    private SharedPreferences sharedPreferences;


    public void ShowLogoutDialog(final Context context){
        sharedPreferences =context.getSharedPreferences("login_details",MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Dialog);
        builder
                .setIcon(context.getResources().getDrawable(R.drawable.logout))
                .setTitle("Log out").setMessage("Are you sure you want to Log out?")
                .setCancelable(false)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sharedPreferences.edit().clear().apply();
                        USER_ID="";
                        Intent i = new Intent(context, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);

                        ((Activity)context).finish();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
