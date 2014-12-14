package im.dema.aweather.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.dema.aweather.Models.SearchCityModel;
import im.dema.aweather.R;

/**
 * Created by dema on 15.12.14.
 */
public class SearchCitiesAdapter extends ArrayAdapter<SearchCityModel> {
    private LayoutInflater inflater;

    private static class ViewHolder {
        TextView name;
        TextView countryCode;
    }

    public SearchCitiesAdapter(Context context, int resource, List<SearchCityModel> items) {
        super(context, resource, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        view = inflater.inflate(R.layout.search_cities_item, null);
        viewHolder.name = (TextView) view.findViewById(R.id.city_name);
        viewHolder.countryCode = (TextView) view.findViewById(R.id.country_code);
        SearchCityModel model = getItem(position);
        viewHolder.name.setText(model.getName());
        viewHolder.countryCode.setText(model.getCountryCode());
        return view;
    }
}
