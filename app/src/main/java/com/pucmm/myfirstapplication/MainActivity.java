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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mCancel;
    private TextView mContinue;
    private Switch mStorage;
    private Switch mLocation;
    private Switch mCamera;
    private Switch mPhone;
    private Switch mContact;

    public static final int CAMERA_PERMISSION_CODE = 100;
    public static final int STORAGE_PERMISSION_CODE = 101;
    public static final int PHONE_PERMISSION_CODE = 102;
    public static final int CONTACTS_PERMISSION_CODE = 103;

    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PHONE = Manifest.permission.CALL_PHONE;
    public static final String CONTACTS = Manifest.permission.READ_CONTACTS;

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


    }

    public void handleContinue (View v) {
        //if no switches are on. Go to the next intent
        boolean noSwitchesAreOn = !mStorage.isChecked() && !mLocation.isChecked() && !mCamera.isChecked() && !mPhone.isChecked() && !mContact.isChecked();
        if (noSwitchesAreOn) {
            Intent intent = new Intent(getBaseContext(), PermissionsActivity.class);
            startActivity(intent);
        }

        if (mStorage.isChecked() && !permissionGrantedFor(READ_STORAGE)) {
            checkPermission(READ_STORAGE, STORAGE_PERMISSION_CODE);
        }

        if (mCamera.isChecked() && !permissionGrantedFor(CAMERA)) {
            checkPermission(CAMERA, CAMERA_PERMISSION_CODE);
        }

        if (mPhone.isChecked() && !permissionGrantedFor(PHONE)) {
            checkPermission(PHONE, PHONE_PERMISSION_CODE);
        }

        if (mContact.isChecked() && !permissionGrantedFor(CONTACTS)) {
            checkPermission(CONTACTS, CONTACTS_PERMISSION_CODE);
        }

        if ((mStorage.isChecked() && permissionGrantedFor(READ_STORAGE))
                || (mCamera.isChecked() && permissionGrantedFor(CAMERA))
                || (mPhone.isChecked() && permissionGrantedFor(PHONE))
                || (mContact.isChecked() && permissionGrantedFor(CONTACTS))) {
            Intent intent = new Intent(getBaseContext(), PermissionsActivity.class);
            startActivity(intent);
        }
    }

    // Function to check and request permission
    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (!permissionGrantedFor(permission)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        } else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or declines the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (granted) {
                // Showing the toast message
                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                mCamera.setChecked(false);
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (granted) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                mStorage.setChecked(false);
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHONE_PERMISSION_CODE) {
            if (granted) {
                Toast.makeText(MainActivity.this, "Phone Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                mPhone.setChecked(false);
                Toast.makeText(MainActivity.this, "Phone Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CONTACTS_PERMISSION_CODE) {
            if (granted) {
                Toast.makeText(MainActivity.this, "Contacts Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                mCamera.setChecked(false);
                Toast.makeText(MainActivity.this, "Contacts Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        Intent intent = new Intent(getBaseContext(), PermissionsActivity.class);
        startActivity(intent);
    }

}
