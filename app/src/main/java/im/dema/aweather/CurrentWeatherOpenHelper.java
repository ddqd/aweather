package im.dema.aweather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dema on 15.11.14.
 */
public class CurrentWeatherOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "current_weather";

    public CurrentWeatherOpenHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CurrentWeatherStorableItem.TableInfo.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
