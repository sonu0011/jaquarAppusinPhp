package sonu.kumar.jaquar.Activity;

import android.app.ProgressDialog;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

import sonu.kumar.jaquar.Adapters.ShopByCategotyAdapter;
import sonu.kumar.jaquar.Adapters.SubCategoryAdapter;
import sonu.kumar.jaquar.Constants.Constant;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.ShopBYCategoryModel;
import sonu.kumar.jaquar.Models.SubCategoryModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

public class SubCategoryActivity extends AppCompatActivity {
    String cat_title;
    int cat_id;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    public static final String TAG="Subcategory";
    private List<SubCategoryModel> list;
    SubCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        list =new ArrayList<>();
        progressDialog =new ProgressDialog(SubCategoryActivity.this);
        recyclerView =findViewById(R.id.commonrecycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SubCategoryActivity.this));
        cat_title = getIntent().getStringExtra("cat_title");
        cat_id =getIntent().getIntExtra("cat_id",0);
        Log.d(TAG, "onCreate: "+cat_title+cat_id);
        if (cat_title !=null && cat_id !=0){
            getSupportActionBar().setTitle(cat_title);
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
                ArrayList<SubCategoryModel> arrayList =new ArrayList<>();
                for (SubCategoryModel model:list){
                    if (model.getSubcat_name().toLowerCase().contains(newText.toLowerCase())){
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
            new Constant().ShowLogoutDialog(SubCategoryActivity.this);
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
                                    list.add(new SubCategoryModel(
                                            jsonObject.getInt("subcat_id"),
                                            jsonObject.getInt("cat_id"),
                                            jsonObject.getString("subcat_name"),
                                            jsonObject.getString("image")

                                    ));
                                }
                                adapter =new SubCategoryAdapter(list,SubCategoryActivity.this,cat_id);
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
                map.put("sub_category","set");
                map.put("cat_id", String.valueOf(cat_id));
                return map;
            }
        };
        MySingleton.getInstance(SubCategoryActivity.this).addToRequestQuee(stringRequest);
    }

}
