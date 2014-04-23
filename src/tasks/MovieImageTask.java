package tasks;

import edu.bc.casinepe.movierecommender.R;
import edu.bc.casinepe.movierecommender.RecommenderClient;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import api.Movie;

public class MovieImageTask extends AsyncTask<String, String, Bitmap> {
	private ImageView imageView;
	private ProgressDialog dialog;
	private Movie movie;

	
	public MovieImageTask(ImageView imageView, Context context, Movie movie) {
		this.imageView = imageView;
		this.dialog = new ProgressDialog(context);
		this.movie = movie;
	}
	
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Loading Images...");
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.show();
    }
	
	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bm = RecommenderClient.getImageFromTitle(params[0]);	
		return bm;
	}


	protected void onPostExecute(Bitmap bm) {
		
		if (bm == null) {
			this.imageView.setImageResource(R.drawable.no_movie_poster);
			//movie.setBitmapLoaded(true);
		} else {
			this.imageView.setImageBitmap(bm);
			//movie.setBitmap(bm);
		}
		
				
		dialog.dismiss();
	
	}

}
