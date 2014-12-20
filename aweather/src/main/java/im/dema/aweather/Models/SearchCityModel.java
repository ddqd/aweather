package im.dema.aweather.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dema on 15.12.14.
 */
public class SearchCityModel implements Parcelable {
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

    public SearchCityModel() {

    }

    public static ArrayList<SearchCityModel> parseFromResponse(String responseString) throws JSONException {
        ArrayList<SearchCityModel> resultCitiesArray = new ArrayList<>();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(countryCode);
        dest.writeInt(id);
    }

    private SearchCityModel(Parcel in) {
        name = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        countryCode = in.readString();
        id = in.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SearchCityModel createFromParcel(Parcel in) {
            return new SearchCityModel(in);
        }

        public SearchCityModel[] newArray(int size) {
            return new SearchCityModel[size];
        }
    };
}
