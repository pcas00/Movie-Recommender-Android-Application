package edu.bc.casinepe.movierecommender;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import api.Movie;

public class PutRatingTask extends AsyncTask<Movie, Void, Movie> {
	private ProgressDialog dialog;
	private Context context;
	public PutRatingTask(Context context) {
		dialog = new ProgressDialog(context);
		this.context = context;
	}
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Rating movie...");
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.show();
    }
    
	@Override
	protected Movie doInBackground(Movie... args) {
		return putMovie(args[0]);
	}

	private Movie putMovie(Movie movie) {
		RecommenderClient client = new RecommenderClient(MainActivity.USER_ID);
		return client.rateMovie(MainActivity.USER_ID, movie);
	}
	protected void onPostExecute(Movie ratedMovie) {
		dialog.dismiss();
		String msg = context.getString(R.string.rated_movie_d_msg);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.rated_movie_d_title)
			   .setMessage(msg + " with a " + ratedMovie.getRating())
			   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
			   });

		// Create the AlertDialog
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
}
