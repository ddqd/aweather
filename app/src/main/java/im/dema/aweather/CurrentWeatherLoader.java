package im.dema.aweather;

import com.pushtorefresh.bamboostorage.BambooStorage;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by dema on 14.11.14.
 */
public class CurrentWeatherLoader {
    private final OkHttpClient mHttpclient;
    private static final String baseUrl = "http://api.openweathermap.org/data/2.5/weather?lang=ru&units=metric&id=";
    private BambooStorage mBambooStorage;
    private Callback loaderCallback;

    private static String getUrlFromId(int id) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl);
        builder.append(id);
        return builder.toString();
    }

    public CurrentWeatherLoader(BambooStorage bambooStorage) {
        this.mBambooStorage = bambooStorage;
        mHttpclient = new OkHttpClient();
        loaderCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        CurrentWeatherStorableItem item = new CurrentWeatherStorableItem();
                        item.parseFromJson(jsonObject);
                        mBambooStorage.add(item);
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
        int citiesCount = mBambooStorage.countOfItems(CurrentWeatherStorableItem.class);
        int[] citiesArrayId = new int[citiesCount];
        List<CurrentWeatherStorableItem> items = mBambooStorage.getAsList(CurrentWeatherStorableItem.class);
        for(int i = 0; i < citiesCount; ) {
            citiesArrayId[i] = items.get(i).cityId;
        }
        loadCitiesByArrayId(citiesArrayId);
    }
}
