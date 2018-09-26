package sonu.kumar.jaquar.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sonu.kumar.jaquar.Adapters.ProductsAdapter;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.ProductsModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

public class UpdateProfileActivity extends AppCompatActivity {
    CircleImageView profile_image;
    EditText user_name;
    String  name,user_id;
    Button updata_profile;
    Uri imageUri;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    public static final String TAG ="UpdataProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        progressDialog =new ProgressDialog(UpdateProfileActivity.this);
        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profile_image = findViewById(R.id.signup_user_profile_imag);
        user_id= getSharedPreferences("user_id",MODE_PRIVATE)
                .getString("user_id",null);
        user_name = findViewById(R.id.signup_user_name);
        updata_profile = findViewById(R.id.signup_updata_profile_btn);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(ContextCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                    }
                    else
                    {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(UpdateProfileActivity.this);
                    }
                }
                else
                {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(UpdateProfileActivity.this);
                }

            }
        });
        updata_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name =user_name.getText().toString().trim();
                if(name.isEmpty())
                {
                    user_name.setError("Enter your name");
                    user_name.requestFocus();
                }
                if(imageUri == null)
                {
                    Toast.makeText(UpdateProfileActivity.this, "Plese select a profile image", Toast.LENGTH_SHORT).show();
                }
                if(!name.isEmpty() && imageUri!=null)
                {
                    progressDialog.setMessage("Updating profile...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                        UploadImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                profile_image.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return super.onSupportNavigateUp();
    }
    private  void UploadImage(){

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      if (response.equals("1")){
                          progressDialog.dismiss();
                          Toast.makeText(UpdateProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(UpdateProfileActivity.this,HomeActivity.class));
                      }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateProfileActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("UpdateProfile", "set");
                map.put("user_id", user_id);
                map.put("user_name",name);
                map.put("profile_pic",imageToString(bitmap));
                return map;
            }
        };
        progressDialog.dismiss();
        MySingleton.getInstance(UpdateProfileActivity.this).addToRequestQuee(stringRequest);


    }
    private  String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imagebytes =byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagebytes,Base64.DEFAULT);

    }

}
