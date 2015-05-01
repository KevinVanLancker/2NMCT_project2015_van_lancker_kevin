package be.howest.nmct.projectdes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by kevin on 18/04/15.
 */
public class MyLocationsFragment extends Fragment {

    Button btnAddLocation;

    public static MyLocationsFragment newInstance(){
        MyLocationsFragment fragment = new MyLocationsFragment();
        return fragment;
    }
    
    public MyLocationsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_locations,container,false);

        btnAddLocation = (Button) v.findViewById(R.id.btnMyLocations);

       /* btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //voeg een locatie toe
            }
        }); */

        return v;
    }
}
