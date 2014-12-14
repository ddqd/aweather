package im.dema.aweather;

import android.app.Activity;
import android.os.Bundle;

import im.dema.aweather.Fragments.CurrentWeatherListFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CurrentWeatherListFragment(), CurrentWeatherListFragment.TAG)
                    .commit();
        }
    }


}
