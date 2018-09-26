package sonu.kumar.jaquar.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.OrderNowModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

/**
 * Created by sonu on 4/9/18.
 */

public class OrderNowAdapter extends RecyclerView.Adapter<OrderNowAdapter.ViewHolder> {
    Context context;
    List<OrderNowModel> list;
    int result1,result,quantity1,quantity;
    TextView totalprice;
    public static final String TAG ="OrdereNowAdapter";

    public OrderNowAdapter(Context context, List<OrderNowModel> list,TextView totalprice) {
        this.context = context;
        this.list = list;
        this.totalprice =totalprice;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_order_now_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderNowModel model = list.get(position);
        Glide.with(context).load(model.getProduct_image()).into(holder.cart_image);
        holder.cart_title.setText(model.getProduct_name());
        holder.cart_price.setText(String.valueOf(model.getProduct_total_price()));
        holder.cart_productcode.setText(model.getProduct_code());
        holder.cart_quantity.setText(String.valueOf(model.getProduct_quantity()));
        holder.getData(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void FilterList(ArrayList<OrderNowModel> arrayList) {
        list =arrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button increase,decrease,remove;
        ImageView cart_image;
        TextView cart_title,cart_price,cart_productcode,cart_quantity;
        public ViewHolder(View itemView) {
            super(itemView);
            cart_quantity =itemView.findViewById(R.id.quantity);
            increase =itemView.findViewById(R.id.increaseItem);
            decrease =itemView.findViewById(R.id.decreaseItem);
            remove =itemView.findViewById(R.id.checkoutDeleteButton);
            cart_image =itemView.findViewById(R.id.checkoutImage);
            cart_title =itemView.findViewById(R.id.checkoutTitle);
            cart_price =itemView.findViewById(R.id.checkoutRsValue);
            cart_productcode =itemView.findViewById(R.id.checkoutProductcode);

        }

        public void getData(final int position) {
            OrderNowModel modal = list.get(position);
            final int user_id = modal.getUser_id();
            final int product_id =modal.getProduct_id();
            final int singleproductprice = modal.getProduct_single_price();
            increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                      quantity1 = Integer.parseInt(cart_quantity.getText().toString());
                   int price1 = Integer.parseInt(cart_price.getText().toString());

                    if (quantity1==5){
                        Toast.makeText(context, "Maximum 5 items are allowed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int total_price = Integer.parseInt(totalprice.getText().toString());
                        int r = total_price+singleproductprice;
                        totalprice.setText(String.valueOf(r));
                        quantity1 =quantity1+1;
                         result1 =singleproductprice+price1;
                        cart_price.setText(String.valueOf(result1));
                        cart_quantity.setText(String.valueOf(quantity1));
                        StringRequest stringRequest =new StringRequest(
                                StringRequest.Method.POST,
                                UrlConstants.ALL_REQUEST,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "onResponse:increase click "+response);

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
                                map.put("UpdateItem","yes");
                                map.put("user_id", String.valueOf(user_id));
                                map.put("product_id", String.valueOf(product_id));
                                map.put("product_quantity", String.valueOf(quantity1));
                                map.put("total_price", String.valueOf(result1));
                                return map;
                            }
                        };
                        MySingleton.getInstance(context).addToRequestQuee(stringRequest);
                    }


                }
            });
            decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int price = Integer.parseInt(cart_price.getText().toString());


                     quantity = Integer.parseInt(cart_quantity.getText().toString());
                    if (quantity == 1){
                        Toast.makeText(context, "At least one product must be there", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int total_price = Integer.parseInt(totalprice.getText().toString());
                        int r = total_price-singleproductprice;
                        totalprice.setText(String.valueOf(r));
                         result =price -singleproductprice;
                        quantity =quantity -1;
                        cart_price.setText(String.valueOf(result));
                        cart_quantity.setText(String.valueOf(quantity));
                        StringRequest stringRequest =new StringRequest(
                                StringRequest.Method.POST,
                                UrlConstants.ALL_REQUEST,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "onResponse:increase click "+response);

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
                                map.put("UpdateItem","yes");
                                map.put("user_id", String.valueOf(user_id));
                                map.put("product_id", String.valueOf(product_id));
                                map.put("product_quantity", String.valueOf(quantity));
                                map.put("total_price", String.valueOf(result));
                                return map;
                            }
                        };
                        MySingleton.getInstance(context).addToRequestQuee(stringRequest);
                    }

                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder  builder = new AlertDialog.Builder(context);
                    builder.setTitle("Remove");
                    builder.setMessage("Do you want to Remove ? ");
                    builder.setIcon(R.drawable.ic_delete_black_24dp);

                    builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            StringRequest stringRequest =new StringRequest(
                                    StringRequest.Method.POST,
                                    UrlConstants.ALL_REQUEST,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, "onResponse:delete click "+response);
                                            if (response.equals("1")){
                                                list.remove(position);
                                                notifyItemRemoved(position);
                                                int price = Integer.parseInt(cart_price.getText().toString());
                                                int tp = Integer.parseInt(totalprice.getText().toString());
                                                int res =tp-price;
                                                totalprice.setText(String.valueOf(res));

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
                                    map.put("RemoveFromCart","yes");
                                    map.put("user_id", String.valueOf(user_id));
                                    map.put("product_id", String.valueOf(product_id));
                                    return map;
                                }
                            };
                            MySingleton.getInstance(context).addToRequestQuee(stringRequest);

                        }
                        });
                           builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        });
                           builder.create();
                           builder.show();


                }
            });
        }
    }
}
