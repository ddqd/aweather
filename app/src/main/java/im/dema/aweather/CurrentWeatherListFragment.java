package im.dema.aweather;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pushtorefresh.bamboostorage.BambooStorage;
import com.pushtorefresh.bamboostorage.IBambooStorableItem;

import java.util.List;

/**
 * Created by dema on 13.11.14.
 */
public class CurrentWeatherListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private CurrentWeatherListViewAdapter adapter;
    private LayoutInflater layoutInflater;
    private BambooStorage mCurrentWeatherStorage;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCurrentWeatherStorage = new BambooStorage(getActivity(), "im.dema.aweather.current_weather");
        List<CurrentWeatherStorableItem> currentWeatherStorableItems = mCurrentWeatherStorage.getAsList(CurrentWeatherStorableItem.class);
        adapter = new CurrentWeatherListViewAdapter(getActivity(), currentWeatherStorableItems);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.current_weather_list_fragment, null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
