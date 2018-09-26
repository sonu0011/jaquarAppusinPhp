package sonu.kumar.jaquar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import sonu.kumar.jaquar.Activity.SubCategoryActivity;
import sonu.kumar.jaquar.Models.ShopBYCategoryModel;
import sonu.kumar.jaquar.R;

/**
 * Created by sonu on 2/9/18.
 */

public class ShopByCategotyAdapter extends RecyclerView.Adapter<ShopByCategotyAdapter.ViewHolder> {
    Context context;
    List<ShopBYCategoryModel> list;

    public ShopByCategotyAdapter(Context context, List<ShopBYCategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ShopByCategotyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShopByCategotyAdapter.ViewHolder(LayoutInflater
        .from(context).inflate(R.layout.custom_show_by_category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShopByCategotyAdapter.ViewHolder holder, int position) {
        ShopBYCategoryModel model = list.get(position);
        Glide.with(context).load(model.getImage()).into(holder.cat_image);
        holder.cat_title.setText(String.valueOf(model.getCat_name()));
        holder.getData(position);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void FilterList(ArrayList<ShopBYCategoryModel> arrayList) {
        list =arrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cat_image;
        TextView cat_title;
        public ViewHolder(View itemView) {
            super(itemView);
            cat_image =itemView.findViewById(R.id.shop_by_category_image);
            cat_title =itemView.findViewById(R.id.shop_by_category_text);
        }
        public void getData(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShopBYCategoryModel model =list.get(position);
                    int id = model.getCat_id();
                    String title =model.getCat_name();
                    Intent intent =new Intent(context, SubCategoryActivity.class);
                    intent.putExtra("cat_title",title);
                    intent.putExtra("cat_id",id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
