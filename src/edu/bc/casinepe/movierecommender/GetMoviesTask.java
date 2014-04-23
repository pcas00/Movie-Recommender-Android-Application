package edu.bc.casinepe.movierecommender;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class GetMoviesTask extends AsyncTask<Long, Void, Movies> {
	private MoviesAdapter adapter;
	private List<Movie> listOfMovies;
	private ProgressDialog dialog;
	
	public GetMoviesTask(MoviesAdapter adapter, List<Movie> listOfMovies, Context context) {
		this.adapter = adapter;
		this.listOfMovies = listOfMovies;
		this.dialog = new ProgressDialog(context);
	}
	
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Loading your movies...");
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.show();
    }
    
	@Override
	protected Movies doInBackground(Long... args) {
		return getRatedMovies(args[0]);
	}
	
	private Movies getRatedMovies(long userId) {
		RecommenderClient client = new RecommenderClient(userId);
		Movies recommendedMovies = client.getRatedMovies();
		return recommendedMovies;

	}

	// onPostExecute displays the results of the AsyncTask.
	protected void onPostExecute(Movies recommendedMovies) {
		List<Movie> allMovies = recommendedMovies.getMovies();
		for (Movie m : allMovies) {
			//Add to list of movies; adapter will handle adding them
			listOfMovies.add(m);
			adapter.notifyDataSetChanged();	
		}
		dialog.dismiss();
		
	}
	
}
