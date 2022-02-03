package com.example.dirtsystemec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity {

    private Button buttonStart;
    private Context context;
    private boolean flagStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        context = this;
        buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flagStart){
                    flagStart = true;
                    Intent i= new Intent(context,MainActivity.class);
                    startActivity(i);
                }else{
                    StartActivity.super.onBackPressed();
                }
            }
        });

    }
}