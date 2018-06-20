package com.example.keetawatsrichompoo.loginfirebase;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by Keetawat Srichompoo on 17-May-18.
 */

public class GPSTracker extends Service implements LocationListener {
    private final Context mContext;

    boolean isGPSOn = false;
    boolean isNetworkOn = false;
    boolean available = false;

    Location loc;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;

    protected LocationManager locManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getApplication();
    }

    public Location getLocation(){
        try{
            locManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSOn = locManager.isProviderEnabled(locManager.GPS_PROVIDER);
            isNetworkOn = locManager.isProviderEnabled(locManager.NETWORK_PROVIDER);

            if(!isGPSOn && !isNetworkOn) {

            } else {
                this.available = true;
                if(isNetworkOn) {
                    locManager.requestLocationUpdates(
                            locManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_UPDATES,this);
                    if(locManager != null) {
                        loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }
                }

                if(isGPSOn) {
                    if(loc == null) {
                        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,MIN_DISTANCE_UPDATES,this);

                        if(locManager != null){
                            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }

                        if(loc != null){
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        } return loc;
    }

    public void stopGPS(){
        if(locManager != null) {
            locManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude(){
        if(loc != null) {
            latitude = loc.getLatitude();
        } return latitude;
    }

    public double getLongitude(){
        if(loc != null) {
            longitude = loc.getLongitude();
        } return longitude;
    }

    public boolean canGetLocation(){
        return this.available;
    }

    public void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS has been set");
        alertDialog.setMessage("GPS is not enabled");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
