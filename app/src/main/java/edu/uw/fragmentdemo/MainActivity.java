package edu.uw.fragmentdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener {

    private static final String TAG = "MainActivity";

    private boolean tf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout fl = (FrameLayout) findViewById(R.id.container_right);

        tf = fl != null && fl.getVisibility() == View.VISIBLE;
        MoviesFragment fragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag("MoviesFragment");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = new MoviesFragment();
        }
        if(tf) {
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag("DetailFragment");
            if(detailFragment == null) {
                detailFragment = new DetailFragment();
            }
            ft.replace(R.id.container_left, fragment, "MoviesFragment");
        } else {
            ft.replace(R.id.container, fragment, "MoviesFragment");
        }
        ft.commit();
    }


    //respond to search button clicking
    public void handleSearchClick(View v){
        Log.v(TAG, "Button handled");

        EditText text = (EditText)findViewById(R.id.txtSearch);
        String searchTerm = text.getText().toString();

        MoviesFragment fragment = (MoviesFragment)getSupportFragmentManager().findFragmentByTag("MoviesFragment");

        if(fragment == null){
            fragment = new MoviesFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, null)
                    .commit();
        }

        fragment.fetchData(searchTerm);
    }

    @Override
    public void movieSelected(Movie movie) {


        Bundle bundle = new Bundle();
        bundle.putString("title", movie.toString());
        bundle.putString("imdb", movie.imdbId);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);

        if(tf) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_right, detailFragment, null)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, detailFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
