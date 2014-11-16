package im.dema.aweather;

import android.test.AndroidTestCase;

import com.pushtorefresh.bamboostorage.BambooStorage;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by dema on 09.11.14.
 */
public class CitiesStorageTest extends AndroidTestCase {
    private BambooStorage mCitiesStorage;

    @Override
    public void setUp() {
        mCitiesStorage = new BambooStorage(getContext(), "im.dema.aweather.cities");
        cleanStorage();
    }

    private void cleanStorage() {
        mCitiesStorage.removeAllOfType(CityStorableItem.class);
        assertEquals(0, mCitiesStorage.countOfItems(CityStorableItem.class));
    }

    private CityStorableItem createTestCity() {
        CityStorableItem city = new CityStorableItem();
        city.setCityId(9339);
        city.setCityName("Asdfhgfh");
        city.setCountryCode("RU");
        city.setLat(44.111);
        city.setLon(40.1111);
        return city;
    }

    public void testCitiesStorage() {
        CityStorableItem city = createTestCity();
        mCitiesStorage.add(city);
        assertTrue(mCitiesStorage.contains(city));
    }

    public void testCitiesLoader() {
//        final CountDownLatch signal = new CountDownLatch(1);
//        final CitiesLoader loader = new CitiesLoader(getContext());
//        try {
//            loader.getCities(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    signal.countDown();
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    String result =  response.body().string();
//                    assertTrue(result.contains("Moscow"));
//                    assertFalse(result.isEmpty());
//                    signal.countDown();
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            signal.await(30, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void testParseCitiesList() {
//        final CitiesLoader loader = new CitiesLoader(getContext());
//        String exampleCitiesList = "819827\tRazvilka\t55.591667\t37.740833\tRU\n" +
//                                    "524901\tMoscow\t55.752220\t37.615555\tRU\n" +
//                                    "1271881\tFirozpur Jhirka\t27.799999\t76.949997\tIN";
//        loader.parseCitiesList(exampleCitiesList);
//        assertEquals(3, mCitiesStorage.countOfItems(CityStorableItem.class));
//        assertEquals(1271881, mCitiesStorage.getLast(CityStorableItem.class).getInternalId());
    }
}
