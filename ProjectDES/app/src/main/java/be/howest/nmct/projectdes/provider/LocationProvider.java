package be.howest.nmct.projectdes.provider;

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

/**
 * Created by kevin on 05/05/15.
 */
public class LocationProvider {
    private static List<Location> locations = new ArrayList<>();
    private static Location location;
    private static final String url = "http://data.kortrijk.be/sport/outdoorlocaties.json";


    public static Location getLocation(String adres, String gemeente, String sport){
        for(Location loc : getLocations()){
            if(loc.adres == adres && loc.gemeente == gemeente && loc.sport == sport){
                location = loc;
            }
        }
        return location;
    }

    public static List<Location> getLocations(){
        InputStream input = null;
        JsonReader reader = null;

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
                        lng = reader.nextString();
                    }else if(name.equals("x")){
                        lat = reader.nextString();
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

        Collections.sort(locations, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                return lhs.sport.compareTo(rhs.sport);
            }
        });

        return locations;
    }
}
