package sonu.kumar.jaquar.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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

import sonu.kumar.jaquar.Adapters.ShopByCategotyAdapter;
import sonu.kumar.jaquar.Constants.Constant;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.ShopBYCategoryModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

public class ShopByCategory extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    ShopBYCategoryModel model;
    List<ShopBYCategoryModel> list;
    ShopByCategotyAdapter adapter;
    public static final String TAG="ShopBYCategory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_by_category);
        list =new ArrayList<>();
        recyclerView = findViewById(R.id.commonrecycleview);
        progressDialog =new ProgressDialog(ShopByCategory.this);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(ShopByCategory.this,
                DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(ShopByCategory.this,
                DividerItemDecoration.VERTICAL));
        getSupportActionBar().setTitle("Shop by category");
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
            ArrayList<ShopBYCategoryModel> arrayList =new ArrayList<>();
            for (ShopBYCategoryModel model:list){
                if (model.getCat_name().toLowerCase().contains(newText.toLowerCase())){
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
            new Constant().ShowLogoutDialog(ShopByCategory.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest =new StringRequest(StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response!=null){
                            Log.d(TAG, "onResponse: "+response);
                            try {
                                JSONArray jsonArray =new JSONArray(response);
                                list.clear();
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject =jsonArray.getJSONObject(i);
                                    list.add(new ShopBYCategoryModel(
                                            jsonObject.getInt("cat_id"),
                                            jsonObject.getString("cat_name"),
                                            jsonObject.getString("image")

                                            ));
                                }
                                adapter =new ShopByCategotyAdapter(ShopByCategory.this,list);
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
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map =new HashMap<>();
                map.put("shop_by_category","set");
                return map;
            }
        };
        MySingleton.getInstance(ShopByCategory.this).addToRequestQuee(stringRequest);
    }
}
