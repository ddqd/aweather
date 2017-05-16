package im.dema.aweather;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SearchView;

import java.util.ArrayList;

import im.dema.aweather.Fragments.CurrentWeatherListFragment;
import im.dema.aweather.Models.SearchCityModel;
import im.dema.aweather.Popups.SearchResultWindow;
import im.dema.aweather.Services.WeatherLoaderService;

public class MainActivity extends Activity implements SearchView.OnQueryTextListener, PopupWindow.OnDismissListener {
    private static final String TAG = "MainActivity";
    private static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT_TAG";
    SearchResultWindow searchResultWindow;
    int mContainer;
    String mCurrentFragmentTag;
    SearchView searchView;
    View.OnClickListener closeSearchViewListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = R.id.container;
        setContentView(R.layout.activity_main);
        closeSearchViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearchView();
            }
        };
        if (savedInstanceState != null) {
            mCurrentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT);
            loadFragmentByTag(mCurrentFragmentTag, true);
        } else {
            loadFragmentByTag(CurrentWeatherListFragment.TAG, false);
        }
        searchResultWindow = new SearchResultWindow(MainActivity.this, getResources().getDisplayMetrics().heightPixels);
        searchResultWindow.setOnDismissListener(this);
    }

    public boolean loadFragmentByTag(final String tag, boolean appendToBackStack) {
        if (tag == null || tag.isEmpty()) {
            return false;
        }
        final FragmentManager fm = getFragmentManager();
        Fragment tmpFragment = fm.findFragmentByTag(tag);
        if (tmpFragment != null) {
            return false;
        }
        if (tag.equals(CurrentWeatherListFragment.TAG)) {
            if (tmpFragment == null) {
                tmpFragment = new CurrentWeatherListFragment();
            }
        }
        if (appendToBackStack) {
            fm.beginTransaction()
                    .replace(mContainer, tmpFragment, tag)
                    .addToBackStack(tag)
                    .commit();
        } else {
            fm.beginTransaction()
                    .replace(mContainer, tmpFragment, tag)
                    .commit();
        }
        mCurrentFragmentTag = tag;
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);

        final ImageView closeButton = (ImageView) searchView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(closeSearchViewListener);
        return super.onCreateOptionsMenu(menu);
    }

    private void closeSearchView() {
        final int searchInputTextId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText et = (EditText) searchView.findViewById(searchInputTextId);
        et.setText("");
        searchView.setQuery("", false);
        searchView.onActionViewCollapsed();
        searchResultWindow.dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return submitSearchQuert(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return submitSearchQuert(newText);
    }

    private boolean submitSearchQuert(String query) {
        if (query.length() >= 3) {
            WeatherLoaderService.searchCityByName(this, query);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case WeatherLoaderService.REQUEST_CODE_SEARCH_CITY_ID:
                    ArrayList<SearchCityModel> updatedItems = data.getParcelableArrayListExtra(WeatherLoaderService.RESULT);
                    searchResultWindow.showResult(updatedItems);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDismiss() {
        closeSearchView();
    }
}
