package com.example.weighstable;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import io.particle.android.sdk.cloud.ParticleCloudSDK;



public class Login extends AppCompatActivity {
    EditText uPassword, uEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enabling android cloud apk for particle

        ParticleCloudSDK.init(this);

        setContentView(R.layout.activity_login);//textView = findViewById(R.id.textView);
        uEmail = (EditText)findViewById(R.id.user_email);
        uPassword = (EditText)findViewById(R.id.user_pass);
    }

    public void onLogin(View view){
        String email = uEmail.getText().toString();
        String password = uPassword.getText().toString();
        String type = "login";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, email, password);

    }


}