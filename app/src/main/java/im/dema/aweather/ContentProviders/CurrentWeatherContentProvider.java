package im.dema.aweather.ContentProviders;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.pushtorefresh.bamboostorage.ABambooSQLiteOpenHelperContentProvider;

import im.dema.aweather.CurrentWeatherOpenHelper;

/**
 * Created by dema on 15.11.14.
 */
public class CurrentWeatherContentProvider extends ABambooSQLiteOpenHelperContentProvider {
    @NonNull
    @Override protected SQLiteOpenHelper provideSQLiteOpenHelper() {
        return new CurrentWeatherOpenHelper(getContext());
    }
}
