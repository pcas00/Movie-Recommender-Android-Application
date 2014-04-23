package edu.bc.casinepe.movierecommender;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

public class MoviesAdapter extends ArrayAdapter<Movie>{

	public MoviesAdapter(Context context, List<Movie> movies) {
		super(context, R.layout.movie_item, movies);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MovieHolder holder = null;
		LayoutInflater inflator = (LayoutInflater)getContext().getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View view = null;
		
		if (convertView == null) {
			view = inflator.inflate(R.layout.movie_item, null, false);
			holder = new MovieHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (MovieHolder) view.getTag();
		}
		
		Movie movie = getItem(position);
		holder.getMovieTitleTextView().setText(movie.getTitle());
		//holder.getMovieImageView().setImageBitmap(movie.getBitmap());
		//Download image asynchronously
		new MovieImageTask(holder.getMovieImageView(), getContext()).execute(movie.getTitle());
		
		return view;
		
	}
	
	

}
