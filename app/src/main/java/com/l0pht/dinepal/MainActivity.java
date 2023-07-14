package com.l0pht.dinepal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //keep the MainActivity file clean by moving the code to DashBoard.java

        Intent dashBoardIntent = new Intent(MainActivity.this, DashBoard.class);
        startActivity(dashBoardIntent);
        finish();
    }
}