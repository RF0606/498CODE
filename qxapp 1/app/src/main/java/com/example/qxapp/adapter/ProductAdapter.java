package com.example.qxapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.R;
import com.example.qxapp.acti.Login;
import com.example.qxapp.acti.ProductInfo;
import com.example.qxapp.user.Product;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Product> products;
    private final int N_TYPE = 0;
    private final int F_TYPE = 1;
    private int max_num = 8;
    private boolean isFootView = true;

    public ProductAdapter(Context context, List<Product> products){
        this.context = context;
        this.products = products;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        View footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
        if (viewType == F_TYPE){
            return new ProductAdapter.RecyclerViewHolder(footView,F_TYPE);}
        else
            return  new ProductAdapter.RecyclerViewHolder(view,N_TYPE);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(isFootView && getItemViewType(position)==F_TYPE){
            final ProductAdapter.RecyclerViewHolder recyclerViewHolder = (ProductAdapter.RecyclerViewHolder) holder;
            recyclerViewHolder.Loading.setText("loading");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    max_num += 8;
                    notifyDataSetChanged();
                }
            },2000);
        }
        else {
            final ProductAdapter.RecyclerViewHolder recyclerViewHolder = (ProductAdapter.RecyclerViewHolder) holder;
            Product product = products.get(position);
            recyclerViewHolder.productName.setText(product.getName());
            recyclerViewHolder.productType.setText(product.getInfo());
            recyclerViewHolder.updateDate.setText(product.getUpdatedAt());
            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerViewHolder.getAdapterPosition();
                    if(BmobUser.getCurrentUser(BmobUser.class)!=null){
                        Intent in = new Intent(context, ProductInfo.class);
                        in.putExtra("productName",products.get(position).getName());
                        in.putExtra("productUpdate",products.get(position).getUpdatedAt());
                        in.putExtra("productInfo",products.get(position).getInfo());
                        in.putExtra("id",products.get(position).getObjectId());
                        context.startActivity(in);
                    }else{
                        Toast.makeText(context,"please login",Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, Login.class));
                    }
                }
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(position == max_num-1){
            return F_TYPE;
        }else{
            return  N_TYPE;}
    }

    @Override
    public int getItemCount() {
        if(products.size() < max_num){
            return products.size();
        }
        return max_num;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productType, updateDate;
        public ImageView productImage;
        public TextView Loading;
        public RecyclerViewHolder(View footView, int f_type) {
            super(footView);
            if(f_type == N_TYPE){
                productName = footView.findViewById(R.id.productname);
                productType = footView.findViewById(R.id.model);
                updateDate = footView.findViewById(R.id.updatedate);
                productImage = footView.findViewById(R.id.productimage);
            }
            else if (f_type == F_TYPE){
                Loading = footView.findViewById(R.id.footText);
            }
        }
    }
}
