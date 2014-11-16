package im.dema.aweather.ContentProviders;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.pushtorefresh.bamboostorage.ABambooSQLiteOpenHelperContentProvider;

import im.dema.aweather.CitiesOpenHelper;

/**
 * Created by dema on 08.11.14.
 */
public class CitiesContentProvider extends ABambooSQLiteOpenHelperContentProvider {

    @NonNull @Override protected SQLiteOpenHelper provideSQLiteOpenHelper() {
        return new CitiesOpenHelper(getContext());
    }
}
