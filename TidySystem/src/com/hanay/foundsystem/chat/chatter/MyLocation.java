package com.hanay.foundsystem.chat.chatter;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyLocation {
	
	private Context mContext;
	private LocationManager mLocationManager; 
    
	private final LocationListener mLocationListener = new LocationListener() {  
        public void onLocationChanged(Location location) { //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发  
            // log it when the location changes  
            if (location != null) {  
                Log.i("SuperMap", "Location changed : Lat: "  
                  + location.getLatitude() + " Lng: "  
                  + location.getLongitude());  
            }  
        }  
      
        public void onProviderDisabled(String provider) {  
        // Provider被disable时触发此函数，比如GPS被关闭  
        }  
      
        public void onProviderEnabled(String provider) {  
        //  Provider被enable时触发此函数，比如GPS被打开  
        }  
      
        public void onStatusChanged(String provider, int status, Bundle extras) {  
        // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
        }  
    }; 
    
    public MyLocation(Context context)
    {
    	this.mContext=context;
    	
    	mLocationManager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  
				 1000, 0, mLocationListener);
    }
    
    public String getMyLocation()
    {
    	Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);				
		String st0=null;
		if(location==null)
		{
			st0="location manager is empty";
		}
		else
		{
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			st0="there's a car accident at longitude: "+longitude+", latitude: "+latitude;
		}
		
		return st0;
    }
}
