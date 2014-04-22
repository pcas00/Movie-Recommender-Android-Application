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
		Log.i(this.getClass().toString(), "Layout is: " + view);
		
		TextView movieText = (TextView) view.findViewById(R.id.movie_item_title_text_view);
		ImageView movieImage = (ImageView) view.findViewById(R.id.movie_image_view);
		
		movieText.setText(movie.getTitle() + " (" + movie.getRating() + ")");
		
		//TODO helper method to retrieve image from IMDB
		//URL imageUrl;
		//try {
			/*
			imageUrl = new URL("http://ia.media-imdb.com/images/M/MV5BMzU0NDY0NDEzNV5BMl5BanBnXkFtZTgwOTIxNDU1MDE@._V1_SY317_CR0,0,214,317_AL_.jpg");
			Bitmap imageValue = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());*/
			
		movieImage.setImageResource(R.drawable.no_movie_poster);
		/*} catch (MalformedURLException e) {
			//TODO set as default no movie image reference
			//movieImage.setImageBitmap();
			Log.e(MoviesAdapter.class.toString(), e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MoviesAdapter.class.toString(), e.getMessage());
			e.printStackTrace();
		}*/

		
		return view;
		
	}

}
