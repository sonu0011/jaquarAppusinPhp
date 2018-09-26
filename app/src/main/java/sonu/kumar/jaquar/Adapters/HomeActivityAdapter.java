package sonu.kumar.jaquar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sonu.kumar.jaquar.Activity.AddToCartActivity;
import sonu.kumar.jaquar.Constants.Constant;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.ProductsModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sonu on 5/9/18.
 */

public class HomeActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ProductsModel> list;
    Context context;
    CoordinatorLayout coordinatorLayout;
    public static final String TAG="HomeAdapter";

    public HomeActivityAdapter(List<ProductsModel> list, Context context,CoordinatorLayout coordinatorLayout) {
        this.list = list;
        this.context = context;
        this.coordinatorLayout =coordinatorLayout;
        Log.d(TAG, "HomeActivityAdapter: constructor ");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == R.layout.custom_view_pager){
            Log.d(TAG, "onCreateViewHolder: view type  viewpager");

            View view = LayoutInflater.from(context).inflate(R.layout.custom_view_pager,parent,false);
           ViewPagerHolder viewPagerHolder =new ViewPagerHolder(view);
            return viewPagerHolder;
        }
        else {
            Log.d(TAG, "onCreateViewHolder: view type custom");
            View view = LayoutInflater.from(context).inflate(R.layout.custom_products_layout,parent,false);
            ProductHolder sliderViewHolder =new ProductHolder(view);
            return sliderViewHolder;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductHolder){

            Log.d(TAG, "onBindViewHolder: instance of product holder");
            final ProductsModel model = list.get(position);
            Glide.with(context).load(model.getProduct_image()).into(((ProductHolder) holder).product_image);
            ((ProductHolder) holder).product_price.setText(model.getProduct_price()+".00");
            ((ProductHolder) holder).product_title.setText(model.getProduct_title());
            ((ProductHolder) holder).product_code.setText(model.getProduct_code());
            StringRequest stringRequest =new StringRequest(StringRequest.Method.POST,
                    UrlConstants.ALL_REQUEST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: whishlist"+response);
                            if (response.equals("1")){
                                Log.d(TAG, "onResponse: equals1");
                                int color = Color.parseColor("#FF0000");
                                ((ProductHolder) holder).product_fav_icon.setColorFilter(color);
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
                    map.put("checkwhishlist","yes");
                    map.put("user_id","1");
                    map.put("product_id", String.valueOf(model.getProduct_id()));
                    return map;
                }
            };
            MySingleton.getInstance(context).addToRequestQuee(stringRequest);

            ((ProductHolder) holder).getData(position);
        }
        if (holder instanceof ViewPagerHolder){

        }

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+list.size());
        return list.size();
    }
    public int getItemViewType(int position) {
//        if(position==0)
//        {
//            return R.layout.custom_view_pager;
//        }
//        else {
            return R.layout.custom_products_layout;

    }
    private class ViewPagerHolder extends RecyclerView.ViewHolder{

        public ViewPagerHolder(View itemView) {
            super(itemView);



        }
    }
    private class ProductHolder extends RecyclerView.ViewHolder{
        ImageView product_image,product_fav_icon;
        TextView product_title,product_code,product_price;
        public ProductHolder(View itemView) {
            super(itemView);
            product_image =itemView.findViewById(R.id.singleProductImage);
            product_code =itemView.findViewById(R.id.singleProductProductcode);
            product_fav_icon =itemView.findViewById(R.id.whishlistimage);
            product_title =itemView.findViewById(R.id.singleProductTitle);
            product_price =itemView.findViewById(R.id.singleProductPrice);

        }

        public void getData(int position) {
            ProductsModel model = list.get(position);
            final int cat_id = model.getCat_id();
            final int subcat_id =model.getSubcat_id();
            final int product_id =model.getProduct_id();
            final String u_id = context.getSharedPreferences("login_details", MODE_PRIVATE).getString("userid",null);

            product_fav_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: user id is"+ Constant.USER_ID+"product id is "+product_id);
                    StringRequest stringRequest =new StringRequest(
                            StringRequest.Method.POST,
                            UrlConstants.ALL_REQUEST,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "onResponse: ");
                                    Log.d(TAG, "onResponse: "+response);
                                    if (response.equals("1")){
                                        StringRequest stringRequest1 =new StringRequest(
                                                StringRequest.Method.POST,
                                                UrlConstants.ALL_REQUEST,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.d(TAG, "onResponse: alreadywhishlist"+response);
                                                        if (response.equals("1")){
                                                            int color = Color.parseColor("#39000000");
                                                            product_fav_icon.setColorFilter(color);
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
                                                String user_id = context.getSharedPreferences("user_id",MODE_PRIVATE)
                                                        .getString("user_id",null);
                                                Log.d(TAG, "onResponse: userid "+user_id);
                                                map.put("removefromwhishlist","yes");
                                                map.put("user_id",user_id);
                                                map.put("product_id", String.valueOf(product_id));
                                                return map;
                                            }
                                        };
                                        MySingleton.getInstance(context).addToRequestQuee(stringRequest1);

                                    }
                                    if (response.equals("0")){
                                        int color = Color.parseColor("#FF0000");
                                        product_fav_icon.setColorFilter(color);

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
                            String user_id = context.getSharedPreferences("user_id",MODE_PRIVATE)
                                    .getString("user_id",null);
                            Log.d(TAG, "onCreate: userid "+user_id);
                            map.put("whishlist","yes");
                            map.put("product_id", String.valueOf(product_id));
                            map.put("user_id", user_id);
                            return map;
                        }
                    };
                    MySingleton.getInstance(context).addToRequestQuee(stringRequest);


                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(context,AddToCartActivity.class);
                    intent.putExtra("cat_id",cat_id);
                    intent.putExtra("subcat_id",subcat_id);
                    intent.putExtra("product_id",product_id);
                    context.startActivity(intent);                }
            });

        }
    }

}
