package im.dema.aweather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dema on 09.11.14.
 */
public class CitiesOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "cities";

    public CitiesOpenHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CitiesStorableItem.TableInfo.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
