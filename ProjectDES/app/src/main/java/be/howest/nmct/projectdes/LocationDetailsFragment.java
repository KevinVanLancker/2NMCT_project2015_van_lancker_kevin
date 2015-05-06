package be.howest.nmct.projectdes;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
    private ProgressBar pbProgress;
    private OnDetailsFragmentListener listener;

    public interface OnDetailsFragmentListener {
        void onClickMap(LatLng cor, String benaming);
    }


    public LocationDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnDetailsFragmentListener) activity;
        }catch(ClassCastException ex){
            throw new ClassCastException(activity.toString() + " must implement OnDetailsFragmentListener");
        }
    }


    public static LocationDetailsFragment newInstance(String adres,String gemeente, String sport){
        LocationDetailsFragment fragment = new LocationDetailsFragment();
        Bundle args = new Bundle();
        args.putString(LOCATIE_ADRES, adres);
        args.putString(LOCATIE_GEMEENTE, gemeente);
        args.putString(LOCATIE_SPORT, sport);
        fragment.setArguments(args);
        return fragment;

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_location_details, container, false);

        pbProgress = (ProgressBar) v.findViewById(R.id.pbProgress);
        btnMap = (Button) v.findViewById(R.id.btnMap);
        tvBenaming = (TextView) v.findViewById(R.id.tvBenaming);
        tvAdres = (TextView) v.findViewById(R.id.tvAdres);
        tvGemeente = (TextView) v.findViewById(R.id.tvGemeente);
        tvSoort = (TextView) v.findViewById(R.id.tvSoort);
        tvSport = (TextView) v.findViewById(R.id.tvSport);
        tvAfmetingen = (TextView) v.findViewById(R.id.tvAfmetingen);
        new GetInfo().execute();


        return v;
    }

    private class GetInfo extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            loc = LocationProvider.getLocation(getArguments().getString(LOCATIE_ADRES), getArguments().getString(LOCATIE_GEMEENTE), getArguments().getString(LOCATIE_SPORT));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pbProgress.setVisibility(View.GONE);
            tvBenaming.setText(loc.benaming);
            tvAdres.setText(loc.adres);
            tvGemeente.setText(loc.gemeente);
            tvSoort.setText("Dit is een " + loc.soort);
            tvSport.setText(loc.sport);
            tvAfmetingen.setText("De afmetingen zijn: " + loc.afmeting);
            btnMap.setVisibility(View.VISIBLE);
            btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickMap(loc.pos, loc.benaming);
                }
            });
        }
    }


}
