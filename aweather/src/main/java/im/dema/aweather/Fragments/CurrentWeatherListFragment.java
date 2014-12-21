package im.dema.aweather.Fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import eu.erikw.PullToRefreshListView;
import im.dema.aweather.Adapters.CurrentWeatherListViewAdapter;
import im.dema.aweather.Models.CurrentWeatherModel;
import im.dema.aweather.R;
import im.dema.aweather.Services.WeatherLoaderService;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dema on 13.11.14.
 */
public class CurrentWeatherListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private Realm realm;
    public static final String TAG = "CurrentWeatherListFragment";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        realm = Realm.getInstance(getActivity());
        RealmResults<CurrentWeatherModel> currentWeatherModels = realm.where(CurrentWeatherModel.class).findAll();
        final CurrentWeatherListViewAdapter adapter = new CurrentWeatherListViewAdapter(getActivity(), R.layout.current_weather_list_fragment, currentWeatherModels, true);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        final PullToRefreshListView refreshListView = (PullToRefreshListView) getListView();
        refreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WeatherLoaderService.updateCurrentWeather(getActivity());
            }
        });
        if(currentWeatherModels.size() == 0) {
            WeatherLoaderService.loadDefaultCitiesList(getActivity());
        }
        realm.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                refreshListView.onRefreshComplete();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.current_weather_list_fragment, null);
        }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void addCity(int cityId) {
        WeatherLoaderService.loadCurrentWeatherById(getActivity(), cityId);
    }
}
