package sonu.kumar.jaquar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import sonu.kumar.jaquar.Adapters.OrderNowAdapter;
import sonu.kumar.jaquar.Constants.Constant;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.OrderNowModel;
import sonu.kumar.jaquar.Models.ProductsModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

public class OrderNowActivity extends AppCompatActivity {

     ProgressDialog progressDialog;
    OrderNowAdapter adapter;
    List<OrderNowModel> list;
     RecyclerView recyclerView;
    String user_id;
    Button continueshopping;
    
    ImageView imageView;
    RelativeLayout relativeLayout;
    TextView totalprice;
    LinearLayout linearLayout;

    public static final String TAG="OrderNowActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);
        list =new ArrayList<>();
        linearLayout =findViewById(R.id.linearlayout);
        continueshopping =findViewById(R.id.showbuttoncartempty);
        imageView  =findViewById(R.id.showimagecartempty);
        relativeLayout =findViewById(R.id.ordernowlayout);
        progressDialog =new ProgressDialog(OrderNowActivity.this);
        recyclerView =findViewById(R.id.ordermowrecycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderNowActivity.this));
        getSupportActionBar().setTitle("Proceed to checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user_id= getSharedPreferences("user_id",MODE_PRIVATE)
                .getString("user_id",null);
        totalprice =findViewById(R.id.totalPriceValue);
        StringRequest stringRequest =new StringRequest(
                StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse:totalprice "+response);
                        if (response !=null){
                            if (response.equals("0")){
                                recyclerView.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                continueshopping.setVisibility(View.VISIBLE);
                                relativeLayout.setVisibility(View.GONE);
                                linearLayout.setBackgroundColor(Color.WHITE);
                            }
                            else {
                                totalprice.setText(response);

                            }
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
                map.put("TotalPrice","yes");
                map.put("user_id", String.valueOf(user_id));
                return map;
            }
        };
        MySingleton.getInstance(OrderNowActivity.this).addToRequestQuee(stringRequest);

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
                ArrayList<OrderNowModel> arrayList =new ArrayList<>();
                for (OrderNowModel model:list){
                    if (model.getProduct_name().toLowerCase().contains(newText.toLowerCase())){
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
            new Constant().ShowLogoutDialog(OrderNowActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest =new StringRequest(
                StringRequest.Method.POST,
                UrlConstants.ALL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                                JSONArray jsonArray = new JSONArray(response);
                                list.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    list.add(new OrderNowModel(
                                            jsonObject.getInt("user_id"),
                                            jsonObject.getInt("product_id"),
                                            jsonObject.getInt("product_single_price"),
                                            jsonObject.getInt("product_quantity"),
                                            jsonObject.getInt("product_total_price"),
                                            jsonObject.getString("product_image"),
                                            jsonObject.getString("product_name"),
                                            jsonObject.getString("product_code")


                                    ));
                            }
                            Log.d(TAG, "onResponse: "+list.size());
                            adapter =new OrderNowAdapter(OrderNowActivity.this,list,totalprice);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                map.put("getcartProduct","yes");
                map.put("user_id",user_id);
                return map;
            }
        };
        MySingleton.getInstance(OrderNowActivity.this).addToRequestQuee(stringRequest);
        progressDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (recyclerView.getChildCount() ==0){
            Log.d(TAG, "onResume: ");
            totalprice.setText("0");
        }
    }

    public void GoToHome(View view) {
        startActivity(new Intent(OrderNowActivity.this,HomeActivity.class));
    }
}
