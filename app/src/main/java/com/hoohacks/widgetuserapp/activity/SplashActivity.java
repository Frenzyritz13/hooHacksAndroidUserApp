package com.hoohacks.widgetuserapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hoohacks.widgetuserapp.R;
import com.hoohacks.widgetuserapp.util.NetworkConnectivityHelper;
import com.hoohacks.widgetuserapp.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE};

    final int REQUEST_PERMISSION = 101;

    SpinKitView spinKitSplash;
    TextView txtSplashLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new NetworkConnectivityHelper(this).startNetworkCallBack();

        spinKitSplash = findViewById(R.id.spinKitSplash);
        txtSplashLoading = findViewById(R.id.txtSplashLoading);

        spinKitSplash.setVisibility(View.GONE);
        txtSplashLoading.setVisibility(View.GONE);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.d("hello","fcm"+instanceIdResult.getToken());
            }
        });

        checkPermissionStatus();

    }

    private void performIntent() {
        spinKitSplash.setVisibility(View.VISIBLE);
        txtSplashLoading.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.mypref), MODE_PRIVATE);
                if (sharedPreferences.getBoolean("isLoggedIn", false)) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2500);
    }

    private void checkPermissionStatus() {
        List<String> permissionsNeeded = new ArrayList<>();
        for (String isPermissionRequired : permissions) {
            int result = ActivityCompat.checkSelfPermission(SplashActivity.this, isPermissionRequired);
            if (result == PackageManager.PERMISSION_DENIED) {
                permissionsNeeded.add(isPermissionRequired);
            }
        }
        if (permissionsNeeded.isEmpty()) {
            performIntent();
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            boolean isAllGranted = true;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                performIntent();
            } else {
                new ToastHelper().makeToast(SplashActivity.this, "Permission Required.", Toast.LENGTH_LONG);
                finishAffinity();
            }
        }
    }
}