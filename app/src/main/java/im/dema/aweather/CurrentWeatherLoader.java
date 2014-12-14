package im.dema.aweather;

import android.content.Context;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import im.dema.aweather.Models.CurrentWeatherModel;
import im.dema.aweather.Models.CurrentWeatherModelHelper;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dema on 14.11.14.
 */
public class CurrentWeatherLoader {
    private OkHttpClient mHttpclient;
    private static final String baseUrl = "http://api.openweathermap.org/data/2.5/weather?lang=ru&units=metric&id=";
    private Callback loaderCallback;
    Context context;
    private List<JSONObject> result;
    int needLoadDataCount = -1;
    Realm realm;

    private static String getUrlFromId(int id) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl);
        builder.append(id);
        return builder.toString();
    }

    public CurrentWeatherLoader(final Context context) {
        this.context = context;
        mHttpclient = new OkHttpClient();
        result = new ArrayList<JSONObject>();
        loaderCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        result.add(jsonObject);
                        checkLoadedData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void loadCurrentWeather(final int cityId) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(getUrlFromId(cityId))
                        .build();
                mHttpclient.newCall(request).enqueue(loaderCallback);
            }
        };
        r.run();
    }

    public void loadDefaultCities() {
        int[] cities  = context.getResources().getIntArray(R.array.default_cities);
        needLoadDataCount = cities.length;
        loadCitiesByArrayId(cities);
    }

    public void loadCitiesByArrayId(final int[] citiesArray) {
        new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < citiesArray.length; i++) {
                    int cityId = citiesArray[i];
                    final Request request = new Request.Builder()
                            .url(getUrlFromId(cityId))
                            .build();
                    mHttpclient.newCall(request).enqueue(loaderCallback);
                }
            }
        }.run();
    }

    public void updateCurrentList() {
        realm = Realm.getInstance(context);
        RealmResults<CurrentWeatherModel> results = realm.allObjects(CurrentWeatherModel.class);
        int[] citiesArrayId = new int[results.size()];
        for (int i = 0; i < results.size(); i++) {
           citiesArrayId[i] = results.get(i).getCityId();
        }
        loadCitiesByArrayId(citiesArrayId);
        needLoadDataCount = citiesArrayId.length;
    }

    private void checkLoadedData() {
        if(needLoadDataCount == result.size()) {
            realm = Realm.getInstance(context);
            realm.beginTransaction();
            for(JSONObject jsonObject: result) {
                try {
                    CurrentWeatherModelHelper.addOrUpdate(jsonObject, realm);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            realm.commitTransaction();
            needLoadDataCount = -1;
        }
    }
}
