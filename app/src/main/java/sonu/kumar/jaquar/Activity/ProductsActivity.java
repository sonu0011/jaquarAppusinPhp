package sonu.kumar.jaquar.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sonu.kumar.jaquar.Adapters.ProductsAdapter;
import sonu.kumar.jaquar.Adapters.SubCategoryAdapter;
import sonu.kumar.jaquar.Constants.Constant;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.ProductsModel;
import sonu.kumar.jaquar.Models.ShopBYCategoryModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

public class ProductsActivity extends AppCompatActivity {
ProductsAdapter adapter;
List<ProductsModel>list;
String sub_cat_title;
int cat_id,parent_id;
    private ProgressDialog progressDialog;
    public static final String TAG ="ProductActivity";
    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        coordinatorLayout =findViewById(R.id.Coordinatelayout);
        list =new ArrayList<>();
        String userid = getSharedPreferences("login_details",MODE_PRIVATE)
                .getString("user_id",null);
        Log.d(TAG, "onCreate: user id is"+userid);


        progressDialog =new ProgressDialog(ProductsActivity.this);
        recyclerView =findViewById(R.id.commonrecycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));
        sub_cat_title = getIntent().getStringExtra("sub_cat_title");
        cat_id =getIntent().getIntExtra("cat_id",0);
        parent_id = getIntent().getIntExtra("subcat_id",0);
        Log.d(TAG, "onCreate: "+sub_cat_title+cat_id+parent_id);
        if (sub_cat_title !=null && cat_id !=0){
            getSupportActionBar().setTitle(sub_cat_title);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchandlogout,menu);

        MenuItem menuItem =menu.findItem(R.id.search_menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<ProductsModel> arrayList =new ArrayList<>();
                for (ProductsModel model:list){
                    if (model.getProduct_title().toLowerCase().contains(newText.toLowerCase())){
                        arrayList.add(model);
                    }
                }
                adapter.FilterList(arrayList);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_menu){

        }
        if (item.getItemId() == R.id.logout_menu){
            new Constant().ShowLogoutDialog(ProductsActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
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
                                adapter = new ProductsAdapter(ProductsActivity.this,list,coordinatorLayout);
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
                map.put("products", "set");
                map.put("cat_id", String.valueOf(cat_id));
                map.put("parent_id", String.valueOf(parent_id));
                return map;
            }
        };
        MySingleton.getInstance(ProductsActivity.this).addToRequestQuee(stringRequest);
    }
}
