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

import sonu.kumar.jaquar.Activity.ProductsActivity;
import sonu.kumar.jaquar.Activity.SubCategoryActivity;
import sonu.kumar.jaquar.Models.ShopBYCategoryModel;
import sonu.kumar.jaquar.Models.SubCategoryModel;
import sonu.kumar.jaquar.R;

/**
 * Created by sonu on 2/9/18.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {
    List<SubCategoryModel> list;
    Context context;
    int cat_id;

    public SubCategoryAdapter(List<SubCategoryModel> list, Context context,int cat_id) {
        this.list = list;
        this.context = context;
        this.cat_id =cat_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_sub_category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubCategoryModel model = list.get(position);
        Glide.with(context).load(model.getImage()).into(holder.sub_image);
        holder.sub_title.setText(model.getSubcat_name());
        holder.getData(position);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void FilterList(ArrayList<SubCategoryModel> arrayList) {
        list =arrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sub_image;
        TextView sub_title;
        public ViewHolder(View itemView) {
            super(itemView);
            sub_image =itemView.findViewById(R.id.subcategoryImage);
            sub_title =itemView.findViewById(R.id.subcategoryTitle);
        }

        public void getData(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SubCategoryModel model =list.get(position);
                    int id = model.getSubcat_id();
                    String title =model.getSubcat_name();
                    Intent intent =new Intent(context, ProductsActivity.class);
                    intent.putExtra("sub_cat_title",title);
                    intent.putExtra("subcat_id",id);
                    intent.putExtra("cat_id",cat_id);
                    context.startActivity(intent);
                }
            });
        }

    }
}
