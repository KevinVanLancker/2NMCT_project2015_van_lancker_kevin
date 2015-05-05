package be.howest.nmct.projectdes;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import be.howest.nmct.projectdes.model.Location;
import be.howest.nmct.projectdes.provider.LocationProvider;


public class LocationDetailsFragment extends Fragment {


    private Button btnMap;
    private TextView tvBenaming, tvAdres, tvGemeente, tvSoort, tvSport, tvAfmetingen;
    private static final String LOCATIE_ADRES = "be.howest.nmct.projectdes.NEW_LOCATIE_ADRES";
    private static final String LOCATIE_GEMEENTE = "be.howest.nmct.projectdes.NEW_LOCATIE_GEMEENTE";
    private static final String LOCATIE_SPORT = "be.howest.nmct.projectdes.NEW_LOCATIE_SPORT";
    private static Location loc;
    private OnDetailsFragmentListener listener;

    public interface OnDetailsFragmentListener {
        void onClickMap(LatLng cor);
    }


    public LocationDetailsFragment() {
        // Required empty public constructor
    }

    public static LocationDetailsFragment newInstance(String adres,String gemeente, String sport){
        LocationDetailsFragment fragment = new LocationDetailsFragment();
        Bundle args = new Bundle();
        args.putString(LOCATIE_ADRES, adres);
        args.putString(LOCATIE_GEMEENTE, gemeente);
        args.putString(LOCATIE_SPORT, sport);
        fragment.setArguments(args);
        //loc = LocationProvider.getLocation(adres, gemeente, sport);
        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_location_details, container, false);

        btnMap = (Button) v.findViewById(R.id.btnMap);
        tvBenaming = (TextView) v.findViewById(R.id.tvBenaming);
        tvAdres = (TextView) v.findViewById(R.id.tvAdres);
        tvGemeente = (TextView) v.findViewById(R.id.tvGemeente);
        tvSoort = (TextView) v.findViewById(R.id.tvSoort);
        tvSport = (TextView) v.findViewById(R.id.tvSport);
        tvAfmetingen = (TextView) v.findViewById(R.id.tvAfmetingen);

      /*  btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickMap(loc.pos);
            }
        });*/

        tvAdres.setText(getArguments().getString(LOCATIE_ADRES));

        /*tvBenaming.setText(loc.benaming);
        tvAdres.setText(loc.adres);
        tvGemeente.setText(loc.gemeente);
        tvSoort.setText("Dit is een " + loc.soort);
        tvSport.setText(loc.sport);
        tvAfmetingen.setText("De afmetingen zijn: " + loc.afmeting);*/


        return v;
    }


}
