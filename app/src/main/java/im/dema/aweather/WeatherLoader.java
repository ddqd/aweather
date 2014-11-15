package im.dema.aweather;

import android.content.Context;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * Created by dema on 14.11.14.
 */
public class WeatherLoader {
    private final OkHttpClient mHttpclient;
    private static final String url = "http://api.openweathermap.org/data/2.5/weather?q=London";

    public WeatherLoader(Context context) {
        mHttpclient = new OkHttpClient();
    }

    public void loadCurrentWeather(int cityId, final Callback callback) {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                Request request = new Request.Builder()
                        .url(url)
                        .build();
                mHttpclient.newCall(request).enqueue(callback);
            }
        };
        r.run();
    }
}
