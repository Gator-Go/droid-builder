package ppp.ppp.ppp;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GpsTracker extends Service implements LocationListener {

  private final Context lContext;
  private Resources res;

  // flag for GPS status
  boolean isGPSEnabled = false;

  // flag for GPS status
  boolean canGetLocation = false;

  Location location; // location
  double latitude; // latitude
  double longitude; // longitude

  // The minimum distance to change Updates in meters
  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

  // The minimum time between updates in milliseconds
  private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

  // Declaring a Location Manager
  protected LocationManager locationManager;

  public GpsTracker(Context context) {
    this.lContext = context;
    this.res = context.getResources();
    getLocation();
  }

  public Location getLocation() {
    try {

      locationManager = (LocationManager) lContext.getSystemService(LOCATION_SERVICE);

      // getting GPS status
      isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

      if (!isGPSEnabled) {

        // no network provider is enabled

      } else {

        this.canGetLocation = true;

        // if GPS Enabled get lat/long using GPS Services
        if (isGPSEnabled && lContext.checkSelfPermission( android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
          if (location == null) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            Log.d("GPS Enabled", "GPS Enabled");

            if (locationManager != null) {
              location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
              if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return location;
  }
	
  /**
   * Stop using GPS listener
   * Calling this function will stop using GPS in your app
   **/
  public void stopUsingGPS(){

    if(locationManager != null && isGPSEnabled && lContext.checkSelfPermission( android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
      locationManager.removeUpdates(GpsTracker.this);
  }
	
  /**
   * Function to get latitude
   **/
  public double getLatitude(){

    if(location != null)
      latitude = location.getLatitude();
		
    return latitude;
  }
	
  /**
   * Function to get longitude
   **/
  public double getLongitude(){

    if(location != null)
      longitude = location.getLongitude();
		
    return longitude;
  }
	
  /**
   * Function to check GPS/wifi enabled
   * @return boolean
   **/
  public boolean canGetLocation() {
    return this.canGetLocation;
  }
	

  @Override
  public void onLocationChanged(Location location) {
  }

  @Override
  public void onProviderDisabled(String provider) {
  }

  @Override
  public void onProviderEnabled(String provider) {
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
  }

  @Override
  public IBinder onBind(Intent arg0) {
    return null;
  }

}
