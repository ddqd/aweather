package im.dema.aweather;

import com.pushtorefresh.bamboostorage.BambooStorage;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dema on 09.11.14.
 */
public class CitiesLoader {
    private final OkHttpClient mHttpclient;
    private BambooStorage mCitiesStorage;
    private final String url = "http://openweathermap.org/help/city_list.txt";
    public CitiesLoader(BambooStorage storage) {
        mHttpclient = new OkHttpClient();
        mCitiesStorage = storage;
    }

    public void getCities() {
       Runnable r = new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                mHttpclient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        parseCitiesList(response.body().string());

                    }
                });
            }
        };
        r.run();
    }

    public void parseCitiesList(String citiesString) {
        ArrayList<CityStorableItem> citiesList = new ArrayList<CityStorableItem>();
        if(citiesString.contains("countryCode")) {
            citiesString = citiesString.substring(citiesString.indexOf('\n') + 1);
        }
        for (String token : citiesString.split("\\n")) {
            CityStorableItem item = new CityStorableItem(token);
            citiesList.add(item);
        }
        writeToStorage(citiesList);
    }

    public void writeToStorage(ArrayList<CityStorableItem> citiesList) {
        mCitiesStorage.addOrUpdateAll(citiesList);
    }
}
