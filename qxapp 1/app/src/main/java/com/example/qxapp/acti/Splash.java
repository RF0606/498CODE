package com.example.qxapp.acti;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.MainActivity;
import com.example.qxapp.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //configure and initial bmob cloud server，connect the app with bomb's account's app key;
        Bmob.initialize(this,"fcacd333fe03d60c35ea5c10f7441a60");

        //create splash screen and delay time
        Timer timer = new Timer();
        timer.schedule(timeTask, 2000);
    }
    //define timeTask
    TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {

            //check user login or not, go to the MainActivity.class if user login, go to the login.class if user didn't login
            //or write startActivity(new Intent(Splash.this,MainActivity.class));
            BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
            if(bmobUser != null){
            Intent intent;
            intent = new Intent(Splash.this,MainActivity.class);
            startActivity(intent);
            }else{
                Intent intent;
                intent = new Intent(Splash.this, Login.class);
                startActivity(intent);
            }
        }
    };
}
