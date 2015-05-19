package be.howest.nmct.projectdes;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends Activity implements LocationsFragment.OnLocationsFragmentListener, LocationDetailsFragment.OnDetailsFragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager wifi;
        wifi=(WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new LocationsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelectLocation(String adres, String gemeente, String sport) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LocationDetailsFragment fragment = LocationDetailsFragment.newInstance(adres, gemeente, sport);
        fragmentTransaction.replace(R.id.container, fragment);

        fragmentTransaction.addToBackStack("showfragmentlocationdetails");
        fragmentTransaction.commit();
    }

    @Override
    public void onClickMap(LatLng cor,String benaming) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MapsFragment fragment = MapsFragment.newInstance(cor, benaming);
        fragmentTransaction.replace(R.id.container, fragment);

        fragmentTransaction.addToBackStack("showfragmentmap");
        fragmentTransaction.commit();
    }
}
