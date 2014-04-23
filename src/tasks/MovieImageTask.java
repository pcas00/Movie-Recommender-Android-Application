package tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import edu.bc.casinepe.movierecommender.R;
import edu.bc.casinepe.movierecommender.RecommenderClient;
import edu.bc.casinepe.movierecommender.R.drawable;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class MovieImageTask extends AsyncTask<String, String, Bitmap> {
	private ImageView imageView;
	private ProgressDialog dialog;

	
	public MovieImageTask(ImageView imageView, Context context) {
		this.imageView = imageView;
		dialog = new ProgressDialog(context);
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
		Log.i(this.getClass().toString(), "Title is " + params[0]);
		Bitmap bm = RecommenderClient.getImageFromTitle(params[0]);	
		Log.i(this.getClass().toString(), "Bit is: " + bm);
		return bm;
	}


	protected void onPostExecute(Bitmap bm) {
		
		if (bm == null) {
			this.imageView.setImageResource(R.drawable.no_movie_poster);
			Log.i(this.getClass().toString(), "ImageURL was null");
		} else {
			this.imageView.setImageBitmap(bm);
		}
		
		dialog.dismiss();
	
	}

}
