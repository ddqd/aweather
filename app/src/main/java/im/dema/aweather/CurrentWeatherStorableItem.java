package im.dema.aweather;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.bamboostorage.ABambooStorableItem;
import com.pushtorefresh.bamboostorage.BambooStorableTypeMeta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by dema on 15.11.14.
 */

@BambooStorableTypeMeta(
        contentPath = CurrentWeatherStorableItem.TableInfo.TABLE_NAME,
        internalIdFieldName = CurrentWeatherStorableItem.TableInfo.CITY_ID
)

public class CurrentWeatherStorableItem extends ABambooStorableItem {
    private static final String iconBaseURL = "http://openweathermap.org/img/w/";
    private static final String iconExtension = ".png";

    public int cityId;
    public String cityName;
    public double lat;
    public double lon;
    public int timeStamp;
    public String country;
    public int clouds;
    public int windSpeed;
    public int windDeg;

    public int temp;
    public int pressure;
    public int humidity;
    public int temp_min;
    public int temp_max;

    public int rainHour = 0;
    public int rainValue = 0;

    public int weatherId;
    public String description;
    public String icon;

    public void parseFromJson (JSONObject item) throws JSONException{
        cityId = item.getInt("id");
        cityName = item.getString("name");

        JSONObject coord = item.getJSONObject("coord");
        lat = coord.getDouble("lat");
        lon = coord.getDouble("lon");

        timeStamp = item.getInt("dt");

        JSONObject sys = item.getJSONObject("sys");
        country = sys.getString("country");

        JSONObject cloudsJson = item.getJSONObject("clouds");
        clouds = cloudsJson.getInt("all");

        JSONObject wind = item.getJSONObject("wind");
        windSpeed = wind.getInt("speed");
        windDeg = wind.getInt("deg");

        JSONObject main = item.getJSONObject("main");

        temp = main.getInt("temp");
        pressure = main.getInt("pressure");
        humidity = main.getInt("humidity");
        temp_min = main.getInt("temp_min");
        temp_max = main.getInt("temp_max");

        JSONArray weatherArray = item.getJSONArray("weather");
        JSONObject weatherItem = weatherArray.getJSONObject(0);
        weatherId = weatherItem.getInt("id");
        description = weatherItem.getString("description");
        parseRain(item);
        icon = weatherItem.getString("icon");
        parseRain(item);
    }

    private void parseRain(JSONObject item) throws JSONException{
        JSONObject rain = item.optJSONObject("rain");
        if(rain != null) {
            if (rain.has("1h")) {
                rainHour = 1;
                rainValue = rain.getInt("1h");
            } else if (rain.has("3h")) {
                rainHour = 3;
                rainValue = rain.getInt("3h");
            }
        }
    }

    public String getIconUrl () {
        StringBuilder builder = new StringBuilder();
        builder.append(iconBaseURL);
        builder.append(icon);
        builder.append(iconExtension);
        return builder.toString();
    }

    public Date getWeatherDate() {
        return new Date((long) timeStamp * 1000);
    }

    @NonNull
    @Override
    public ContentValues toContentValues(@NonNull Resources resources) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(TableInfo.CITY_ID, cityId);
        contentValues.put(TableInfo.CITY_NAME, cityName);
        contentValues.put(TableInfo.LAT, lat);
        contentValues.put(TableInfo.LON, lon);
        contentValues.put(TableInfo.TIME, timeStamp);
        contentValues.put(TableInfo.COUNTRY, country);
        contentValues.put(TableInfo.CLOUDS, clouds);
        contentValues.put(TableInfo.WIND_SPEED, windSpeed);
        contentValues.put(TableInfo.WIND_DEGRESS, windDeg);
        contentValues.put(TableInfo.TEMP, temp);
        contentValues.put(TableInfo.PRESSURE, pressure);
        contentValues.put(TableInfo.HUMIDITY, humidity);
        contentValues.put(TableInfo.TEMP_MIN, temp_min);
        contentValues.put(TableInfo.TEMP_MAX, temp_max);
        contentValues.put(TableInfo.RAIN_HOUR, rainHour);
        contentValues.put(TableInfo.RAIN_VALUE, rainValue);
        contentValues.put(TableInfo.WEATHER_ID, weatherId);
        contentValues.put(TableInfo.DESCRIPTION, description);
        contentValues.put(TableInfo.ICON, icon);
        return contentValues;
    }

    @Override
    public void fillFromCursor(@NonNull Cursor cursor) {
        cityId = cursor.getInt(cursor.getColumnIndex(TableInfo.CITY_ID));
        cityName = cursor.getString(cursor.getColumnIndex(TableInfo.CITY_NAME));
        lat = cursor.getDouble(cursor.getColumnIndex(TableInfo.LAT));
        lon = cursor.getDouble(cursor.getColumnIndex(TableInfo.LON));
        timeStamp = cursor.getInt(cursor.getColumnIndex(TableInfo.TIME));
        country = cursor.getString(cursor.getColumnIndex(TableInfo.COUNTRY));
        clouds = cursor.getInt(cursor.getColumnIndex(TableInfo.CLOUDS));
        windSpeed = cursor.getInt(cursor.getColumnIndex(TableInfo.WIND_SPEED));
        windDeg = cursor.getInt(cursor.getColumnIndex(TableInfo.WIND_DEGRESS));
        temp = cursor.getInt(cursor.getColumnIndex(TableInfo.TEMP));
        pressure = cursor.getInt(cursor.getColumnIndex(TableInfo.PRESSURE));
        humidity = cursor.getInt(cursor.getColumnIndex(TableInfo.HUMIDITY));
        temp_min = cursor.getInt(cursor.getColumnIndex(TableInfo.TEMP_MIN));
        temp_max = cursor.getInt(cursor.getColumnIndex(TableInfo.TEMP_MAX));
        rainHour = cursor.getInt(cursor.getColumnIndex(TableInfo.RAIN_HOUR));
        rainValue = cursor.getInt(cursor.getColumnIndex(TableInfo.RAIN_VALUE));
        weatherId = cursor.getInt(cursor.getColumnIndex(TableInfo.WEATHER_ID));
        description = cursor.getString(cursor.getColumnIndex(TableInfo.DESCRIPTION));
        icon = cursor.getString(cursor.getColumnIndex(TableInfo.ICON));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CurrentWeatherStorableItem that = (CurrentWeatherStorableItem) o;
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
        String TABLE_NAME   = "current_weather";

        String CITY_ID      =   "city_id";
        String CITY_NAME    =   "city_name";
        String LAT          =   "lat";
        String LON          =   "lon";
        String TIME         =   "time_stamp";
        String COUNTRY      =   "country";
        String CLOUDS       =   "clouds";
        String WIND_SPEED   =   "wind_speed";
        String WIND_DEGRESS =   "wind_degress";
        String TEMP         =   "temp";
        String PRESSURE     =   "pressure";
        String HUMIDITY     =   "humidity";
        String TEMP_MIN     =   "temp_min";
        String TEMP_MAX     =   "temp_max";
        String RAIN_HOUR    =   "rain_hour";
        String RAIN_VALUE   =   "rain_value";
        String WEATHER_ID   =   "weather_id";
        String DESCRIPTION  =   "description";
        String ICON         =   "icon";

        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                CITY_ID + " INTEGER PRIMARY KEY, " +
                CITY_NAME + " TEXT, " +
                LAT + " REAL, " +
                LON + " REAL, " +
                TIME + " INTEGER, " +
                COUNTRY + " TEXT, " +
                CLOUDS + " INTEGER, " +
                WIND_SPEED + " INTEGER, " +
                WIND_DEGRESS + " INTEGER, " +
                TEMP + " INTEGER, " +
                PRESSURE + " INTEGER, " +
                HUMIDITY + " INTEGER, " +
                TEMP_MIN + " INTEGER, " +
                TEMP_MAX + " INTEGER, " +
                RAIN_HOUR + " INTEGER, " +
                RAIN_VALUE + " INTEGER, " +
                WEATHER_ID + " INTEGER, " +
                DESCRIPTION + " TEXT, " +
                ICON + " TEXT);";
    }
}
