package sonu.kumar.jaquar.Activity;

import android.app.ProgressDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import sonu.kumar.jaquar.Constants.Constant;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.ProductsModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

public class SearchForProductsActivity extends AppCompatActivity {
    ProductsAdapter adapter;
    List<ProductsModel> list;
    EditText editText;
    ImageView search;
    int cat_id,parent_id;
    private ProgressDialog progressDialog;
    public static final String TAG ="SearchForProductsActivity";
    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_products);
        coordinatorLayout =findViewById(R.id.searchCoordinate);
        editText =findViewById(R.id.searchforproductsEditText);
        search =findViewById(R.id.homesearch_btn);
        list =new ArrayList<>();
        String userid = getSharedPreferences("login_details",MODE_PRIVATE)
                .getString("user_id",null);
        progressDialog =new ProgressDialog(SearchForProductsActivity.this);
        recyclerView =findViewById(R.id.searchRecycycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchForProductsActivity.this));
       getSupportActionBar().setTitle("Search for products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


        return super.onSupportNavigateUp();
    }
    @Override
    protected void onStart() {
        super.onStart();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().trim().isEmpty()){
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
                                        if (response.equals("1")) {
                                            if (recyclerView
                                                    .getVisibility() == View.VISIBLE) {
                                                recyclerView.setVisibility(View.INVISIBLE);
                                            }
                                            progressDialog.dismiss();
                                            Snackbar.make(coordinatorLayout, "No result found", Snackbar.LENGTH_SHORT).show();

                                        } else {
                                            if (recyclerView.getVisibility() == View.INVISIBLE) {
                                                recyclerView.setVisibility(View.VISIBLE);
                                            }
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
                                                adapter = new ProductsAdapter(SearchForProductsActivity.this, list, coordinatorLayout);
                                                recyclerView.setAdapter(adapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
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
                            map.put("SearchForProducts", "set");
                            map.put("key",editText.getText().toString());
                            return map;
                        }
                    };
                    MySingleton.getInstance(SearchForProductsActivity.this).addToRequestQuee(stringRequest);
                }
                else {
                    if (recyclerView.getVisibility() ==View.VISIBLE){
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                    Snackbar.make(coordinatorLayout,"Enter some text for search",Snackbar.LENGTH_SHORT).show();

                }
            }
        });

    }
}