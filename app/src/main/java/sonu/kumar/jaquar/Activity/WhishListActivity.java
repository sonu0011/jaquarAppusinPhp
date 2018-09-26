package sonu.kumar.jaquar.Activity;

import android.app.ProgressDialog;
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
import android.widget.TextView;

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
import sonu.kumar.jaquar.Adapters.WhishListAdapter;
import sonu.kumar.jaquar.Constants.Constant;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.ProductsModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

public class WhishListActivity extends AppCompatActivity {
    WhishListAdapter adapter;
    List<ProductsModel> list;
    String sub_cat_title;
    int cat_id,parent_id;
    private ProgressDialog progressDialog;
    public static final String TAG ="WhishlistActivity";
    RecyclerView recyclerView;
    private String user_id;
    TextView textView ,textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whish_list);
        list =new ArrayList<>();
        String userid = getSharedPreferences("login_details",MODE_PRIVATE)
                .getString("user_id",null);
        Log.d(TAG, "onCreate: user id is"+userid);


        progressDialog =new ProgressDialog(WhishListActivity.this);
        recyclerView =findViewById(R.id.commonrecycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(WhishListActivity.this));
        Log.d(TAG, "onCreate: "+sub_cat_title+cat_id+parent_id);
       getSupportActionBar().setTitle("Whish list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user_id= getSharedPreferences("user_id",MODE_PRIVATE)
                .getString("user_id",null);


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
            new Constant().ShowLogoutDialog(WhishListActivity.this);
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
                        Log.d(TAG, "onResponse: "+response);
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
                                adapter = new WhishListAdapter(list,WhishListActivity.this);
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
                map.put("WhishlistProducts", "set");
                map.put("user_id",user_id);
                return map;
            }
        };
        MySingleton.getInstance(WhishListActivity.this).addToRequestQuee(stringRequest);
    }
}
