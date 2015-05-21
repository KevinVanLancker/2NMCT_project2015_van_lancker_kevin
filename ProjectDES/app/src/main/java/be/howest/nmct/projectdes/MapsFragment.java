package be.howest.nmct.projectdes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;



/**
 * Created by kevin on 18/04/15.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener{

    private ProgressDialog pDialog;
    private MapFragment mMap;
    private GoogleMap mGMap;
    private List<LatLng> polyz;
    private LocationManager mLocationManager;
    private LatLng mLastPos;
    private static LatLng mLoc;
    private static final String LOCATIE_BENAMING = "be.howest.nmct.projectdes.NEW_LOCATIE_BENAMING";


    public static MapsFragment newInstance(LatLng cor, String benaming){
        MapsFragment fragment = new MapsFragment();
        mLoc = cor;
        Bundle args = new Bundle();
        args.putString(LOCATIE_BENAMING, benaming);
        fragment.setArguments(args);
        return fragment;
    }

    public MapsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {

            if (mMap != null) {
                removeMapFragment();
            }
        } catch (IllegalStateException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_maps,container,false);
        mMap = getMapFragment();
        mMap.getMapAsync(this);
        getCurrentPosition();
        new GetDirection().execute();

        return v;
    }

    private void getCurrentPosition(){
        mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Location location;
        if (mWifi.isConnected()) {
          location   = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        else{
            location   = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if(location != null && location.getTime() < Calendar.getInstance().getTimeInMillis() - 3 * 60 * 1000 ) {
                mLastPos = new LatLng(location.getLatitude(), location.getLongitude());
        }

    }

    private void removeMapFragment(){
        FragmentManager fm;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            fm = getFragmentManager();
        } else {

            fm = getChildFragmentManager();
        }
        fm.beginTransaction().remove(mMap).commit();
    }

    private MapFragment getMapFragment() {
        FragmentManager fm;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            fm = getFragmentManager();
        } else {

            fm = getChildFragmentManager();
        }
        return (MapFragment) fm.findFragmentById(R.id.mMap);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mGMap = map;
        map.setMyLocationEnabled(true);

        }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            mLocationManager.removeUpdates(this);
        }
        else{
            mLastPos = new LatLng(location.getLatitude(),location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    class GetDirection extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Laden van traject. Even geduld...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            while(mLastPos == null) {
                getCurrentPosition();
            }
            String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + mLastPos.latitude +"," + mLastPos.longitude + "&destination=" + mLoc.latitude + "," + mLoc.longitude + "&sensor=false";
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpconn = (HttpURLConnection) url
                        .openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(httpconn.getInputStream()),
                            8192);
                    String strLine = null;

                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }
                String jsonOutput = response.toString();
                JSONObject jsonObject = new JSONObject(jsonOutput);
                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                // Grab the first route
                JSONObject route = routesArray.getJSONObject(0);
                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                polyz = decodePoly(polyline);

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for (int i = 0; i < polyz.size() - 1; i++) {
                LatLng src = polyz.get(i);
                LatLng dest = polyz.get(i + 1);
                mGMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude,dest.longitude))
                        .width(7).color(Color.RED).geodesic(true));
            }
            mGMap.addMarker(new MarkerOptions().title(getArguments().getString(LOCATIE_BENAMING)).position(mLoc)).showInfoWindow();
            mGMap.addMarker(new MarkerOptions().title("Huidige positie").position(mLastPos).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mGMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLoc, 13));
            pDialog.dismiss();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
