package edu.bc.casinepe.movierecommender;

import com.google.gson.Gson;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RateMovieActivity extends Activity {
	private Movie movie;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		Intent intent = getIntent();
		Gson gson = new Gson();
	    this.movie = gson.fromJson(intent.getStringExtra(MyMoviesFragment.MOVIE), Movie.class);
	    Log.i(this.getClass().toString(), "Movie id is: " + movie.getId());
	    
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		    
		setContentView(R.layout.activity_rate_movie);
	    
	    final TextView movieTitle = (TextView) findViewById(R.id.rate_movie_title_textview);
	    final ImageView movieImage = (ImageView) findViewById(R.id.rate_movie_imageview);
	    final RatingBar movieRating = (RatingBar) findViewById(R.id.star_rating);	  
	    final Button submitRating = (Button) findViewById(R.id.rate_movie_button);
	    
		new GetMovieTask(this, movieTitle, movieImage, movieRating).execute(movie);
		
		submitRating.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		    	float rating = movieRating.getRating();
		    	Movie m = new Movie(movie.getId(), movie.getTitle(), rating);
		    	Log.i(this.getClass().toString(), "Put rating for: " + m);
		    	m.setRating(rating);
		    	new PutRatingTask(v.getContext()).execute(m);
		    }
		});
	    
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rate_movie, menu);
		return true;
	}

}
