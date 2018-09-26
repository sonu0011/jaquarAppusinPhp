package sonu.kumar.jaquar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sonu.kumar.jaquar.Adapters.ProductsAdapter;
import sonu.kumar.jaquar.Constants.Constant;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.ProductsModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

public class AddToCartActivity extends AppCompatActivity {
    Toolbar toolbar;
    int cat_id,subcat_id,product_id;
    ImageView p_image,fav_image;
    TextView p_code,p_title,p_price;
    String pro_id,pro_title,pro_price,pro_code,pro_image;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    Button addtocartbtn,addtowhishlistbtn;
    String  user_id;
    TextView cartvalue,addtocartwhishlistcount,addtocartcartcount;
    public static final String TAG="AddtoCartActivity";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        addtocartcartcount =findViewById(R.id.addtocartcartcount);
        addtocartwhishlistcount =findViewById(R.id.addtocartwhishlistcount);
        coordinatorLayout =findViewById(R.id.cartCoorrdinatelayout);
        cartvalue =findViewById(R.id.cartValue);
        cartvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddToCartActivity.this,OrderNowActivity.class));
            }
        });
        addtocartbtn =findViewById(R.id.buyProductAddtocartBtn);
        addtowhishlistbtn =findViewById(R.id.buyProductBuyNowBtn);
        p_image =findViewById(R.id.buyProductImage);
        fav_image =findViewById(R.id.addtocarfav);
        p_code =findViewById(R.id.buyProductGetProductCode);
        p_title =findViewById(R.id.buyProductTitle);
        p_price =findViewById(R.id.buyProductGetProductPrice);
        progressDialog =new ProgressDialog(AddToCartActivity.this);
        toolbar =findViewById(R.id.addtocarttoolbar);
        cat_id = getIntent().getIntExtra("cat_id",0);
        subcat_id =getIntent().getIntExtra("subcat_id",0);
        product_id =getIntent().getIntExtra("product_id",0);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Add To Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
         user_id= getSharedPreferences("user_id",MODE_PRIVATE)
                .getString("user_id",null);
        Log.d(TAG, "onCreate: userid "+user_id);
//        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_menu_black_24dp));
        checkWhishlistimage();

    }

    private void checkWhishlistimage() {
        StringRequest stringRequest1 = new StringRequest(
                StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {
                            int color = Color.parseColor("#FF0000");
                            fav_image.setColorFilter(color);

                        }
                        if (response.equals("0")) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("cartwhishlist", "yes");
                map.put("product_id", String.valueOf(product_id));
                map.put("user_id", user_id);
                return map;
            }
        };
        MySingleton.getInstance(AddToCartActivity.this).addToRequestQuee(stringRequest1);
    }

    @Override
        public boolean onSupportNavigateUp() {
            onBackPressed();

            return super.onSupportNavigateUp();
        }
    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final StringRequest stringRequest = new StringRequest(StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                pro_id = jsonObject.getString("product_id");
                                pro_title = jsonObject.getString("product_title");
                                pro_price = jsonObject.getString("product_price");
                                pro_code = jsonObject.getString("product_code");
                                pro_image = jsonObject.getString("product_image");
                                Glide.with(getApplicationContext()).load(pro_image).into(p_image);
                                p_code.setText(pro_code);
                                p_title.setText(pro_title);
                                p_price.setText(pro_price + ".00");


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("SingleProduct", "yes");
                map.put("cat_id", String.valueOf(cat_id));
                map.put("subcat_id", String.valueOf(subcat_id));
                map.put("product_id", String.valueOf(product_id));
                return map;
            }
        };
        MySingleton.getInstance(AddToCartActivity.this).addToRequestQuee(stringRequest);
     addtowhishlistbtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             StringRequest stringRequest1 = new StringRequest(
                     StringRequest.Method.POST,
                     UrlConstants.ALL_REQUEST,
                     new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             if (response.equals("1")) {
                                Snackbar.make(coordinatorLayout,"Product is already in whishlist",Snackbar.LENGTH_SHORT).show();

                             }
                             if (response.equals("0")) {

                                 StringRequest stringRequest =new StringRequest(
                                         StringRequest.Method.POST,
                                         UrlConstants.ALL_REQUEST,
                                         new Response.Listener<String>() {
                                             @Override
                                             public void onResponse(String response) {
                                                 Log.d(TAG, "onResponse: ");
                                                 Log.d(TAG, "onResponse: "+response);
                                                 if (response.equals("0")){
                                                     int color = Color.parseColor("#FF0000");
                                                     fav_image.setColorFilter(color);
                                                     int count = Integer.parseInt(addtocartwhishlistcount.getText().toString());
                                                     count =count+1;
                                                     addtocartwhishlistcount.setText(String.valueOf(count));

                                                     Snackbar.make(coordinatorLayout,"Prduct is added to whishlist",Snackbar.LENGTH_SHORT).show();
                                                 }

                                             }
                                         },
                                         new Response.ErrorListener() {
                                             @Override
                                             public void onErrorResponse(VolleyError error) {
                                                 Log.d(TAG, "onErrorResponse: "+error.getMessage());

                                             }
                                         }
                                 ){
                                     @Override
                                     protected Map<String, String> getParams() throws AuthFailureError {
                                         Map<String,String> map =new HashMap<>();
                                         map.put("whishlist","yes");
                                         map.put("product_id", String.valueOf(product_id));
                                         map.put("user_id", user_id);
                                         return map;
                                     }
                                 };
                                 MySingleton.getInstance(AddToCartActivity.this).addToRequestQuee(stringRequest);

                             }

                         }
                     },
                     new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {
                             Log.d(TAG, "onErrorResponse: " + error.getMessage());

                         }
                     }
             ) {
                 @Override
                 protected Map<String, String> getParams() throws AuthFailureError {
                     Map<String, String> map = new HashMap<>();
                     map.put("cartwhishlist", "yes");
                     map.put("product_id", String.valueOf(product_id));
                     map.put("user_id", user_id);
                     return map;
                 }
             };
             MySingleton.getInstance(AddToCartActivity.this).addToRequestQuee(stringRequest1);
         }
     });


     addtocartbtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             StringRequest sr =new StringRequest(
                     StringRequest.Method.POST,
                     UrlConstants.ALL_REQUEST,
                     new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             Log.d(TAG, "onResponse: add to cart"+response);
                             if (response.equals("1")){
                                 Snackbar.make(coordinatorLayout,"Product is already in cart",Snackbar.LENGTH_SHORT).show();

                             }
                             if (response.equals("0")){
                                 int count = Integer.parseInt(addtocartcartcount.getText().toString());
                                 count =count+1;
                                 addtocartcartcount.setText(String.valueOf(count));
                                 Snackbar.make(coordinatorLayout,"Added to cart successfully",Snackbar.LENGTH_SHORT).show();


                             }

                         }
                     },
                     new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {

                         }
                     }
             ){
                 @Override
                 protected Map<String, String> getParams() throws AuthFailureError {
                     Map<String,String> map =new HashMap<>();

                    map.put("addtocart","yes");
                    map.put("user_id",user_id);
                    map.put("product_id", String.valueOf(product_id));
                    map.put("product_name",pro_title);
                    map.put("product_image",pro_image);
                    map.put("product_code",pro_code);
                    map.put("product_single_price",pro_price);
                    map.put("product_quantity","1");
                    map.put("product_total_price",pro_price);
                     return  map;
                 }
             };
             MySingleton.getInstance(AddToCartActivity.this).addToRequestQuee(sr);
         }
     });
        StringRequest requ =new StringRequest(
                StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: cart count "+response);
                        //cart value
                        addtocartcartcount.setText(response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>map =new HashMap<>();
                map.put("cartcount","yes");
                map.put("user_id",user_id);
                return map;
            }
        };
        MySingleton.getInstance(AddToCartActivity.this).addToRequestQuee(requ);
        StringRequest request =new StringRequest(
                StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: whishlistcount "+response);
                        //whishlist count value
                        addtocartwhishlistcount.setText(response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>map =new HashMap<>();
                map.put("getWhishCount","yes");
                map.put("user_id",user_id);
                return map;
            }
        };
        MySingleton.getInstance(AddToCartActivity.this).addToRequestQuee(request);
    }

    public void gotowhishlist(View view) {
        startActivity(new Intent(AddToCartActivity.this,WhishListActivity.class));
    }

    public void gotoCart(View view) {
        startActivity(new Intent(AddToCartActivity.this,OrderNowActivity.class));

    }
}
