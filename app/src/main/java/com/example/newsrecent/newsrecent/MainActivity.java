package com.example.newsrecent.newsrecent;
//SPLASH ACTIVITY
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainActivity extends AppCompatActivity {
    private long SplashTimeout = 2500 ;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION}, REQUEST_CODE);
//        if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            SplashTimeout = 8000;
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Location Access is necessary to find News for your location please provide access")
//                    .setTitle("Location Access Required").setCancelable(false).setPositiveButton("Cancel",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_COARSE_LOCATION}, REQUEST_CODE);
//                        }
//                    });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//
//      }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(homeIntent);
                finish();
            }
        },SplashTimeout);
    }
}