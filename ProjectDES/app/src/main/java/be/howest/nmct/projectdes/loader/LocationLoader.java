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

import be.howest.nmct.projectdes.Location;

/**
 * Created by kevin on 03/05/15.
 */
public class LocationLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private static final String url = "http://data.kortrijk.be/sport/outdoorlocaties.json";
    private List<Location> locs;


    private final String[] mColumnNames = new String[]{
            BaseColumns._ID,
            Contract.SportLocatieColumns.COLUMN_LOCATIE_ADRES,
            Contract.SportLocatieColumns.COLUMN_LOCATIE_GEMEENTE,
            Contract.SportLocatieColumns.COLUMN_LOCATIE_SPORT
    };

    private List<Location> getLocations(){
        InputStream input = null;
        JsonReader reader = null;
        List<Location> locations = new ArrayList<>();

        try {
            input = new URL(url).openStream();
            reader = new JsonReader(new InputStreamReader(input, "UTF-8"));

            reader.beginArray();
            while(reader.hasNext()){
                reader.beginObject();
                Location loc = new Location();
                String lat = "";
                String lng = "";
                while(reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("benaming")){
                        loc.benaming = reader.nextString();
                    }else if(name.equals("adres")){
                        loc.adres = reader.nextString();
                    }else if(name.equals("gemeente")){
                        loc.gemeente = reader.nextString();
                    }else if(name.equals("soort")){
                        loc.soort = reader.nextString();
                    }else if(name.equals("sport")){
                        loc.sport = reader.nextString();
                    }else if(name.equals("afmetingen")){
                        if(reader.peek().equals(JsonToken.NULL)){
                            reader.skipValue();
                        }else{
                            loc.afmeting = reader.nextString();
                        }
                    }else if(name.equals("y")){
                        lat = reader.nextString();
                    }else if(name.equals("x")){
                        lng = reader.nextString();
                    }
                    else{
                        reader.skipValue();
                    }
                }

                loc.pos = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                locations.add(loc);

                reader.endObject();
            }
            reader.endArray();
        }    catch(IOException ex){
            ex.printStackTrace();
        }finally {
            try{
                reader.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
            try {
                input.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return locations;
    }
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

            locs = getLocations();
            Collections.sort(locs, new Comparator<Location>() {
                @Override
                public int compare(Location lhs, Location rhs) {
                    return lhs.sport.compareTo(rhs.sport);
                }
            });


            MatrixCursor cursor = new MatrixCursor(mColumnNames);
            int id = 1;

            for (Location loc : locs){
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
