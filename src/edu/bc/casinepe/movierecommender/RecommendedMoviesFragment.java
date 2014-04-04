package edu.bc.casinepe.movierecommender;

import java.util.List;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

public class RecommendedMoviesFragment extends Fragment {
	
	private RecommenderClient client = null;
	private ListView listOfMovies = null;
	private List<Movie> movies;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container==null)
			return null;
		
		//TODO Need to change this view!!
		View rootView = inflater.inflate(/*R.layout.fragment_main*/ R.layout.activity_main, container, false);

		listOfMovies = (ListView)getView().findViewById(R.id.listOfMovies);

		if (client == null) {
			client = new RecommenderClient(MainActivity.USER_ID);
		}
		
		//Need an adapter for movies so that a view can be inflated with data
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getMoviesAndDisplay();
	}
	
	public void getMoviesAndDisplay() {
		//TODO get list view in order to append results to
		(new Thread() {
			@Override
			public void run() {
				Movies recommendedMovies = client.getMovies();
				movies = recommendedMovies.getMovies();
			}
		}).start();
	}

}
