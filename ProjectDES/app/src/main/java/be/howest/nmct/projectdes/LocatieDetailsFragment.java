package be.howest.nmct.projectdes;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class LocatieDetailsFragment extends Fragment {

    Button btnMap;
    TextView tvBenaming, tvAdres, tvGemeente, tvSoort, tvSport, tvAfmetingen;
    static final String LOCATIE_ADRES = "be.howest.nmct.projectdes.NEW_LOCATIE_ADRES";
    static final String LOCATIE_GEMEENTE = "be.howest.nmct.projectdes.NEW_LOCATIE_GEMEENTE";
    static final String LOCATIE_SPORT = "be.howest.nmct.projectdes.NEW_LOCATIE_SPORT";


    public LocatieDetailsFragment() {
        // Required empty public constructor
    }

    public static LocatieDetailsFragment newInstance(String adres,String gemeente, String sport){
        LocatieDetailsFragment fragment = new LocatieDetailsFragment();
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
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_locatie_details, container, false);

        btnMap = (Button) v.findViewById(R.id.btnMap);
        tvBenaming = (TextView) v.findViewById(R.id.tvBenaming);
        tvAdres = (TextView) v.findViewById(R.id.tvAdres);
        tvGemeente = (TextView) v.findViewById(R.id.tvGemeente);
        tvSoort = (TextView) v.findViewById(R.id.tvSoort);
        tvSport = (TextView) v.findViewById(R.id.tvSport);
        tvAfmetingen = (TextView) v.findViewById(R.id.tvAfmetingen);


        return v;
    }


}
