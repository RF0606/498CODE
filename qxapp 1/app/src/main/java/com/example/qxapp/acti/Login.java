package com.example.qxapp.acti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.MainActivity;
import com.example.qxapp.R;
import com.example.qxapp.user.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Login extends AppCompatActivity {
    private EditText userName, passWord;
    private Button register, logIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get username and password
        userName = findViewById(R.id.userName);
        passWord = findViewById(R.id.userPassword);
        register = findViewById(R.id.register);
        logIn = findViewById(R.id.button);

        //register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        //login
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get tpyed username and password
                final User user = new User();
                user.setUsername(userName.getText().toString().trim());
                user.setPassword(passWord.getText().toString().trim());
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User o, BmobException e) {
                        //justice account exist or not ，if e == null means no error，login success，otherwise login fail
                        if (e == null) {
                            Toast.makeText(Login.this,"login successful"+user.getUsername(),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                        }else{
                            Toast.makeText(Login.this,"login fail"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}