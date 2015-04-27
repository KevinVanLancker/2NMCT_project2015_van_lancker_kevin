package be.howest.nmct.projectdes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kevin on 18/04/15.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    MapFragment mMap;
    GoogleMap mGMap;
    LatLng mCurPos;
    GoogleApiClient mGoogleApiClient;


    public MapsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps,container,false);

        mMap = getMapFragment();
        mMap.getMapAsync(this);

        return v;
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
        //mGMap.setOnMyLocationChangeListener();
        map.setMyLocationEnabled(true);
       /* Location loc = map.getMyLocation();
        LatLng home = new LatLng(loc.getLatitude(), loc.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(home,15));
        map.addMarker(new MarkerOptions()
                        .title("home")
                        .snippet("")
                        .position(home)
        );*/

        //testcode
        /*LatLng sydney = new LatLng(-33.867, 151.206);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,13));
        map.addMarker(new MarkerOptions()
                        .title("home")
                        .snippet("")
                        .position(sydney)
        );*/
    }



    //komt hier niet in
    @Override
    public void onLocationChanged(Location l2) {
        /*CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(l2.getLatitude(), l2.getLongitude()), 15);*/

       // mGMap.animateCamera(cameraUpdate);
        mCurPos = new LatLng(l2.getLatitude(),l2.getLongitude());
        mGMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurPos,15));
        mGMap.addMarker(new MarkerOptions()
                .title("Thuis")
                .snippet("")
                .position(mCurPos));
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


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
