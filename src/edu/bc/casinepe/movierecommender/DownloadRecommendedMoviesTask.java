package edu.bc.casinepe.movierecommender;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

public class DownloadRecommendedMoviesTask extends AsyncTask<Long, Void, Movies> {
	private MoviesAdapter adapter;
	private List<Movie> listOfMovies;
	public DownloadRecommendedMoviesTask(MoviesAdapter adapter, List<Movie> listOfMovies) {
		this.adapter = adapter;
		this.listOfMovies = listOfMovies;
	}
	
	@Override
	protected Movies doInBackground(Long... args) {
		return downloadRecommendedMovies(args[0]);
	}
	
	private Movies downloadRecommendedMovies(long userId) {
		RecommenderClient client = new RecommenderClient(userId);
		Movies recommendedMovies = client.getMovies();
		return recommendedMovies;

	}

	// onPostExecute displays the results of the AsyncTask.
	protected void onPostExecute(Movies recommendedMovies) {
		Log.i(this.getClass().toString(), "onPostExecute with " + recommendedMovies);
		List<Movie> allMovies = recommendedMovies.getMovies();
		Log.i(this.getClass().toString(), "Movies are: " + allMovies);
		for (Movie m : allMovies) {
			//Add to list of movies; adapter will handle adding them
			Log.i(this.getClass().toString(), "Adding movie: " + m + " to ListView of movies");
			listOfMovies.add(m);
			adapter.notifyDataSetChanged();	
		}
		
	}


	
	//		Log.i(this.getClass().toString(), "");

}
