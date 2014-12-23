package im.dema.aweather.Fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import eu.erikw.PullToRefreshListView;
import im.dema.aweather.Adapters.CurrentWeatherListViewAdapter;
import im.dema.aweather.Models.CurrentWeatherModel;
import im.dema.aweather.R;
import im.dema.aweather.Services.WeatherLoaderService;
import im.dema.aweather.Utils.SwipeDismissListViewTouchListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dema on 13.11.14.
 */
public class CurrentWeatherListFragment extends ListFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Realm realm;
    public static final String TAG = "CurrentWeatherListFragment";
    CurrentWeatherListViewAdapter mCurrentWeatherListAdapter;
    Button loadDefaultsButton;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        realm = Realm.getInstance(getActivity());
        RealmResults<CurrentWeatherModel> currentWeatherModels = realm.where(CurrentWeatherModel.class).findAll();
        mCurrentWeatherListAdapter = new CurrentWeatherListViewAdapter(getActivity(), R.layout.current_weather_list_fragment, currentWeatherModels, true);
        setListAdapter(mCurrentWeatherListAdapter);
        getListView().setOnItemClickListener(this);
        final PullToRefreshListView refreshListView = (PullToRefreshListView) getListView();
        refreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mCurrentWeatherListAdapter.getCount() > 0) {
                    WeatherLoaderService.updateCurrentWeather(getActivity());
                }
            }
        });
        final LinearLayout emptyView = (LinearLayout) getView().findViewById(R.id.current_weather_empty_list);
        final LinearLayout progressEmptyView = (LinearLayout) getView().findViewById(android.R.id.empty);
        if(currentWeatherModels.size() == 0) {
            WeatherLoaderService.loadDefaultCitiesList(getActivity());
            progressEmptyView.setVisibility(View.VISIBLE);
            getListView().setEmptyView(progressEmptyView);
            emptyView.setVisibility(View.GONE);
        } else {
            progressEmptyView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            getListView().setEmptyView(emptyView);
        }
        realm.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                refreshListView.onRefreshComplete();
            }
        });
        SwipeDismissListViewTouchListener touchListener =
                 new SwipeDismissListViewTouchListener(getListView(),
                         new SwipeDismissListViewTouchListener.OnDismissCallback() {
                                 public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                         for (int position : reverseSortedPositions) {
                                                 realm.beginTransaction();
                                                 RealmResults<CurrentWeatherModel> result = realm.where(CurrentWeatherModel.class).findAll();
                                                 result.remove(position-1);
                                                 if(result.size() == 0) {
                                                    progressEmptyView.setVisibility(View.GONE);
                                                    emptyView.setVisibility(View.VISIBLE);
                                                    getListView().setEmptyView(emptyView);
                                                 }
                                                 realm.commitTransaction();
                                            }
                                    }
                            });
        getListView().setOnTouchListener(touchListener);
        getListView().setOnScrollListener(touchListener.makeScrollListener());
        loadDefaultsButton = (Button) getView().findViewById(R.id.load_defaults);
        loadDefaultsButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_defaults:
                WeatherLoaderService.loadDefaultCitiesList(getActivity());
                break;
        }
    }
}
