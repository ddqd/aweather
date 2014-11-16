package im.dema.aweather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

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

    public void reloadCitiesList(View view) {
        final CitiesLoader loader = new CitiesLoader(this);
        try {
            loader.getCities(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    loader.parseCitiesList(response.body().string());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
