package com.example.user.smartpocket;

import android.app.Activity;
import android.os.Bundle;

public class Bills extends Activity {
    String Login = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        Login = getIntent().getStringExtra("Name");
    }
}
