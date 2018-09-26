package sonu.kumar.jaquar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sonu.kumar.jaquar.BroadcastReceivers.InternetReceiver;
import sonu.kumar.jaquar.Constants.Constant;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

public class LoginActivity extends AppCompatActivity {
        EditText login_email,login_pass;
        Button loginbtn;
        CoordinatorLayout coordinatorLayout;
        public static final String TAG ="LoginActivity";
        String login_email_val,login_pass_val;
        ProgressDialog progressDialog;
        CheckBox checkBox;
        SharedPreferences sharedPreferences,userid;
        SharedPreferences.Editor editor,editor1;
    private InternetReceiver internetReceiver;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: ");
            sharedPreferences =getSharedPreferences("login_details",MODE_PRIVATE);
            userid  = getSharedPreferences("user_id",MODE_PRIVATE);
           editor1 =userid.edit();
            editor =sharedPreferences.edit();
            CheckBoxIsClickedOrNot();
            checkBox =findViewById(R.id.logincheckbox);
            progressDialog =new ProgressDialog(LoginActivity.this);
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            internetReceiver = new InternetReceiver();
            registerReceiver(internetReceiver, intentFilter);
            login_email =findViewById(R.id.login_username);
            login_pass =findViewById(R.id.login_password);
            login_email_val =login_email.getText().toString().trim();
            login_pass_val =login_pass.getText().toString().trim();
            loginbtn =findViewById(R.id.login_btn);
            coordinatorLayout =findViewById(R.id.cordinateLayout);


        }

    private void CheckBoxIsClickedOrNot() {
        if (sharedPreferences!=null){
           String user =  sharedPreferences.getString("username",null);
           String pass =  sharedPreferences.getString("password",null);
           if (user !=null && pass!=null){
               Log.d(TAG, "CheckBoxIsClickedOrNot: "+user+pass);
               Constant.USERNAME=user;
               startActivity(new Intent(LoginActivity.this,HomeActivity.class));
               finish();
           }

        }
    }

    @Override
        protected void onStart() {
            super.onStart();
            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!login_email.getText().toString().trim().isEmpty() && !login_pass.getText().toString().trim().isEmpty()){
                        LOginUser();
                    }
                    else {
                        Snackbar.make(coordinatorLayout,"All fields are required",Snackbar.LENGTH_SHORT).show();
                    }


                }
            });

        }

    private void LOginUser() {
        progressDialog.setMessage("Logging...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();;
        StringRequest stringRequest =new StringRequest(StringRequest.Method.POST
                , UrlConstants.LOGIN_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: ");
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            Log.d(TAG, "onResponse: "+jsonObject);
                           String error =jsonObject.getString("error");
                           if (error.equals("false")){
                               String userid =jsonObject.getString("user_id");
                               Constant.USER_ID =userid;
                               editor1.putString("user_id",userid);
                               editor1.commit();
                               Log.d(TAG, "onResponse: constant user id"+Constant.USER_ID);
                               editor.putString("user_id",userid);
                               editor.commit();
                               Log.d(TAG, "onResponse: user id is "+userid);
                               if (checkBox.isChecked()){
                                   progressDialog.dismiss();
                                   editor.putString("username",login_email.getText().toString());
                                   editor.putString("password",login_pass.getText().toString());
                                   editor.commit();
                                   Constant.USERNAME=login_email.getText().toString();
                                   startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                   finish();
                               }
                               else {
                                   progressDialog.dismiss();
                                   Constant.USERNAME=login_email.getText().toString();
                                   startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                   finish();
                               }
                           }
                           if (error.equals("true")){
                            Snackbar.make(coordinatorLayout,"Invalid username or password",Snackbar.LENGTH_SHORT).show();
                           }
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: ");
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map =new HashMap<>();
                map.put("login_email",login_email.getText().toString());
                map.put("login_pwd",login_pass.getText().toString());
                return map;

            }
        };
        MySingleton.getInstance(LoginActivity.this).addToRequestQuee(stringRequest);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (internetReceiver != null) {
            unregisterReceiver(internetReceiver);
        }
        Log.d(TAG, "onDestroy: "+Constant.USER_ID);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
        CheckBoxIsClickedOrNot();
    }
}


