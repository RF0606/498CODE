package com.example.qxapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.R;
import com.example.qxapp.acti.Login;
import com.example.qxapp.acti.Receive;
import com.example.qxapp.user.Post;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Post> data;

    private final int N_TYPE = 0;
    private final int F_TYPE = 1;

    //the maximum number in one page
    private int max_num = 8;
    private boolean isFootView = true;

    public HomePageAdapter(Context mContext, List<Post> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ord,parent,false);
        View footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
        if (viewType == F_TYPE){
            return new RecyclerViewHolder(footView,F_TYPE);
        } else{
            return new RecyclerViewHolder(view,N_TYPE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(isFootView && (getItemViewType(position))==F_TYPE){
                //text when loading at bottom
                final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
                recyclerViewHolder.Loading.setText("Loading now......");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        max_num += 8;
                        notifyDataSetChanged();
                    }
                },2000);
            }else {
                //text shows
                final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
                Post post = data.get(position);
                recyclerViewHolder.userName.setText(post.getName());
                recyclerViewHolder.nickName.setText(post.getNickName());
                recyclerViewHolder.content.setText(post.getContent());
                recyclerViewHolder.time.setText(post.getCreatedAt());

                recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = recyclerViewHolder.getAdapterPosition();

                        if(BmobUser.getCurrentUser(BmobUser.class)!=null){
                            Intent in = new Intent(mContext, Receive.class);
                            in.putExtra("r_userName",data.get(position).getName());
                            in.putExtra("r_time",data.get(position).getCreatedAt());
                            in.putExtra("r_content",data.get(position).getContent());
                            in.putExtra("id",data.get(position).getObjectId());
                            mContext.startActivity(in);
                        }else{
                            Toast.makeText(mContext,"please login",Toast.LENGTH_SHORT).show();
                            mContext.startActivity(new Intent(mContext, Login.class));
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
            return N_TYPE;}
    }

    @Override
    public int getItemCount() {
        if(data.size() < max_num){
            return data.size();
        }
        return max_num;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView nickName, content, time, userName;
        public TextView Loading;

        public RecyclerViewHolder(View footView, int f_type) {
            super(footView);
            if(f_type == N_TYPE){
                userName = footView.findViewById(R.id.username);
                nickName = footView.findViewById(R.id.nickname);
                content = footView.findViewById(R.id.content);
                time = footView.findViewById(R.id.time);
            }
            else if (f_type == F_TYPE){
                Loading = footView.findViewById(R.id.footText);
            }
        }
    }
}
