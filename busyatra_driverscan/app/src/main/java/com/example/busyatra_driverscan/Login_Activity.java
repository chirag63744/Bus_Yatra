package com.example.busyatra_driverscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login_Activity extends AppCompatActivity {
    Button bt, bt2;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);
        bt = (Button) findViewById(R.id.login_button);
        et = (EditText) findViewById(R.id.phone_num_et);
        bt2 = (Button) findViewById(R.id.sign_up_by);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Login_Activity.this, OTP_Activity.class);
                    i.putExtra("number", et.getText().toString());
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(Login_Activity.this, "Next Intent Problem", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Intent i = new Intent(Login_activity.this, Sign_up_Activity.class);
                    //startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(Login_Activity.this, "Next Intent Problem - Sign up", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}