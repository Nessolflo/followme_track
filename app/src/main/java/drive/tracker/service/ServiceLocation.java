package drive.tracker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import drive.tracker.domain.ConstantsTracker;
import drive.tracker.service.holder.UserLocations;


public class ServiceLocation extends Service {
    public ServiceLocation() {
    }

    public static ServiceLocation INSTANCE = null;
    private static final String TAG = "SERVICELOCATION";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL_MINUTES = 2;
    private static final int LOCATION_INTERVAL = 1000 * 60 * LOCATION_INTERVAL_MINUTES;
    private static final float LOCATION_DISTANCE = 10f;

    private ServiceRepository repository;

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            final UserLocations userLocations = new UserLocations();
            userLocations.latitud = String.valueOf(location.getLatitude());
            userLocations.longitud = String.valueOf(location.getLongitude());
            userLocations.idUnit = getSharedPreferences(getPackageName(), MODE_PRIVATE)
                    .getString(ConstantsTracker.KEY_ID, "");
            sendData(userLocations);

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }

    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        INSTANCE =this;
        repository = new ServiceRepository();
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);

            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        INSTANCE = null;
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager =
                    (LocationManager) getApplicationContext()
                            .getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void sendData(UserLocations userLocations){
        repository.sendLocation(userLocations, new ServiceRepository.CallbackSendLocation() {
            @Override
            public void onSuccessful() {
                Log.d(ConstantsTracker.TAG, "Send location onSuccessful");
            }

            @Override
            public void onFail(String message) {
                Log.d(ConstantsTracker.TAG, "Send location onFail "+message);
            }
        });
    }
}
