package com.example.qxapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qxapp.R;
import com.example.qxapp.adapter.HomePageAdapter;
import com.example.qxapp.user.Post;
import com.example.qxapp.user.User;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class fragmentHome extends Fragment {
    private RecyclerView rv;
    private SwipeRefreshLayout srLayout;
    private TextView helloHome, bar_userName,hy;

    private List<Post> data;

    private HomePageAdapter homePageAdapter;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmenthome,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //逻辑处理
        initView();

        //初始刷新
        Refresh();

        Bmob.initialize(getActivity(),"fcacd333fe03d60c35ea5c10f7441a60");

        //刷新时刷新图标显示的颜色
        srLayout.setColorSchemeColors(getResources().getColor(R.color.mediumvioletred),getResources().getColor(R.color.mediumseagreen));
        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
            }
        });


        //username loading XXX welcome
        BmobUser bu = BmobUser.getCurrentUser(User.class);
        String userId = bu.getObjectId();
        BmobQuery<User> us = new BmobQuery<>();
        us.getObject(userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    bar_userName.setText(user.getUsername());
                }
                else{
                    bar_userName.setText("");
                    hy.setText("");

                }
            }
        });

    }

    public void Refresh(){
        BmobQuery<Post> po = new BmobQuery<Post>();
        po.order("_createAt");
        po.setLimit(1000);
        po.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                srLayout.setRefreshing(false);
                if(e == null){
                    data = list;
                    homePageAdapter = new HomePageAdapter(getActivity(),data);
                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv.setAdapter(homePageAdapter);
                }else{
                    Toast.makeText(getActivity(),"get data failed"+e,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initView(){
        rv = getActivity().findViewById(R.id.recyclerview);
        srLayout = getActivity().findViewById(R.id.swipe);
        helloHome = getActivity().findViewById(R.id.home);
        bar_userName = getActivity().findViewById(R.id.bar_userName);
        hy = getActivity().findViewById(R.id.hy);

    }
}
