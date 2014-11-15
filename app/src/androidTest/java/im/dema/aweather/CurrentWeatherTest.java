package im.dema.aweather;

import android.test.AndroidTestCase;

import com.pushtorefresh.bamboostorage.BambooStorage;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by dema on 14.11.14.
 */
public class CurrentWeatherTest extends AndroidTestCase {
    private WeatherLoader loader;
    private BambooStorage mCurrentWeatherStorage;
    //http://api.openweathermap.org/data/2.5/weather?q=Rostov-on-Don&lang=ru&units=metric
    private static String result = "{\"coord\":{\"lon\":39.72,\"lat\":47.23},\"sys\":{\"type\":1,\"id\":7301,\"message\":0.0319,\"country\":\"Russia\",\"sunrise\":1416025536,\"sunset\":1416059157},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"пасмурно\",\"icon\":\"04d\"}],\"base\":\"cmc stations\",\"main\":{\"temp\":10,\"pressure\":1020,\"humidity\":76,\"temp_min\":10,\"temp_max\":10},\"wind\":{\"speed\":8,\"deg\":90,\"gust\":13},\"clouds\":{\"all\":75},\"dt\":1416043800,\"id\":501175,\"name\":\"Rostov-on-Don\",\"cod\":200}";

    @Override
    public void setUp() {
        loader = new WeatherLoader(getContext());
        mCurrentWeatherStorage = new BambooStorage(getContext(), "im.dema.aweather.current_weather");
        cleanStorage();
    }


    private void cleanStorage() {
        mCurrentWeatherStorage.removeAllOfType(CurrentWeatherStorableItem.class);
        assertEquals(0, mCurrentWeatherStorage.countOfItems(CurrentWeatherStorableItem.class));
    }

    public void testCurrentWeatherGet() {
        final CountDownLatch signal = new CountDownLatch(1);
        loader.loadCurrentWeather(501175, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                assertTrue(false);
                signal.countDown();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                assertTrue(response.code() == 200);
                signal.countDown();
            }
        });
        try {
            signal.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testResponse() {
        assertTrue(result != null);
        assertFalse(result.isEmpty());
    }

    public void testCurrentWeatherJsonParse() {
        CurrentWeatherStorableItem weather = null;
        try {
            JSONObject currentWeatherJson = new JSONObject(result);
            weather = new CurrentWeatherStorableItem();
            weather.parseFromJson(currentWeatherJson);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            assertTrue(weather != null);
            assertEquals(weather.id, 501175);
            assertEquals(weather.name, "Rostov-on-Don");
            assertEquals(weather.temp, 10);
            assertEquals(weather.clouds, 75);
            assertEquals(weather.description, "пасмурно");
        }
    }

    public void testCurrentWeatherWriteStorage() {
        JSONObject currentWeatherJson = null;
        try {
            currentWeatherJson = new JSONObject(result);
            CurrentWeatherStorableItem currentWeatherStorableItem = new CurrentWeatherStorableItem();
            currentWeatherStorableItem.parseFromJson(currentWeatherJson);
            mCurrentWeatherStorage.add(currentWeatherStorableItem);
            assertEquals(1, mCurrentWeatherStorage.countOfItems(CurrentWeatherStorableItem.class));
            assertEquals(0, mCurrentWeatherStorage.getFirst(CurrentWeatherStorableItem.class).id, 501175);
            assertTrue(mCurrentWeatherStorage.contains(currentWeatherStorableItem));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
