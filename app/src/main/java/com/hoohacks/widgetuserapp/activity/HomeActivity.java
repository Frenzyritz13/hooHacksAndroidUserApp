package com.hoohacks.widgetuserapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hoohacks.widgetuserapp.R;
import com.hoohacks.widgetuserapp.dialog.DialogProgress;
import com.hoohacks.widgetuserapp.util.NetworkConnectivityHelper;
import com.hoohacks.widgetuserapp.util.ToastHelper;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    ImageView imgProfile;
    TextView txtName, txtEmail, txtMobile, txtLogout, txtDeviceId, txtCalibrate;
    SharedPreferences sharedPreferences;
    boolean isCalibrationRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new NetworkConnectivityHelper(this).startNetworkCallBack();

        imgProfile = findViewById(R.id.imgProfile);
        txtName = findViewById(R.id.txtProfileName);
        txtEmail = findViewById(R.id.txtProfileEmail);
        txtMobile = findViewById(R.id.txtProfileContact);
        txtLogout = findViewById(R.id.txtLogout);
        txtDeviceId = findViewById(R.id.txtProfileDeviceId);
        txtCalibrate = findViewById(R.id.txtCalibrate);
        sharedPreferences = getSharedPreferences(getString(R.string.mypref), MODE_PRIVATE);

        txtName.setText(sharedPreferences.getString("name","--"));
        txtDeviceId.setText("Device Id : "+sharedPreferences.getString("device_id","--"));
        txtMobile.setText("Contact : "+sharedPreferences.getString("mobile","--"));
        txtEmail.setText("Email : "+sharedPreferences.getString("email","--"));

        Picasso.get().load(sharedPreferences.getString("image_link","no_image")).error(R.drawable.ic_user_150)
                .into(imgProfile);

        FirebaseMessaging.getInstance().subscribeToTopic("active")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();

                FirebaseMessaging.getInstance().unsubscribeFromTopic("active")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkConnectivityHelper.isConnected){
                    DialogFragment progressDialog = new DialogProgress("Requesting for calibration...");
                    progressDialog.setCancelable(false);
                    progressDialog.show(getSupportFragmentManager(),"Dialog Progress");

                    DialogFragment progressDialogCali = new DialogProgress("Device in calibration mode, start fidgeting with your device...");
                    progressDialogCali.setCancelable(false);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Calibration/DeviceId1");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!isCalibrationRequested){
                                isCalibrationRequested = true;
                                databaseReference.child("status").setValue("3").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if(task.isSuccessful()){
                                            progressDialogCali.show(getSupportFragmentManager(),"Dialog Progress");
                                        }else{
                                            new ToastHelper().makeToast(HomeActivity.this,"Something went wrong! Please try again later.", Toast.LENGTH_LONG);
                                        }
                                    }
                                });
                            }
                            String status = "";
                            status = (String)snapshot.child("status").getValue();
                            if(status.equals("2")){
                                progressDialogCali.dismiss();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.CustomAlertDialogNew);
                                final View customLayout
                                        = getLayoutInflater()
                                        .inflate(R.layout.custom_alert_dialog, null);
                                Button btnDialog = customLayout.findViewById(R.id.btnDialog);
                                ImageView imgDialog = customLayout.findViewById(R.id.imgDialog);
                                imgDialog.setImageDrawable(getResources().getDrawable(R.drawable.emo_happy));
                                builder.setView(customLayout);
                                builder.setTitle("Yayyyy!!");
                                builder.setMessage("Calibration was successful. You can always re-calibrate your device.");
                                final AlertDialog alertDialog = builder.create();
                                //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                                alertDialog.show();
                                btnDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                                new ToastHelper().makeToast(HomeActivity.this,"Calibration successful.", Toast.LENGTH_LONG);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.CustomAlertDialogNew);
                    final View customLayout
                            = getLayoutInflater()
                            .inflate(R.layout.custom_alert_dialog, null);
                    Button btnDialog = customLayout.findViewById(R.id.btnDialog);
                    ImageView imgDialog = customLayout.findViewById(R.id.imgDialog);
                    imgDialog.setImageDrawable(getResources().getDrawable(R.drawable.emo_sad));
                    builder.setView(customLayout);
                    builder.setTitle("Can't connect to Internet!");
                    builder.setMessage("Please check your internet connection and try again later.");
                    final AlertDialog alertDialog = builder.create();
                    //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    alertDialog.show();
                    btnDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        });

    }
}