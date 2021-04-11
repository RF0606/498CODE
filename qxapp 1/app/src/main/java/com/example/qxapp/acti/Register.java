package com.example.qxapp.acti;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.R;
import com.example.qxapp.user.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Register extends AppCompatActivity {

    private EditText userName, passWord,nickName;
    private Button register;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);
        userName = findViewById(R.id.userName);
        passWord = findViewById(R.id.userPassword);
        nickName = findViewById(R.id.userNickName);
        register = findViewById(R.id.button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = new User();
                user.setUsername(userName.getText().toString().trim());
                user.setPassword(passWord.getText().toString().trim());
                user.setNickname(nickName.getText().toString().trim());

                //user enter account, password and nickname or not
                if (userName.getText().toString().equals("")){
                    Toast.makeText(Register.this,"Please enter account",Toast.LENGTH_SHORT).show();
                }else if (passWord.getText().toString().equals("")){
                    Toast.makeText(Register.this,"Please enter password",Toast.LENGTH_SHORT).show();
                }else if (nickName.getText().toString().equals("")){
                    Toast.makeText(Register.this,"Please enter nickName",Toast.LENGTH_SHORT).show();
                }else {
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                         if (e==null){
                             Toast.makeText(Register.this,"Register Successful",Toast.LENGTH_SHORT).show();
                            finish();
                         }else
                             Toast.makeText(Register.this,"Register Fail",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
