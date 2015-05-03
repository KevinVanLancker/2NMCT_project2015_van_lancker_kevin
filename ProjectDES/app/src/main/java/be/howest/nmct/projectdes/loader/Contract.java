package be.howest.nmct.projectdes.loader;

import android.provider.BaseColumns;

/**
 * Created by kevin on 03/05/15.
 */
public class Contract {
    public interface SportLocatieColumns extends BaseColumns{
        public static final String COLUMN_LOCATIE_ADRES = "locatie_adres";
        public static final String COLUMN_LOCATIE_GEMEENTE = "locatie_gemeente";
        public static final String COLUMN_LOCATIE_SPORT = "locatie_sport";
    }
}
