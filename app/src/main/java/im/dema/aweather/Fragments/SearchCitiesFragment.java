package im.dema.aweather.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.badoo.mobile.util.WeakHandler;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import im.dema.aweather.Adapters.SearchCitiesAdapter;
import im.dema.aweather.Models.SearchCityModel;
import im.dema.aweather.R;

public class SearchCitiesFragment extends Fragment implements SearchView.OnQueryTextListener {
    public static final String TAG = "SearchCitiesFragment";
    private static String OWM_API_SEARCH_HOST = "http://dema.im:8080/api/search?name=";

    private Callback mHttpCallback;
    private List<SearchCityModel> items;
    private OkHttpClient mHttpClient;
    WeakHandler handler;
    SearchCitiesAdapter adapter;
    public SearchCitiesFragment() {
        handler = new WeakHandler();
        items = new ArrayList<>();
        mHttpClient = new OkHttpClient();
        mHttpCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //todo handle failure request
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() == 200) {
                    try {
                        items.clear();
                        items.addAll(SearchCityModel.parseFromResponse(response.body().string()));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_cities_fragment, null);
        SearchView searchView = (SearchView) view.findViewById(R.id.cities_search_searchview);
        ListView listView = (ListView) view.findViewById(R.id.cities_search_listview);
        searchView.setOnQueryTextListener(this);
        adapter = new SearchCitiesAdapter(getActivity(), R.layout.search_cities_item, items);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length() >= 3) {
            searchCities(query);
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length() >= 3) {
            searchCities(newText);
            return true;
        }
        return false;
    }

    private void searchCities(final String text) {
        mHttpClient.cancel(TAG);
        handler.post(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(OWM_API_SEARCH_HOST + text)
                        .tag(TAG)
                        .build();
                mHttpClient.newCall(request).enqueue(mHttpCallback);
            }
        });
    }
}
