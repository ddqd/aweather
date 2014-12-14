package im.dema.aweather.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dema on 15.12.14.
 */
public class SearchCityModel {
    private String name;
    private double lat;
    private double lon;
    private String countryCode;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public static List<SearchCityModel> parseFromResponse(String responseString) throws JSONException {
        List<SearchCityModel> resultCitiesArray = new ArrayList<>();
        JSONObject responseJson = new JSONObject(responseString);
        JSONArray result = responseJson.getJSONArray("result");
        for (int i = 0; i < result.length(); i++) {
            JSONObject jsonCityModel = result.getJSONObject(i);
            SearchCityModel cityModel = new SearchCityModel();
            cityModel.setName(jsonCityModel.getString("name"));
            cityModel.setId(jsonCityModel.getInt("id"));
            cityModel.setLat(jsonCityModel.getDouble("lat"));
            cityModel.setLon(jsonCityModel.getDouble("lon"));
            cityModel.setCountryCode(jsonCityModel.getString("countryCode"));
            resultCitiesArray.add(cityModel);
        }
        return resultCitiesArray;
    }

}
