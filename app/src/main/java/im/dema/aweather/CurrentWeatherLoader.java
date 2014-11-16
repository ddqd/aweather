package im.dema.aweather;

import android.content.Context;

import com.pushtorefresh.bamboostorage.BambooStorage;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by dema on 14.11.14.
 */
public class CurrentWeatherLoader {
    private final OkHttpClient mHttpclient;
    private static final String baseUrl = "http://api.openweathermap.org/data/2.5/weather?lang=ru&units=metric&id=";
    private BambooStorage mBambooStorage;

    private static String getUrlFromId(int id) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl);
        builder.append(id);
        return builder.toString();
    }

    public CurrentWeatherLoader(Context context, BambooStorage bambooStorage) {
        this.mBambooStorage = bambooStorage;
        mHttpclient = new OkHttpClient();
    }

    public void loadCurrentWeather(final int cityId, final Callback callback) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(getUrlFromId(cityId))
                        .build();
                mHttpclient.newCall(request).enqueue(callback);
            }
        };
        r.run();
    }

    public void loadDefaultCities(final int[] defaultCities) {
        new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < defaultCities.length; i++) {
                    int cityId = defaultCities[i];
                    final Request request = new Request.Builder()
                            .url(getUrlFromId(cityId))
                            .build();
                    mHttpclient.newCall(request).enqueue(new Callback() {
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
                    });
                }
            }
        }.run();


    }
}
