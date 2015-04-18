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
public class MainFragment extends Fragment {

    private Button btnMap, btnMyLocations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main,container,false);

        btnMap = (Button) v.findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ga naar de mapFragment
            }
        });

        btnMyLocations = (Button) v.findViewById(R.id.btnMyLocations);
        btnMyLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ga naar de locatie fragment
            }
        });

        return v;
    }
}
