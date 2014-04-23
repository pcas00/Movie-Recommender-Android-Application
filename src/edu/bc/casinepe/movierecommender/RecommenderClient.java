package edu.bc.casinepe.movierecommender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tasks.MovieImageTask;

import com.google.gson.Gson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import api.Movie;
import api.Movies;
import api.Rating;

public class RecommenderClient {

	private HttpClient client;
	private long userId;
	public static final String URI = "http://10.0.2.2:8080/";

	public RecommenderClient(long userId) {
		this.userId = userId;
		client = new DefaultHttpClient();
	}

	public URL getMovieImage(String request) {

		HttpGet httpGet = new HttpGet("http://www.omdbapi.com/?t=" + request);		
		String response = makeRequest(httpGet);
		URL imageUrl = null;

		try {
			JSONObject jObject = new JSONObject(response);
			String imageString = jObject.getString("Poster");

			if (!imageString.equalsIgnoreCase("n/a")) {
				imageUrl = new URL(imageString);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 

		return imageUrl;
	}

	// @return Recommended list of movies
	public Movies getRecommendedMovies() {
		HttpGet httpGet = new HttpGet(URI + "movies/db/" + this.userId);
		Log.i(this.getClass().toString(), "URI is: " + URI + "movies/db/" + this.userId);
		String jsonResponse = makeRequest(httpGet);
		return parseJsonMovies(jsonResponse);

	}
	
	// @return Recommended list of movies
	public Movies getRatedMovies() {
		HttpGet httpGet = new HttpGet(URI + "user/"+ MainActivity.USER_ID + "/movies");
		String jsonResponse = makeRequest(httpGet);
		return parseJsonMovies(jsonResponse);

	}

	public Movie getMovie(long movieId) {
		HttpGet httpGet = new HttpGet(URI + "movie/" + movieId);
		String jsonResponse = makeRequest(httpGet);
		return parseJsonMovie(jsonResponse);
	}

	/* 	@param userId A user identifier who is rating the movie
	 *	@param m A movie with movie id and rating
	 */
	public Movie rateMovie(long userId, Movie m) {		
		HttpPut httpPut = new HttpPut(URI + "rate/" + m.getId());
		Gson gson = new Gson();
		Rating rating = new Rating(userId, m.getId(), m.getRating());
		StringEntity params = null;
		String jsonResponse = null;
		try {
			params = new StringEntity(gson.toJson(rating));
			params.setContentType("application/json");
			httpPut.setEntity(params);
			jsonResponse = makeRequest(httpPut);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(this.getClass().toString(), e.toString());
		}
		
		return parseJsonMovie(jsonResponse);
	}

	/* helper function that parses JSON for a Movies object
	 * @param jsonString a JSON formatted string
	 * @return movies list of movies extracted from JSON string
	 */
	private Movies parseJsonMovies(String jsonString) {
		Movies movies = new Movies();
		try {
			JSONObject jObject   = new JSONObject(jsonString);
			JSONArray entries    = jObject.getJSONArray("movies");
			for (int i=0; i<entries.length(); i++) {
				JSONObject entry      = entries.getJSONObject(i);
				long id               = entry.getLong("movieId");
				String title          = entry.getString("title");
				float rating		  = entry.getLong("rating");
				Movie m = new Movie(id, title, rating);
				movies.addMovie(m);		
			}

		} catch (JSONException e) {
			Log.e(this.getClass().toString(), "JSON Exception: " + e.getMessage());
			return null;
		}
		
		return movies;

	}

	/* helper function that parses JSON for a Movie object
	 * @param jsonString a JSON formatted string
	 * @return movie a movie extracted from JSON string
	 */
	private Movie parseJsonMovie(String jsonString) {
		Movie m = new Movie();
		try {
			JSONObject jObject   = new JSONObject(jsonString);
			long id               = jObject.getLong("movieId");
			String title          = jObject.getString("title");
			float rating		  = jObject.getLong("rating");
			m.setId(id);
			m.setTitle(title);
			m.setRating(rating);
			
		} catch (JSONException e) {
			Log.e(this.getClass().toString(), "JSON Exception: " + e.getMessage());
			return null;
		}
		
		return m;
	}

	/*
	 * @param request contains a GET or POST request
	 * @return result a JSON response from the server
	 */
	private String makeRequest(HttpUriRequest request) {
		String result = "";
		try {
			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200 || statusCode == 201) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content), 65536);
				String line;
				while ((line = reader.readLine()) != null) {
					result += line + "\n";
				}
			} else {
				Log.e("json", "failed to download");
				result = "{ statusCode: " + statusCode + "}";
			}
		} catch (IOException e) {
			Log.i("network", e.toString());
		}

		return result;

	}

	public static Bitmap getImageFromTitle(String movieTitle) {
		//Replace spaces with +
		movieTitle = movieTitle.replace(' ', '+');
		//Get first occurence of '('' to find year
		int firstParenthesis = movieTitle.indexOf('(');
		String year = null;
		if (firstParenthesis != -1) {
			year = movieTitle.substring(firstParenthesis+1, firstParenthesis + 5);
		}

		/* Check to see if there is a comma
						   E.g Shawshank Redemption, The (1994) => Shawshank Redemption
		 */

		movieTitle = (firstParenthesis == -1) ? movieTitle : movieTitle.substring(0, firstParenthesis-1);
		int firstComma = movieTitle.indexOf(',');

		String friendlyMovieTitle = (firstComma == -1) ? movieTitle : movieTitle.substring(0,firstComma);
		final String request = (year == null) ? friendlyMovieTitle : friendlyMovieTitle + "&y=" + year;
		//Use OMDBApi to request movie image using friendly title and year

		RecommenderClient client = new RecommenderClient(MainActivity.USER_ID);
		URL movieImageUrl = client.getMovieImage(request);

		if (movieImageUrl == null) {
			return null;
		}

		Bitmap bm = null;
		try {
			InputStream is = movieImageUrl.openConnection().getInputStream();
			bm = BitmapFactory.decodeStream(is);
		} catch (MalformedURLException e) {
			Log.e(MovieImageTask.class.toString(), e.getMessage());
		} catch (IOException e) {
			Log.e(MovieImageTask.class.toString(), e.getMessage());
		}

		return bm;		

	}

}
