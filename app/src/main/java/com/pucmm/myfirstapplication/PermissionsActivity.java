package com.pucmm.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class PermissionsActivity extends AppCompatActivity {

    private TextView mStorage;
    private TextView mLocation;
    private TextView mCamera;
    private TextView mPhone;
    private TextView mContact;
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

        mCamera.setOnClickListener(v -> {
            View parentLayout = findViewById(R.id.actions_layout);
            Snackbar snackbar = Snackbar.make(parentLayout,"This is Simple Snackbar",Snackbar.LENGTH_LONG);
            snackbar.setAction("Open", view -> {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, MainActivity.CAMERA_PERMISSION_CODE);
            }).setBackgroundTint(ContextCompat.getColor(this,R.color.blackish)).show();
        });

        mPhone.setOnClickListener(v -> {
            View parentLayout = findViewById(R.id.actions_layout);
            Snackbar snackbar = Snackbar.make(parentLayout,"This is Simple Snackbar",Snackbar.LENGTH_LONG);
            snackbar.setAction("Open", view -> {
                Intent phoneCallIntent = new Intent(Intent.ACTION_VIEW);
                phoneCallIntent.setData(Uri.parse("tel:18095555555"));
                startActivity(phoneCallIntent);
            }).setBackgroundTint(ContextCompat.getColor(this,R.color.blackish)).show();
        });

        mContact.setOnClickListener(v -> {
            View parentLayout = findViewById(R.id.actions_layout);
            Snackbar snackbar = Snackbar.make(parentLayout,"This is Simple Snackbar",Snackbar.LENGTH_LONG);
            snackbar.setAction("Open", view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
            }).setBackgroundTint(ContextCompat.getColor(this,R.color.blackish)).show();
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
