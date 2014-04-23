package tasks;

import java.util.List;

import edu.bc.casinepe.movierecommender.MoviesAdapter;
import edu.bc.casinepe.movierecommender.RecommenderClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import api.Movie;
import api.Movies;

public class GetRecommendedMoviesTask extends AsyncTask<Long, Void, Movies> {
	private MoviesAdapter adapter;
	private List<Movie> listOfMovies;
	private ProgressDialog dialog;
	
	public GetRecommendedMoviesTask(MoviesAdapter adapter, List<Movie> listOfMovies, Context context) {
		this.adapter = adapter;
		this.listOfMovies = listOfMovies;
		this.dialog = new ProgressDialog(context);
	}
	
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Loading recommended movies...");
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.show();
    }
    
	@Override
	protected Movies doInBackground(Long... args) {
		Log.i(this.getClass().toString(), "GetRecommendedMovie doInBackground");
		return getRecommendedMovies(args[0]);
	}
	
	private Movies getRecommendedMovies(long userId) {
		Log.i(this.getClass().toString(), "GetRecommendedMovie getRecommendedMovies");
		RecommenderClient client = new RecommenderClient(userId);
		Movies m = client.getRecommendedMovies();
		Log.i(this.getClass().toString(), "Recommended movie size: " + m.getMovies().size());
		return m;
	}

	// onPostExecute displays the results of the AsyncTask.
	protected void onPostExecute(Movies recommendedMovies) {
		Log.i(this.getClass().toString(), "onPostExecute with rec movies: " + recommendedMovies.getMovies());
		List<Movie> allMovies = recommendedMovies.getMovies();
		for (Movie m : allMovies) {
			Log.i(this.getClass().toString(), "movie m is : " + m);
			//Add to list of movies; adapter will handle adding them
			listOfMovies.add(m);
			adapter.notifyDataSetChanged();			
		}
		dialog.dismiss();
		
	}

}
