package edu.bc.casinepe.movierecommender;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tasks.MovieImageTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import api.Movie;

public class MoviesAdapter extends ArrayAdapter<Movie>{
	int layout;
	public MoviesAdapter(Context context, List<Movie> movies, int layout) {
		super(context, layout, movies);
		this.layout = layout;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MovieHolder holder = null;
		LayoutInflater inflator = (LayoutInflater)getContext().getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View view = null;
		
		if (convertView == null) {
			view = inflator.inflate(layout, null, false);
			holder = new MovieHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (MovieHolder) view.getTag();
		}
		
		Movie movie = getItem(position);
		holder.getMovieTitleTextView().setText(movie.getTitle());
		if (layout == R.layout.rated_movie_item) {
			holder.getNumStarsRating().setText(view.getContext().getString(R.string.you_rated_this_movie_with) + " " + movie.getRating());
		}

		//Download image asynchronously
		/*if (!movie.bitmapIsLoaded()) {
			Log.i(this.getClass().toString(), "Movie: " + movie.getTitle() + " Bitmap is not loaded; get asynchronously");*/
			new MovieImageTask(holder.getMovieImageView(), getContext(), movie).execute(movie.getTitle());	
		/*} else if (movie.bitmapIsLoaded() && movie.getBitmap() == null) {
			Log.i(this.getClass().toString(), "Movie: " + movie.getTitle() + " BitMap is loaded but it is no poster");
			holder.getMovieImageView().setImageResource(R.drawable.no_movie_poster);
		} else {
			Log.i(this.getClass().toString(), "Movie: " + movie.getTitle() + " Bitmap is loaded and ithas an image");
			holder.getMovieImageView().setImageBitmap(movie.getBitmap());
		}*/
		
		return view;
		
	}
	
	

}
