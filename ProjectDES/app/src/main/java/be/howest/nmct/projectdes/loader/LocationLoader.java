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
import java.util.List;
import java.util.Objects;

import be.howest.nmct.projectdes.Location;

/**
 * Created by kevin on 03/05/15.
 */
public class LocationLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private static final String url = "http://data.kortrijk.be/sport/outdoorlocaties.json";
    public List<Location> locations = new ArrayList<>();

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

    public void loadCursor(){
        synchronized (lock){
            if(mCursor != null) return;

            MatrixCursor cursor = new MatrixCursor(mColumnNames);
            InputStream input = null;
            JsonReader reader = null;

            try {
                input = new URL(url).openStream();
                reader = new JsonReader(new InputStreamReader(input, "UTF-8"));

                int id = 1;
                reader.beginArray();
                while(reader.hasNext()){
                    reader.beginObject();
                    Location loc = new Location();
                    loc.id = id;
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

                    MatrixCursor.RowBuilder row = cursor.newRow();
                    row.add(id);
                    row.add(loc.adres);
                    row.add(loc.gemeente);
                    row.add(loc.sport);
                    id++;
                    locations.add(loc);

                    reader.endObject();
                }
                reader.endArray();
                mCursor = cursor;
            }
            catch(IOException ex){
                ex.printStackTrace();
            }finally {
                try{
                    reader.close();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                try{
                    input.close();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public Cursor loadInBackground() {
        if(mCursor == null){
            loadCursor();
        }
        return mCursor;
    }
}
