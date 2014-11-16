package im.dema.aweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by dema on 11.11.14.
 */
public class CitiesListViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<CityStorableItem> items;

    CitiesListViewAdapter(Context context, List<CityStorableItem> items) {
        layoutInflater = LayoutInflater.from(context);
        this.items = items;
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
        view = layoutInflater.inflate(R.layout.cities_item, null);
        TextView cityName = (TextView)view.findViewById(R.id.cities_item_name);
        CityStorableItem item = (CityStorableItem) getItem(i);
        cityName.setText(item.getCityName());
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
