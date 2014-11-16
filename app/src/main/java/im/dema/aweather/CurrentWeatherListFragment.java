package im.dema.aweather;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pushtorefresh.bamboostorage.ABambooStorageListener;
import com.pushtorefresh.bamboostorage.BambooStorage;
import com.pushtorefresh.bamboostorage.IBambooStorableItem;

/**
 * Created by dema on 13.11.14.
 */
public class CurrentWeatherListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private CurrentWeatherListViewAdapter adapter;
    private BambooStorage mCurrentWeatherStorage;
    private ABambooStorageListener storageListener;

    public static final String TAG = "CurrentWeatherListFragment";

    public static final String ARG_NEW_CITY = "add_new_city";

    CurrentWeatherLoader loader;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mCurrentWeatherStorage = new BambooStorage(getActivity(), "im.dema.aweather.current_weather");
        adapter = new CurrentWeatherListViewAdapter(getActivity(), mCurrentWeatherStorage);
        setListAdapter(adapter);
        loader = new CurrentWeatherLoader(mCurrentWeatherStorage);
        getListView().setOnItemClickListener(this);
        storageListener = new ABambooStorageListener() {
            @Override
            public void onAdd(@NonNull IBambooStorableItem storableItem) {
                super.onAdd(storableItem);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onRemove(@NonNull IBambooStorableItem storableItem, int count) {
                adapter.notifyDataSetChanged();
                super.onRemove(storableItem, count);
            }

            @Override
            public void onUpdate(@NonNull IBambooStorableItem storableItem, int count) {
                adapter.notifyDataSetChanged();
                super.onUpdate(storableItem, count);
            }
        };

        mCurrentWeatherStorage.addListener(storageListener);

        if(mCurrentWeatherStorage.countOfItems(CurrentWeatherStorableItem.class) == 0) {
            loader.loadCitiesByArrayId(getActivity().getResources().getIntArray(R.array.default_cities));
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
            CitiesListFragment citiesListFragment;
            citiesListFragment = (CitiesListFragment) getFragmentManager().findFragmentByTag(CitiesListFragment.TAG);
            if(citiesListFragment ==null) {
                citiesListFragment = new CitiesListFragment();
            }
            getFragmentManager().beginTransaction()
                   .replace(R.id.container, citiesListFragment, CitiesListFragment.TAG)
                   .addToBackStack(null)
                   .commit();
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
