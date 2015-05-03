package be.howest.nmct.projectdes;

import android.app.Activity;
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
    onChangeFragmentListener i;

    public interface onChangeFragmentListener{
         void showFragmentMap();
         void showFragmentLocations();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        i = (onChangeFragmentListener) activity;
    }


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
                i.showFragmentMap();
            }
        });

        btnMyLocations = (Button) v.findViewById(R.id.btnMyLocations);
        btnMyLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ga naar de locatie fragment
                i.showFragmentLocations();
            }
        });

        return v;
    }
}
