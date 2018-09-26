package sonu.kumar.jaquar.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sonu.kumar.jaquar.Adapters.HomeActivityAdapter;
import sonu.kumar.jaquar.Adapters.ProductsAdapter;
import sonu.kumar.jaquar.Constants.Constant;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.ProductsModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    CircleImageView profileimage;
    TextView username,newarrivalscount,whishlistcount,fabCounter,useremail;
    String user_name,user_id;

    NavigationView navigationView;
    SharedPreferences sharedPreferences;
    AppBarLayout appBarLayout;
    Button shopbycategory,searchforprductsbtn;
    public static final String TAG="HomeActivity";
    List<ProductsModel> list;
    HomeActivityAdapter adapter;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;
    private String unique_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fabCounter =findViewById(R.id.fabCounter);
        unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        Log.d(TAG, "onCreate: "+unique_id);
        recyclerView =findViewById(R.id.homerecycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        coordinatorLayout =findViewById(R.id.homecordinatelayout);
        user_id = getSharedPreferences("user_id",MODE_PRIVATE)
                .getString("user_id",null);
        Log.d(TAG, "onCreate: userid "+user_id);
        list =new ArrayList<>();
        progressDialog =new ProgressDialog(HomeActivity.this);
        IntializeallThings();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getApplicationContext(),R.anim.slide_bottom);
        recyclerView.setLayoutAnimation(layoutAnimationController);

    }

    private void IntializeallThings() {
        sharedPreferences =getSharedPreferences("login_details",MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(HomeActivity.this,UpdateProfileActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        profileimage =navigationView.getHeaderView(0).findViewById(R.id.imageViewProfile);
        username =navigationView.getHeaderView(0).findViewById(R.id.userNameProfile);
        useremail =navigationView.getHeaderView(0).findViewById(R.id.userEmailProfile);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.profilelogo);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        profileimage.setImageDrawable(roundedBitmapDrawable);
        Log.d(TAG, "onCreate: "+Constant.USERNAME);
        initializeCountDrawer();
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initializeCountDrawer() {
        newarrivalscount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_newarrivals));
        whishlistcount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_whishlist));

        newarrivalscount.setGravity(Gravity.CENTER_VERTICAL);
        newarrivalscount.setTypeface(null, Typeface.BOLD);
        newarrivalscount.setTextColor(Color.RED);
        newarrivalscount.setText("5");
        whishlistcount.setGravity(Gravity.CENTER_VERTICAL);
        whishlistcount.setTypeface(null, Typeface.BOLD);
        whishlistcount.setTextColor(Color.RED);
        StringRequest stringRequest =new StringRequest(
                StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: whishlistcount "+response);
                        whishlistcount.setText(response);


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
        MySingleton.getInstance(HomeActivity.this).addToRequestQuee(stringRequest);

        appBarLayout =findViewById(R.id.appbarlayout);
        shopbycategory =appBarLayout.findViewById(R.id.shopByCategotyBtn);
        searchforprductsbtn =appBarLayout.findViewById(R.id.searchforproductsbtn);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {

        } else if (id == R.id.nav_newarrivals) {
            startActivity(new Intent(HomeActivity.this,NewArrivalsActivity.class));

        } else if (id == R.id.nav_whishlist) {
            startActivity(new Intent(HomeActivity.this,WhishListActivity.class));

        } else if (id == R.id.nav_orderHIstory) {

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Jaquar App");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.agbe.jaquar");
            startActivity(Intent.createChooser(intent, "Share Via"));

        } else if (id == R.id.nav_logout) {
            new  Constant().ShowLogoutDialog(HomeActivity.this);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        Log.d(TAG, "onDestroy: "+Constant.USERNAME);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        shopbycategory.setOnClickListener(this);
        searchforprductsbtn.setOnClickListener(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            Log.d(TAG, "onResponse: " + response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                list.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    list.add(new ProductsModel(
                                            jsonObject.getString("product_title"),
                                            jsonObject.getString("product_price"),
                                            jsonObject.getString("product_code"),
                                            jsonObject.getString("product_image"),
                                            jsonObject.getInt("product_id"),
                                            jsonObject.getInt("cat_id"),
                                            jsonObject.getInt("subcat_id")
                                    ));
                                }
                                adapter = new HomeActivityAdapter(list,HomeActivity.this,coordinatorLayout);
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                map.put("HomeProducts", "set");
                return map;
            }
        };
        MySingleton.getInstance(HomeActivity.this).addToRequestQuee(stringRequest);

        StringRequest stringRequest1 =new StringRequest(
                StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: cart count "+response);
                        fabCounter.setText(response);



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
        MySingleton.getInstance(HomeActivity.this).addToRequestQuee(stringRequest1);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shopByCategotyBtn:
                startActivity(new Intent(HomeActivity.this,ShopByCategory.class));
                break;
            case R.id.searchforproductsbtn:
                startActivity(new Intent(HomeActivity.this,SearchForProductsActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                break;
        }

    }

    public void gotocart(View view) {
        startActivity(new Intent(HomeActivity.this,OrderNowActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        if (response!=null){
                            try {
                                JSONArray jsonArray =new JSONArray(response);
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject =jsonArray.getJSONObject(i);
                                    String  email  = jsonObject.getString("user_email");
                                    if (email !=null){
                                        useremail.setText(email);
                                    }
                                    String name =jsonObject.getString("user_name");
                                    if (name !=null){
                                        username.setText("Hi, "+name);
                                    }
                                    String image =jsonObject.getString("user_profile_pic");
                                    if (image!=null){
                                        Log.d(TAG, "onResponse: "+image);
                                        Glide.with(HomeActivity.this).load(image).into(profileimage);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

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
                map.put("fetchprofiledetails", "set");
                map.put("user_id", user_id);
                return map;
            }
        };
        MySingleton.getInstance(HomeActivity.this).addToRequestQuee(stringRequest);
    }
}
