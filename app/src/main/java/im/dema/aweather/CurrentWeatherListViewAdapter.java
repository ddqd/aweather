package im.dema.aweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pushtorefresh.bamboostorage.IBambooStorableItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dema on 13.11.14.
 */
public class CurrentWeatherListViewAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<CurrentWeatherStorableItem> items;
    private Context context;

    CurrentWeatherListViewAdapter(Context context, List<CurrentWeatherStorableItem> items) {
        this.items = items;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CurrentWeatherStorableItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).getInternalId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CurrentWeatherStorableItem item = getItem(i);
        view = layoutInflater.inflate(R.layout.current_weather_item, null);
        ImageView iconView = (ImageView) view.findViewById(R.id.current_weather_icon);
        Picasso.with(context).load(item.getIconUrl()).into(iconView);
        return view;
    }
}
