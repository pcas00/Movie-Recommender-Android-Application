package edu.bc.casinepe.movierecommender;

import java.util.ArrayList;
import java.util.List;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecommendedMoviesFragment extends FragmentActivity {
	
	private ListView listOfMovies = null;
	private List<Movie> movies;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(this.getClass().toString(), "onCreateView has been called");
		if (container==null)
			return null;
		
		//TODO Need to change this view!!
		View rootView = inflater.inflate(R.layout.recommended_movies_fragment, container, false);

		//listOfMovies = (ListView) findViewById(R.id.list);

		/*if (client == null) {
			Log.i(this.getClass().toString(), "Client was null; adding Recommender Client");
			client = new RecommenderClient(MainActivity.USER_ID);
		}*/
				
		//Need an adapter for movies so that a view can be inflated with data
		MoviesAdapter adapter = new MoviesAdapter(getBaseContext(), movies);
		listOfMovies.setAdapter(adapter);
		
		getMoviesAndDisplay();
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getMoviesAndDisplay();
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
				Movies movies2 = new Movies();
				for (Movie m : allMovies) {
					//Add to list of movies; adapter will handle adding them
					movies2.addMovie(m);
				}
			}
		}).start();
	}

}
