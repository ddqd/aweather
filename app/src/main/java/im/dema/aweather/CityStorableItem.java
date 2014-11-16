package im.dema.aweather;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.bamboostorage.ABambooStorableItem;
import com.pushtorefresh.bamboostorage.BambooStorableTypeMeta;

/**
 * Created by dema on 08.11.14.
 */

@BambooStorableTypeMeta(
        contentPath = CityStorableItem.TableInfo.TABLE_NAME,
        internalIdFieldName = CityStorableItem.TableInfo.ID
)
public class CityStorableItem extends ABambooStorableItem {

    public int id;

    private int cityId;

    public String getCityName() {
        return cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getCountryCode() {
        return countryCode;
    }

    private String cityName;
    private double lat;
    private double lon;
    private String countryCode;

    public CityStorableItem() {}

    public CityStorableItem(String line) {
        try {
            String[] cityItemDescription = line.split("\\t+");
            this.cityId = Integer.parseInt(cityItemDescription[0]);
            this.cityName = cityItemDescription[1];
            this.lat = Double.parseDouble(cityItemDescription[2]);
            this.lon = Double.parseDouble(cityItemDescription[3]);
            this.countryCode = cityItemDescription[4];
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ContentValues toContentValues(@NonNull Resources resources) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableInfo.CITY_ID, cityId);
        contentValues.put(TableInfo.CITY_NAME, cityName);
        contentValues.put(TableInfo.LAT, lat);
        contentValues.put(TableInfo.LON, lon);
        contentValues.put(TableInfo.COUNTRY_CODE, countryCode);

        return contentValues;
    }

    @Override
    public void fillFromCursor(@NonNull Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(TableInfo.ID));
        cityId = cursor.getInt(cursor.getColumnIndex(TableInfo.CITY_ID));
        cityName = cursor.getString(cursor.getColumnIndex(TableInfo.CITY_NAME));
        lat = cursor.getDouble(cursor.getColumnIndex(TableInfo.LAT));
        lon = cursor.getDouble(cursor.getColumnIndex(TableInfo.LON));
        countryCode = cursor.getString(cursor.getColumnIndex(TableInfo.COUNTRY_CODE));
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CityStorableItem that = (CityStorableItem) o;


        if (cityId != that.cityId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = cityName != null ? cityName.hashCode() : 0;
        result = 31 * result + cityId;
        result = 31 * result + (int) (cityId ^ (cityId >>> 32));
        return result;
    }

    public interface TableInfo {
        String TABLE_NAME         = "cities";

        String ID =   "_id";
        String CITY_ID      =   "city_id";
        String CITY_NAME    =   "city_name";
        String LAT          =   "lat";
        String LON          =   "lon";
        String COUNTRY_CODE =   "country_code";

        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CITY_ID + " INTEGER, " +
                CITY_NAME + " TEXT, " +
                LAT + " REAL, " +
                LON + " REAL, " +
                COUNTRY_CODE + " TEXT);";
    }
}
