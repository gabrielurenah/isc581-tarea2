package com.pucmm.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class PermissionsActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mStorage;
    private TextView mLocation;
    private TextView mCamera;
    private TextView mPhone;
    private TextView mContact;
    private View mParentLayout;

    private static Map<String, Consumer<String>> MAP = new HashMap<>();
    {
        MAP.put(MainActivity.NO_PERMISSIONS, (s) -> { backHome(); });
        MAP.put(MainActivity.CAMERA, (s) -> {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, MainActivity.PERMISSION_CODE);
        });
        MAP.put(MainActivity.READ_STORAGE, (s) -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("application/pdf");
            startActivity(i);
        });
        MAP.put(MainActivity.CONTACTS, (s) -> {
            Intent i = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
            startActivity(i);
        });
        MAP.put(MainActivity.PHONE, (s) -> {
            // use ACTION_CALL to call that number directly
            Intent phoneCallIntent = new Intent(Intent.ACTION_VIEW);
            phoneCallIntent.setData(Uri.parse("tel:18095555555"));
            startActivity(phoneCallIntent);
        });
        MAP.put(MainActivity.LOCATION, (s) -> {
           Uri uri = Uri.parse("geo:0.0?q=restaurants");
           Intent i = new Intent(Intent.ACTION_VIEW, uri);
           i.setPackage("com.google.android.apps.maps");
           startActivity(i);
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mStorage = findViewById(R.id.action_storage);
        mLocation = findViewById(R.id.action_location);
        mCamera = findViewById(R.id.action_camera);
        mPhone = findViewById(R.id.action_phone);
        mContact = findViewById(R.id.action_contact);
        mParentLayout = findViewById(R.id.actions_layout);

        mCamera.setOnClickListener(this);
        mLocation.setOnClickListener(this);
        mStorage.setOnClickListener(this);
        mPhone.setOnClickListener(this);
        mContact.setOnClickListener(this);
    }


    public boolean permissionGrantedFor(String permission) {
        return ContextCompat.checkSelfPermission(PermissionsActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onClick(View view) {
        if (view == mCamera) openSnackbar(MainActivity.CAMERA);
        if (view == mStorage) openSnackbar(MainActivity.READ_STORAGE);
        if (view == mContact) openSnackbar(MainActivity.CONTACTS);
        if (view == mLocation) openSnackbar(MainActivity.LOCATION);
        if (view == mPhone) openSnackbar(MainActivity.PHONE);
    }

    private void openSnackbar(String permission) {
        if (permissionGrantedFor(permission)) {
            makeSnackbar("Permission already granted", permission);
        } else makeSnackbar("Please request the permission", MainActivity.NO_PERMISSIONS);
    }

    private void makeSnackbar(String string, String key) {
        String btnText = key.equals(MainActivity.NO_PERMISSIONS) ? "BACK" : "OPEN";
        Snackbar snackbar = Snackbar.make(mParentLayout,string,Snackbar.LENGTH_LONG);
        snackbar.setAction(btnText, view -> {
            MAP.get(key).accept("");
        }).setBackgroundTint(ContextCompat.getColor(this,R.color.blackish)).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backHome();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
