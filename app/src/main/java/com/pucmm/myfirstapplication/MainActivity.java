package com.pucmm.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mCancel;
    private TextView mContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCancel = findViewById(R.id.btn_cancel);
        mContinue = findViewById(R.id.btn_continue);

        mCancel.setOnClickListener(this::handleCancel);
        mContinue.setOnClickListener(this::handleContinue);
    }

    public void handleContinue (View v) {
        Intent intent = new Intent(getBaseContext(), PermissionsActivity.class);
        startActivity(intent);
    }

    public void handleCancel (View v) {
        finish();
        System.exit(0);
    }
}
