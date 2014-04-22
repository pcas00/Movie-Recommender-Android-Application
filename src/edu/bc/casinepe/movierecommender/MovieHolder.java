package edu.bc.casinepe.movierecommender;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieHolder {
	private View row;
	private TextView movieTitleTextView = null;
	private ImageView movieImageView = null;
	
	public MovieHolder(View row) {
		this.row = row;
	}
	
	public TextView getMovieTitleTextView() {
		if (this.movieTitleTextView == null) {
			this.movieTitleTextView = (TextView) row.findViewById(R.id.movie_item_title_text_view);
		}
		return this.movieTitleTextView;
	}
	
	public ImageView getMovieImageView() {
		if (this.movieImageView == null) {
			this.movieImageView = (ImageView) row.findViewById(R.id.movie_image_view);
		}
		return this.movieImageView; 
	}
}
