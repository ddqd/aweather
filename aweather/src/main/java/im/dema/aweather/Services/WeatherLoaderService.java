package im.dema.aweather.Services;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import im.dema.aweather.Models.CurrentWeatherModel;
import im.dema.aweather.Models.CurrentWeatherModelHelper;
import im.dema.aweather.Models.SearchCityModel;
import im.dema.aweather.R;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dema on 15.12.14.
 */
public class WeatherLoaderService extends IntentService {
    private static final String TAG = "WeatherLoaderService";
    private OkHttpClient mHttpclient;
    private static final String OWM_HOST = "http://api.openweathermap.org/data/2.5/weather?lang=ru&units=metric&id=";
    private static final String OWM_API_SEARCH_HOST = "http://dema.im:8080/api/search?name=";
    private Callback cityLoaderCallback;
    private List<JSONObject> result;
    private int needLoadDataCount = -1;
    private Realm realm;

    private static final int TASK_LOAD_ONE = 0;
    private static final int TASK_LOAD_DEFAULT_CITIES = 2;
    private static final int TASK_UPDATE_CURRENT_LIST = 3;
    private static final int TASK_DEFAULT = -1;

    public static final int TASK_SEARCH_CITY_ID_BY_NAME = 4;
    private static final String CITY_SEARCH_NAME = "city_search";

    private static final String CITY_ID = "city_id";
    public static final String TASK_TYPE = "task_type";

    public final static String PARAM_PENDING_INTENT = "pendingIntent";
    public final static String RESULT = "result";

    public static final int REQUEST_CODE_SEARCH_CITY_ID = 11;


    public WeatherLoaderService() {
        super(TAG);
        mHttpclient = new OkHttpClient();
        result =  Collections.synchronizedList(new ArrayList<JSONObject>());
        cityLoaderCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //TODO handle response
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() == 200) {
                    try {
                        String responseString =  response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        result.add(jsonObject);
                        checkLoadedData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       final int taskType = intent.getIntExtra(TASK_TYPE, TASK_DEFAULT);
        switch (taskType) {
            case TASK_LOAD_ONE:
                int cityId = intent.getIntExtra(CITY_ID, 0);
                if(cityId > 0) {
                    loadCurrentWeatherById(cityId);

                }
                break;
            case TASK_LOAD_DEFAULT_CITIES:
                loadCurrentWeatherForDC();
                break;
            case  TASK_UPDATE_CURRENT_LIST:
                updateCurrentList();
                break;
            case TASK_SEARCH_CITY_ID_BY_NAME:
                String searchName = intent.getStringExtra(CITY_SEARCH_NAME);
                PendingIntent pi = intent.getParcelableExtra(PARAM_PENDING_INTENT);
                searchCityByName(pi, searchName);
            default:
                break;
        }
    }

    private static String getUrlFromId(int id) {
        StringBuilder builder = new StringBuilder();
        builder.append(OWM_HOST);
        builder.append(id);
        return builder.toString();
    }

    private void loadCurrentWeatherById(final int cityId) {
        needLoadDataCount = 1;
        Request request = new Request.Builder()
                .url(getUrlFromId(cityId))
                .build();
        mHttpclient.newCall(request).enqueue(cityLoaderCallback);
    }

    private void loadCurrentWeatherForDC() {
        int[] cities  = getResources().getIntArray(R.array.default_cities);
        needLoadDataCount = cities.length;
        loadCurrentWeatherByList(cities);
    }

    private void loadCurrentWeatherByList(final int[] citiesArray) {
        for(int i = 0; i < citiesArray.length; i++) {
            final int cityId = citiesArray[i];
            loadCurrentWeatherById(cityId);
        }
    }

    private void updateCurrentList() {
        realm = Realm.getInstance(this);
        RealmResults<CurrentWeatherModel> results = realm.allObjects(CurrentWeatherModel.class);
        int[] citiesArrayId = new int[results.size()];
        for (int i = 0; i < results.size(); i++) {
            citiesArrayId[i] = results.get(i).getCityId();
        }
        loadCurrentWeatherByList(citiesArrayId);
        needLoadDataCount = citiesArrayId.length;
    }

    private void checkLoadedData() {
        if(needLoadDataCount == result.size()) {
            realm = Realm.getInstance(this);
            realm.beginTransaction();
            try {
                for(JSONObject jsonObject: result) {
                        CurrentWeatherModelHelper.addOrUpdate(jsonObject, realm);
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            realm.commitTransaction();
            needLoadDataCount = -1;
        }
    }

    private void searchCityByName(final PendingIntent pi, final String text) {
        Request request = new Request.Builder()
                .url(OWM_API_SEARCH_HOST + text)
                .build();
        mHttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.w(TAG, "fail");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() == 200) {
                    try {
                        Intent intent = new Intent();
                        ArrayList<SearchCityModel> items = SearchCityModel.parseFromResponse(response.body().string());
                        intent.putParcelableArrayListExtra(RESULT, items);
                        pi.send(WeatherLoaderService.this, Activity.RESULT_OK, intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void updateCurrentWeather(Context context) {
        Intent intent = new Intent(context, WeatherLoaderService.class);
        intent.putExtra(TASK_TYPE, TASK_UPDATE_CURRENT_LIST);
        context.startService(intent);
    }

    public static void loadDefaultCitiesList(Context context) {
        Intent intent = new Intent(context, WeatherLoaderService.class);
        intent.putExtra(TASK_TYPE, TASK_LOAD_DEFAULT_CITIES);
        context.startService(intent);
    }

    public static void loadCurrentWeatherById(Context context, final int cityId) {
        Intent intent = new Intent(context, WeatherLoaderService.class);
        intent.putExtra(TASK_TYPE, TASK_LOAD_ONE);
        intent.putExtra(CITY_ID, cityId);
        context.startService(intent);
    }

    public static void searchCityByName(Activity activity, final String name) {
        Intent intent = new Intent(activity, WeatherLoaderService.class)
                .putExtra(WeatherLoaderService.CITY_SEARCH_NAME, name);
        PendingIntent pi = activity.createPendingResult(REQUEST_CODE_SEARCH_CITY_ID, new Intent(), 0);
        intent.putExtra(WeatherLoaderService.PARAM_PENDING_INTENT, pi);
        intent.putExtra(WeatherLoaderService.TASK_TYPE, WeatherLoaderService.TASK_SEARCH_CITY_ID_BY_NAME);
        activity.startService(intent);
    }
}
