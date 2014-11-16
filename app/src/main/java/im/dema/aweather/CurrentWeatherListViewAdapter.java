package im.dema.aweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushtorefresh.bamboostorage.BambooStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dema on 13.11.14.
 */
public class CurrentWeatherListViewAdapter extends BaseAdapter {
    private BambooStorage storage;
    private LayoutInflater layoutInflater;
    private Context context;
    private List<CurrentWeatherStorableItem> items;

    CurrentWeatherListViewAdapter(Context context, BambooStorage storage) {
        items = storage.getAsList(CurrentWeatherStorableItem.class);
        this.storage = storage;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).getInternalId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CurrentWeatherStorableItem item = (CurrentWeatherStorableItem) getItem(i);
        view = layoutInflater.inflate(R.layout.current_weather_fragment, null);
        ImageView iconView = (ImageView) view.findViewById(R.id.current_weather_icon);
        Picasso.with(context).load(item.getIconUrl()).into(iconView);
        TextView cityName = (TextView) view.findViewById(R.id.current_weather_city_name);
        cityName.setText(item.cityName);
        TextView degress = (TextView) view.findViewById(R.id.current_weather_degrees);
        degress.setText(item.temp + " \u2103"); //degrees symbol code
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        items = storage.getAsList(CurrentWeatherStorableItem.class);
        super.notifyDataSetChanged();
    }
}
