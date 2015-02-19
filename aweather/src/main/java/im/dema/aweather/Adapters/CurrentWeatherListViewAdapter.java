package im.dema.aweather.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import im.dema.aweather.Models.CurrentWeatherModel;
import im.dema.aweather.R;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by dema on 13.11.14.
 */
public class CurrentWeatherListViewAdapter extends RealmBaseAdapter<CurrentWeatherModel> implements ListAdapter {
    private Context context;

    private static class ViewHolder {
        ImageView iconView;
        TextView cityName;
        TextView degress;
    }

    public CurrentWeatherListViewAdapter(Context context, int resId, RealmResults<CurrentWeatherModel> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        this.context = context;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.current_weather_fragment_item, null);
            viewHolder.iconView = (ImageView) view.findViewById(R.id.current_weather_icon);
            viewHolder.cityName = (TextView) view.findViewById(R.id.current_weather_city_name);
            viewHolder.degress = (TextView) view.findViewById(R.id.current_weather_degrees);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        CurrentWeatherModel item = realmResults.get(position);
        viewHolder.cityName.setText(item.getCityName());
        Picasso.with(context).load(item.getIcon()).into(viewHolder.iconView);
        viewHolder.degress.setText(item.getTemp() + " \u2103"); //degrees symbol code
        return view;
    }
    @Override
    public void updateRealmResults(RealmResults<CurrentWeatherModel> realmResults) {
        super.updateRealmResults(realmResults);
    }
}
