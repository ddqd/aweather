package im.dema.aweather.Fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.badoo.mobile.util.WeakHandler;

import im.dema.aweather.Adapters.CurrentWeatherListViewAdapter;
import im.dema.aweather.CurrentWeatherLoader;
import im.dema.aweather.Models.CurrentWeatherModel;
import im.dema.aweather.R;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dema on 13.11.14.
 */
public class CurrentWeatherListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private WeakHandler handler;
    private Realm realm;
    public static final String TAG = "CurrentWeatherListFragment";
    CurrentWeatherLoader loader;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        realm = Realm.getInstance(getActivity());
        RealmResults<CurrentWeatherModel> currentWeatherModels = realm.where(CurrentWeatherModel.class).findAll();
        handler = new WeakHandler();
        final CurrentWeatherListViewAdapter adapter = new CurrentWeatherListViewAdapter(getActivity(), R.layout.current_weather_list_fragment, currentWeatherModels, true);
        setListAdapter(adapter);
        loader = new CurrentWeatherLoader(getActivity());
        getListView().setOnItemClickListener(this);
        if(currentWeatherModels.size() == 0) {
            loader.loadDefaultCities();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
        SearchCitiesFragment searchCitiesFragment;
        searchCitiesFragment = (SearchCitiesFragment) getFragmentManager().findFragmentByTag(SearchCitiesFragment.TAG);
        if(searchCitiesFragment ==null) {
            searchCitiesFragment = new SearchCitiesFragment();
        }
        getFragmentManager().beginTransaction()
           .replace(R.id.container, searchCitiesFragment, SearchCitiesFragment.TAG)
           .addToBackStack(null)
           .commit();
        return true;
        } else if( id == R.id.action_reload) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loader.updateCurrentList();
                }
            });
            return true;
        }
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
        loader.loadCurrentWeather(cityId);
    }
}
