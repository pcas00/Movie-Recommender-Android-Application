package edu.bc.casinepe.movierecommender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
	
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	//Default user_id used to access movies and recommendations
	public static final long USER_ID = 40;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
		//TODO something
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		Toast.makeText(this, "Reselected", Toast.LENGTH_SHORT).show();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Log.i("Swipe", "*****getItem " + position);
			switch(position) {
			case 0: return new AllMoviesFragment();
			case 1: return new MyMoviesFragment();
			}
			return null; // shouldn't happen
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.recommended_movies_title).toUpperCase(l);
			case 1:
				return getString(R.string.my_movies).toUpperCase(l);
			}
			return null;
		}
	}

}

class AllMoviesFragment extends ListFragment {
	
	private ListView listOfMovies = null;
	private List<Movie> movies;
	
	public AllMoviesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.i(this.getClass().toString(), "ALlMoviesFragment onCreateView");
		View rootView = inflater.inflate(R.layout.recommended_movies_fragment,
				container, false);
		
		listOfMovies = (ListView) rootView.findViewById(android.R.id.list);
		movies = new ArrayList<Movie>();
		//Need an adapter for movies so that a view can be inflated with data
		MoviesAdapter adapter = new MoviesAdapter(getActivity(), movies);
		//setListAdapter(adapter);
		listOfMovies.setAdapter(adapter);
				
		getMoviesAndDisplay();

		return rootView;
	}

	public void onResume() {
		super.onResume();
	}
	
	public void getMoviesAndDisplay() {
		Log.i(this.getClass().toString(), "GetMoviesAndDisplay()");
		//TODO get list view in order to append results to
		(new Thread() {
			@Override
			public void run() {
				RecommenderClient client = new RecommenderClient(MainActivity.USER_ID);
				Movies recommendedMovies = client.getMovies();
				List<Movie> allMovies = recommendedMovies.getMovies();
				//TODO movies2 should be movies list from class
				for (Movie m : allMovies) {
					//Add to list of movies; adapter will handle adding them
					Log.i(this.getClass().toString(), "Adding movie: " + m + " to ListView of movies");
					movies.add(m);
				}
			}
		}).start();
	}
}

class MyMoviesFragment extends Fragment {

	public MyMoviesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.recommended_movies_fragment,
				container, false);
		return rootView;
	}
}

