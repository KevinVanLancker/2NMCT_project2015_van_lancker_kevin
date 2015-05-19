package be.howest.nmct.projectdes;


import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import be.howest.nmct.projectdes.loader.Contract;
import be.howest.nmct.projectdes.loader.LocationLoader;

/**
 * Created by kevin on 03/05/15.
 */
public class LocationsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    LocationAdapter la;
    private OnLocationsFragmentListener listener;

    public interface OnLocationsFragmentListener{
        void onSelectLocation(String adres, String gemeente, String sport);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnLocationsFragmentListener) activity;
        }catch(ClassCastException ex){
            throw new ClassCastException(activity.toString() + " must implement OnLocationsFragmentListener");
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = (Cursor) la.getItem(position);
        String selectedAdres = c.getString(c.getColumnIndex(Contract.SportLocatieColumns.COLUMN_LOCATIE_ADRES));
        String selectedGemeente = c.getString(c.getColumnIndex(Contract.SportLocatieColumns.COLUMN_LOCATIE_GEMEENTE));
        String selectedSport = c.getString(c.getColumnIndex(Contract.SportLocatieColumns.COLUMN_LOCATIE_SPORT));
        if(listener != null){
            listener.onSelectLocation(selectedAdres,selectedGemeente,selectedSport);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        la = new LocationAdapter(getActivity(),R.layout.row_locaties,null,new String[]{Contract.SportLocatieColumns.COLUMN_LOCATIE_ADRES,Contract.SportLocatieColumns.COLUMN_LOCATIE_GEMEENTE, Contract.SportLocatieColumns.COLUMN_LOCATIE_SPORT },
             new int[]{R.id.tvAdres,R.id.tvGemeente,R.id.tvSport},0);
        setListAdapter(la);
        getLoaderManager().initLoader(0, null, this);
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

       /* @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
        }*/
    }

}
