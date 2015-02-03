package im.dema.aweather.Models;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import im.dema.aweather.R;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dema on 14.12.14.
 */
public class CurrentWeatherModelHelper {
    private static final String iconBaseURL = "http://openweathermap.org/img/w/";
    private static final String iconExtension = ".png";

    public static void createWeatherModelFromJson (JSONObject jsonObject, Realm realm) throws JSONException {
        CurrentWeatherModel currentWeatherModel = realm.createObject(CurrentWeatherModel.class);
        updateWeatherModel(jsonObject, currentWeatherModel);
    }

    public static CurrentWeatherModel updateWeatherModel (JSONObject item, CurrentWeatherModel currentWeatherModel) throws JSONException {
        currentWeatherModel.setCityId(item.getInt("id"));
        currentWeatherModel.setCityName(item.getString("name"));
        JSONObject coord = item.getJSONObject("coord");
        currentWeatherModel.setLat(coord.getDouble("lat"));
        currentWeatherModel.setLon(coord.getDouble("lon"));
        currentWeatherModel.setTimeStamp(item.getInt("dt"));

        JSONObject sys = item.getJSONObject("sys");
        currentWeatherModel.setCountry(sys.getString("country"));

        JSONObject cloudsJson = item.getJSONObject("clouds");
        currentWeatherModel.setClouds(cloudsJson.getInt("all"));

        JSONObject wind = item.getJSONObject("wind");
        currentWeatherModel.setWindSpeed(wind.getInt("speed"));
        currentWeatherModel.setWindDeg(wind.getInt("deg"));

        JSONObject main = item.getJSONObject("main");
        currentWeatherModel.setTemp(main.getInt("temp"));
        currentWeatherModel.setPressure(main.getInt("pressure"));
        currentWeatherModel.setHumidity(main.getInt("humidity"));
        currentWeatherModel.setTemp_min(main.getInt("temp_min"));
        currentWeatherModel.setTemp_max(main.getInt("temp_max"));

        JSONArray weatherArray = item.getJSONArray("weather");
        JSONObject weatherItem = weatherArray.getJSONObject(0);
        currentWeatherModel.setWeatherId(weatherItem.getInt("id"));
        currentWeatherModel.setDescription(weatherItem.getString("description"));
        currentWeatherModel.setIcon(getIconUrl(weatherItem.getString("icon")));
        JSONObject rain = item.optJSONObject("rain");
        int rainHour = 0;
        int rainValue = 0;
        if(rain != null) {
            if (rain.has("1h")) {
                rainHour = 1;
                rainValue = rain.getInt("1h");
            } else if (rain.has("3h")) {
                rainHour = 3;
                rainValue = rain.getInt("3h");
            }
        }
        currentWeatherModel.setRainHour(rainHour);
        currentWeatherModel.setRainValue(rainValue);
        return currentWeatherModel;
    }

    private static String getIconUrl (String iconUrl) {
        StringBuilder builder = new StringBuilder();
        builder.append(iconBaseURL);
        builder.append(iconUrl);
        builder.append(iconExtension);
        return builder.toString();
    }

    public static CurrentWeatherModel getContains(JSONObject jsonObject, Realm realm) {
        if(jsonObject.has("id")) {
            int cityId = jsonObject.optInt("id");
            if (cityId != 0) {
                RealmResults<CurrentWeatherModel> result = realm.where(CurrentWeatherModel.class)
                        .equalTo("cityId", cityId)
                        .findAll();
                if (result.size() > 0) {
                    return result.first();
                }
            }
        }
        return null;
    }

    public static void addOrUpdate(JSONObject item, Realm realm) throws JSONException{
        CurrentWeatherModel model = getContains(item, realm);
        if(model != null) {
            updateWeatherModel(item, model);
        } else {
            createWeatherModelFromJson(item, realm);
        }
    }

    public static int getIconByWeatherId(Context context, int weatherId) {

        return 0;
    }
}
