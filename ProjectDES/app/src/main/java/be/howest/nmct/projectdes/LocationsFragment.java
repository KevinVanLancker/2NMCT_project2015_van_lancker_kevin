package be.howest.nmct.projectdes;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import be.howest.nmct.projectdes.loader.Contract;
import be.howest.nmct.projectdes.loader.LocationLoader;

/**
 * Created by kevin on 03/05/15.
 */
public class LocationsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    LocationAdapter la;

    public static LocationsFragment newInstance(){
        LocationsFragment fragment = new LocationsFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        la = new LocationAdapter(getActivity(),R.layout.row_locaties,null,new String[]{Contract.SportLocatieColumns.COLUMN_LOCATIE_ADRES,Contract.SportLocatieColumns.COLUMN_LOCATIE_GEMEENTE, Contract.SportLocatieColumns.COLUMN_LOCATIE_SPORT },
             new int[]{R.id.tvAdres,R.id.tvGemeente,R.id.tvSport},0);
        setListAdapter(la);
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LocationLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        la.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        la.swapCursor(null);
    }

    class LocationAdapter extends SimpleCursorAdapter {
        public LocationAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }


    }

}
