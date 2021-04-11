package com.example.qxapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qxapp.R;
import com.example.qxapp.acti.Login;
import com.example.qxapp.user.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class fragmentUs extends Fragment {
    private TextView us_userName, us_nickName;
    private Button us_logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentus,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        //加载个人信息
        getMyInfo();

        us_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
            }
        });

    }

    private void getMyInfo() {

        User bmobUser = User.getCurrentUser(User.class);
//        us_userName.setText(bmobUser.getUsername());
//        us_nickName.setText(bmobUser.getNickname());
        String id = bmobUser.getObjectId();
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(id, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    us_userName.setText(user.getUsername());
                    us_nickName.setText(user.getNickname());
                } else
                    Toast.makeText(getActivity(), "error: " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        us_userName = getActivity().findViewById(R.id.us_userName);
        us_nickName = getActivity().findViewById(R.id.us_nickName);
        us_logout = getActivity().findViewById(R.id.us_logout);

    }
}
