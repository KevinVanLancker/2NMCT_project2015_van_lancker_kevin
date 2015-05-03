package be.howest.nmct.projectdes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by kevin on 18/04/15.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener{

    MapFragment mMap;
    GoogleMap mGMap;
    LatLng mCurPos;


    public static MapsFragment newInstance(){
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

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
        mGMap.setOnMyLocationChangeListener(this);
        map.setMyLocationEnabled(true);

        }




    @Override
    public void onMyLocationChange(Location loc) {
        mGMap.clear();
        mCurPos = new LatLng(loc.getLatitude(),loc.getLongitude());
        mGMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurPos,15));
    }

}
