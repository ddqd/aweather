package im.dema.aweather;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;

import com.pushtorefresh.bamboostorage.BambooStorage;

import java.util.ArrayList;

/**
 * Created by dema on 12.11.14.
 */
public class CitiesListFragment extends ListFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final String TAG = "CitiesListFragment";

    CitiesListViewAdapter adapter;
    BambooStorage mBamboostorage;
    int citiesLoadedCount = 1;
    private static final int CITIES_LOAD_PER_PAGE = 20;
    public ArrayList<CityStorableItem> items;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemClickListener(this);
        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
                    if(loadCities()) {
                        getListView().setSelection(20);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        items = new ArrayList<CityStorableItem>();
        mBamboostorage = new BambooStorage(getActivity(), "im.dema.aweather.cities");
        adapter = new CitiesListViewAdapter(getActivity(), items);
        setListAdapter(adapter);
        loadCities();
        reloadCitiesList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cities_fragment, null);
        Button emptyListButton = (Button) view.findViewById(android.R.id.empty);
        emptyListButton.setOnClickListener(this);
        return view;
    }

    private boolean loadCities() {
        int totalCities = mBamboostorage.countOfItems(CityStorableItem.class);
        int loadChunk = CITIES_LOAD_PER_PAGE;
        if(citiesLoadedCount >= totalCities) {
            return false;
        }
        if((totalCities - citiesLoadedCount) > CITIES_LOAD_PER_PAGE) {
            loadChunk = CITIES_LOAD_PER_PAGE;
        }
        String req = "("+ CityStorableItem.TableInfo.ID + " >= ? AND " + CityStorableItem.TableInfo.ID + " <= ? )";
        items.addAll(mBamboostorage.getAsList(CityStorableItem.class, req, new String[]{String.valueOf(citiesLoadedCount), String.valueOf(citiesLoadedCount+loadChunk)}, CityStorableItem.TableInfo.CITY_NAME));
        citiesLoadedCount+=loadChunk;
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CityStorableItem selectedItem = items.get(i);
        if(selectedItem == null) {
            return;
        }
        CurrentWeatherListFragment currentWeatherFragment = (CurrentWeatherListFragment)
                getFragmentManager().findFragmentByTag(CurrentWeatherListFragment.TAG);
        if (currentWeatherFragment != null) {
            currentWeatherFragment.addCity(selectedItem.getCityId());
        } else {
            currentWeatherFragment = new CurrentWeatherListFragment();
            Bundle args = new Bundle();
            args.putInt(CurrentWeatherListFragment.ARG_NEW_CITY, selectedItem.getCityId());
            currentWeatherFragment.setArguments(args);
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.container, currentWeatherFragment, CurrentWeatherListFragment.TAG)
                .commit();
    }

    public void reloadCitiesList() {
        if(mBamboostorage.countOfItems(CityStorableItem.class) == 0) {
            final CitiesLoader loader = new CitiesLoader(mBamboostorage);
            loader.getCities();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.empty:
                reloadCitiesList();
                break;
        }
    }

}
