package edu.bc.casinepe.movierecommender;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		View view;
		Movie movie = getItem(position);
		
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, null);
		} else {
			view = convertView;
		}
		
		TextView movieText = (TextView) convertView.findViewById(R.id.movieItemTextView);
		ImageView movieImage = (ImageView) convertView.findViewById(R.id.movieItemImageView);
		
		movieText.setText(movie.getTitle() + " (" + movie.getRating() + ")");
		
		//TODO helper method to retrieve image from IMDB
		URL imageUrl;
		try {
			imageUrl = new URL("http://www.google.com");
			Bitmap imageValue = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
			movieImage.setImageBitmap(imageValue);
		} catch (MalformedURLException e) {
			//TODO set as default no movie image reference
			//movieImage.setImageBitmap();
			Log.e(MoviesAdapter.class.toString(), e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MoviesAdapter.class.toString(), e.getMessage());
			e.printStackTrace();
		}

		
		return convertView;
		
	}

}
