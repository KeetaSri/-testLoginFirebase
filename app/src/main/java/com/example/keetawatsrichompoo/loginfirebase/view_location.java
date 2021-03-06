package com.example.keetawatsrichompoo.loginfirebase;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class view_location extends AppCompatActivity {
    private Button getLoc;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);

        try{
            if(ActivityCompat.checkSelfPermission(this, mPermission) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        getLoc = (Button) findViewById(R.id.getLocate);
        getLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps = new GPSTracker(view_location.this);
                if(!gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(),"Current Location is \n Lat: "
                            + latitude + "\n Long: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    gps.showAlert();
                }
            }
        });

    }
}
