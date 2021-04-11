package com.example.qxapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.R;
import com.example.qxapp.adapter.HomePageAdapter;
import com.example.qxapp.adapter.ProductAdapter;
import com.example.qxapp.user.Post;
import com.example.qxapp.user.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class fragmentProduct extends Fragment {
    private ProductAdapter productAdapter;
    private EditText editText;
    private ImageView search;
    private RecyclerView rv;
    private FloatingActionButton add,write,service,set;
    private boolean flag;
    List<Product> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentproduct,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        BmobQuery<Product> pro = new BmobQuery<Product>();
        pro.order("_createAt");
        pro.setLimit(1000);
        pro.findObjects(new FindListener<Product>() {
            @Override
            public void done(List<Product> list, BmobException e) {
                if(e == null){
                data = list;
                productAdapter = new ProductAdapter(getActivity(),data);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv.setAdapter(productAdapter);
                }

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == false){
                write.setVisibility(View.VISIBLE);
                service.setVisibility(View.VISIBLE);
                set.setVisibility(View.VISIBLE);
                flag = true;}
                else if(flag == true){
                    write.setVisibility(View.INVISIBLE);
                    service.setVisibility(View.INVISIBLE);
                    set.setVisibility(View.INVISIBLE);
                    flag = false;
                }
            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getActivity(),Write.class));
            }
        });

    }

    private void initView() {
        editText=getActivity().findViewById(R.id.editText);
        search=getActivity().findViewById(R.id.searchImage);
        rv=getActivity().findViewById(R.id.product_recyclerView);
        add=getActivity().findViewById(R.id.floatingActionButton);
        write=getActivity().findViewById(R.id.FB_write);
        service=getActivity().findViewById(R.id.FB_service);
        set=getActivity().findViewById(R.id.FB_set);
        flag = false;
    }


}
