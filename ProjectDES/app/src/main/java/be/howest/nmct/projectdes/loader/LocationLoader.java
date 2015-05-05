package be.howest.nmct.projectdes.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.JsonReader;
import android.util.JsonToken;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import be.howest.nmct.projectdes.model.Location;
import be.howest.nmct.projectdes.provider.LocationProvider;

/**
 * Created by kevin on 03/05/15.
 */
public class LocationLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;


    private final String[] mColumnNames = new String[]{
            BaseColumns._ID,
            Contract.SportLocatieColumns.COLUMN_LOCATIE_ADRES,
            Contract.SportLocatieColumns.COLUMN_LOCATIE_GEMEENTE,
            Contract.SportLocatieColumns.COLUMN_LOCATIE_SPORT
    };

    private static Object lock = new Object();

    public LocationLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if(mCursor != null){
            deliverResult(mCursor);
        }
        if(takeContentChanged() || mCursor == null){
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        if(mCursor == null){
            loadCursor();
        }
        return mCursor;
    }

    public void loadCursor(){
        synchronized (lock){
            if(mCursor != null) return;
            MatrixCursor cursor = new MatrixCursor(mColumnNames);
            int id = 1;

            for (Location loc : LocationProvider.getLocations()){
                MatrixCursor.RowBuilder row = cursor.newRow();
                row.add(id);
                row.add(loc.adres);
                row.add(loc.gemeente);
                row.add(loc.sport);
                id++;
            }
            mCursor = cursor;
        }
    }

}
