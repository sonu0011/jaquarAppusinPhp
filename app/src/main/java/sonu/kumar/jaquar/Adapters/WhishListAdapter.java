package sonu.kumar.jaquar.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sonu.kumar.jaquar.Activity.AddToCartActivity;
import sonu.kumar.jaquar.Constants.UrlConstants;
import sonu.kumar.jaquar.Models.ProductsModel;
import sonu.kumar.jaquar.R;
import sonu.kumar.jaquar.Singleton.MySingleton;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sonu on 4/9/18.
 */

public class WhishListAdapter extends RecyclerView.Adapter<WhishListAdapter.ViewHolder> {
    List<ProductsModel> list;
    Context context;
    public static final String TAG ="WhishlistAdapter";

    public WhishListAdapter(List<ProductsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_whishlist_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductsModel model = list.get(position);
        Glide.with(context).load(model.getProduct_image()).into(holder.product_image);
        holder.product_code.setText(model.getProduct_code());
        holder.product_price.setText(String.valueOf(model.getProduct_price()));
        holder.product_title.setText(model.getProduct_title());
        holder.getData(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void FilterList(ArrayList<ProductsModel> arrayList) {
        list =arrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView product_title, product_code, product_price;
        Button delete;
        View itemviewclick;

        public ViewHolder(View itemView1) {
            super(itemView1);
            itemviewclick =itemView1;

            product_image = itemView1.findViewById(R.id.newlyImageview);
            product_code = itemView1.findViewById(R.id.newlyProductCode);
            product_title = itemView1.findViewById(R.id.newlytitel);
            product_price = itemView1.findViewById(R.id.newlyProductPrice);
            delete = itemView1.findViewById(R.id.newlyDeletefav);


        }

        public void getData(final int position) {
            ProductsModel model = list.get(position);
            final int cat_id = model.getCat_id();
            final int subcat_id = model.getSubcat_id();
            final int product_id = model.getProduct_id();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddToCartActivity.class);
                            intent.putExtra("cat_id", cat_id);
                            intent.putExtra("subcat_id", subcat_id);
                            intent.putExtra("product_id", product_id);
                            context.startActivity(intent);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder  builder = new AlertDialog.Builder(context,R.style.Dialog);

                    builder.setTitle("Delete");
                    builder.setMessage("Do you want to Delete ? ");
                    builder.setIcon(R.drawable.deletewhite);

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            StringRequest stringRequest1 =new StringRequest(
                                    StringRequest.Method.POST,
                                    UrlConstants.ALL_REQUEST,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, "onResponse: alreadywhishlist"+response);
                                            if (response.equals("1")){
                                                list.remove(position);
                                                notifyItemRemoved(position);
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
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
                    builder.create();
                    builder.show();

                }
            });
        }
    }
}
