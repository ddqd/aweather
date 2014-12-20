package im.dema.aweather.Popups;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import im.dema.aweather.Adapters.SearchCitiesAdapter;
import im.dema.aweather.Models.SearchCityModel;
import im.dema.aweather.R;
import im.dema.aweather.Services.WeatherLoaderService;

/**
 * Created by dema on 20.12.14.
 */
public class SearchResultWindow extends PopupWindow implements View.OnTouchListener{
    private View mView;
    private Context mContext;
    SearchCitiesAdapter mSearchAdapter;
    List<SearchCityModel> mSearchResult;
    ListView mSearchListView;

    public SearchResultWindow(Context context, int screenHeight) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight/2);
        this.setAnimationStyle(R.style.AnimationPopup);
        this.mContext = context;
        mSearchResult = new ArrayList<>();
        setBackgroundDrawable(new BitmapDrawable());
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.popup_search_result, null);
        this.setContentView(mView);
        mSearchAdapter = new SearchCitiesAdapter(mContext, R.layout.popup_search_result, mSearchResult);
        mSearchListView = (ListView) mView.findViewById(R.id.cities_search_listview);
        View emptyView = mView.findViewById(R.id.cities_search_empty);
        mSearchListView.setEmptyView(emptyView);
        mSearchListView.setAdapter(mSearchAdapter);
        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherLoaderService.loadCurrentWeatherById(mContext, mSearchResult.get(position).getId());
                dismiss();
            }
        });
        mSearchListView.setOnTouchListener(this);
    }

    public void showResult(ArrayList<SearchCityModel> result) {
        if(!isShowing()) {
            TypedValue tv = new TypedValue();
            int statusBarHeight = (int) Math.ceil(25 * mContext.getResources().getDisplayMetrics().density);
            int actionBarHeight = -1;
            if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
            }
            showAtLocation(mView, Gravity.TOP, 0, actionBarHeight + statusBarHeight);
        }
        mSearchResult.clear();
        mSearchResult.addAll(result);
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(isFocusable()) {
            setFocusable(false);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!isFocusable()) {
            setFocusable(true);
            update();
        }
        return mSearchListView.onTouchEvent(event);
    }
}
