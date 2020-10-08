package com.pucmm.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mCancel;
    private TextView mContinue;
    private Switch mStorage;
    private Switch mLocation;
    private Switch mCamera;
    private Switch mPhone;
    private Switch mContact;

    public static final int PERMISSION_CODE = 100;

    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PHONE = Manifest.permission.CALL_PHONE;
    public static final String CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String NO_PERMISSIONS = "NO_PERMISSIONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCancel = findViewById(R.id.btn_cancel);
        mContinue = findViewById(R.id.btn_continue);

        mStorage = findViewById(R.id.switch_storage);
        mLocation = findViewById(R.id.switch_location);
        mCamera = findViewById(R.id.switch_camera);
        mPhone = findViewById(R.id.switch_phone);
        mContact = findViewById(R.id.switch_contact);

        mContinue.setOnClickListener(this::handleContinue);
        mCancel.setOnClickListener(v -> { finish(); System.exit(0); });

        turnOnSwitchesWithPermission();
        listenForSwitchesChange();
    }

    public boolean permissionGrantedFor(String permission) {
        return ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void turnOnSwitchesWithPermission() {
        if (permissionGrantedFor(READ_STORAGE)) mStorage.setChecked(true);
        if (permissionGrantedFor(CAMERA)) mCamera.setChecked(true);
        if (permissionGrantedFor(PHONE)) mPhone.setChecked(true);
        if (permissionGrantedFor(CONTACTS)) mContact.setChecked(true);
        if (permissionGrantedFor(LOCATION)) mLocation.setChecked(true);
    }

    public void listenForSwitchesChange() {
        mStorage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked || permissionGrantedFor(READ_STORAGE)) mStorage.setChecked(true);
        });

        mCamera.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked || permissionGrantedFor(CAMERA)) mCamera.setChecked(true);
        });

        mPhone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked || permissionGrantedFor(PHONE)) mPhone.setChecked(true);
        });

        mContact.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked || permissionGrantedFor(CONTACTS)) mContact.setChecked(true);
        });

        mLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked || permissionGrantedFor(LOCATION)) mLocation.setChecked(true);
        });
    }

    public void handleContinue (View v) {
        //if no switches are on. Go to the next intent
        boolean noSwitchesAreOn = !mStorage.isChecked() && !mLocation.isChecked() && !mCamera.isChecked() && !mPhone.isChecked() && !mContact.isChecked();
        if (noSwitchesAreOn) {
            Intent intent = new Intent(getBaseContext(), PermissionsActivity.class);
            startActivity(intent);
            return;
        }

        List<String> permissions = new ArrayList<>();

        if (mStorage.isChecked() && !permissionGrantedFor(READ_STORAGE)) permissions.add(READ_STORAGE);
        if (mCamera.isChecked() && !permissionGrantedFor(CAMERA)) permissions.add(CAMERA);
        if (mPhone.isChecked() && !permissionGrantedFor(PHONE)) permissions.add(PHONE);
        if (mContact.isChecked() && !permissionGrantedFor(CONTACTS)) permissions.add(CONTACTS);
        if (mLocation.isChecked() && !permissionGrantedFor(LOCATION)) permissions.add(LOCATION);

        ActivityCompat.requestPermissions(this,permissions.toArray(new String[0]), PERMISSION_CODE);
    }

    // This function is called when the user accepts or declines the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent(getBaseContext(), PermissionsActivity.class);
        startActivity(intent);
    }

}
