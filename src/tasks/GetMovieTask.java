package tasks;
import java.util.List;

import edu.bc.casinepe.movierecommender.MainActivity;
import edu.bc.casinepe.movierecommender.R;
import edu.bc.casinepe.movierecommender.RecommenderClient;
import edu.bc.casinepe.movierecommender.R.drawable;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import api.Movie;


public class GetMovieTask extends AsyncTask<Movie, Void, Movie>{
	TextView movieTitle;
	ImageView movieImage;
	RatingBar movieRating;
	private ProgressDialog dialog;

	
	public GetMovieTask(Context context, TextView movieTitle, ImageView movieImage, RatingBar movieRating) {
		this.movieTitle = movieTitle;
		this.movieRating = movieRating;
		this.movieImage = movieImage;
		this.dialog = new ProgressDialog(context);
	}
	
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Loading Movie...");
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.show();
    }
    
	@Override
	protected Movie doInBackground(Movie... args) {
		return getMovie(args[0]);
	}
	
	private Movie getMovie(Movie movie) {
		RecommenderClient client = new RecommenderClient(MainActivity.USER_ID);
		Movie newMovie = client.getMovie(movie.getId());
		if (newMovie != null) {
			newMovie.setBitmap(RecommenderClient.getImageFromTitle(newMovie.getTitle()));	
		}
		return newMovie;
		
	}

	// onPostExecute displays the results of the AsyncTask.
	protected void onPostExecute(Movie m) {
		if (m != null) {
			movieTitle.setText(m.getTitle());
			movieRating.setRating(m.getRating());
			Bitmap bm = m.getBitmap();
			if (bm == null) {
				movieImage.setImageResource(R.drawable.no_movie_poster);
			} else {
				movieImage.setImageBitmap(m.getBitmap());
			}	
		}
		dialog.dismiss();		
	}
}
