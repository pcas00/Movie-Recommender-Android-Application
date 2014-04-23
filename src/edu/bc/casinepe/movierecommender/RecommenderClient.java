package edu.bc.casinepe.movierecommender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class RecommenderClient {

	private HttpClient client;
	private long userId;
	public static final String URI = "http://localhost:8080/";

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
			Log.i(this.getClass().toString(), "Poster is: " + imageString);

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
		/*HttpGet httpGet = new HttpGet(URI + "movies/");

		String jsonResponse = makeRequest(httpGet);*/

		String jsonResponse = "{\"movies\":[{\"title\":\"Open Season (1996)\",\"rating\":3.0,\"ratingsCount\":0,\"movieId\":402},{\"title\":\"Running Free (2000)\",\"rating\":4.0,\"ratingsCount\":0,\"movieId\":3647},{\"title\":\"Condition Red (1995)\",\"rating\":4.0,\"ratingsCount\":0,\"movieId\":624},{\"title\":\"Smoking/No Smoking (1993)\",\"rating\":4.0,\"ratingsCount\":0,\"movieId\":3530},{\"title\":\"Nueba Yol (1995)\",\"rating\":1.0,\"ratingsCount\":0,\"movieId\":133}]}";

		return parseJsonMovies(jsonResponse);

	}
	
	// @return Recommended list of movies
	public Movies getRatedMovies() {
		/*HttpGet httpGet = new HttpGet(URI + "movies/");

		String jsonResponse = makeRequest(httpGet);*/

		String jsonResponse = "{\"movies\":[{\"title\":\"Movie One\",\"rating\":3.0,\"ratingsCount\":0,\"movieId\":402},{\"title\":\"Movie two Free (2000)\",\"rating\":4.0,\"ratingsCount\":0,\"movieId\":3647},{\"title\":\"Movie three (1995)\",\"rating\":4.0,\"ratingsCount\":0,\"movieId\":624},{\"title\":\"Movie 6 (1993)\",\"rating\":4.0,\"ratingsCount\":0,\"movieId\":3530},{\"title\":\"Nueba Yol (1995)\",\"rating\":1.0,\"ratingsCount\":0,\"movieId\":133}]}";

		return parseJsonMovies(jsonResponse);

	}

	public Movie getMovie(long movieId) {
		/*HttpGet httpGet = new HttpGet(URI + "");
		String jsonResponse = makeRequest(httpGet);*/
		String jsonResponse = "{\"movies\":[{\"title\":\"Open Season (1996)\",\"rating\":3.0,\"ratingsCount\":0,\"movieId\":402}]}";

		return parseJsonMovie(jsonResponse);
	}

	/* 	@param userId A user identifier who is rating the movie
	 *	@param m A movie with movie id and rating
	 */
	public Movie rateMovie(long userId, Movie m) {		
		HttpPut httpPut = new HttpPut(URI + "movie/" + m.getId() + "/rate");
		String jsonResponse = makeRequest(httpPut);

		return parseJsonMovie(jsonResponse);
	}

	/* helper function that parses JSON for a Movies object
	 * @param jsonString a JSON formatted string
	 * @return movies list of movies extracted from JSON string
	 */
	private Movies parseJsonMovies(String jsonString) {
		String result = "";
		try {
			JSONObject jObject   = new JSONObject(jsonString);
			//Log.i(this.getClass().toString(), "JSON Object: " + jObject);
			Movies movies = new Movies();
			JSONArray entries    = jObject.getJSONArray("movies");
			Log.i(this.getClass().toString(), "Entries are: " + entries.length());
			for (int i=0; i<entries.length(); i++) {
				JSONObject entry      = entries.getJSONObject(i);
				long id               = entry.getLong("movieId");
				String title          = entry.getString("title");
				float rating		  = entry.getLong("rating");
				Movie m = new Movie(id, title, rating);
				movies.addMovie(m);		
			}

			return movies;
		} catch (JSONException e) {
			return null;
		}
	}

	/* helper function that parses JSON for a Movie object
	 * @param jsonString a JSON formatted string
	 * @return movie a movie extracted from JSON string
	 */
	private Movie parseJsonMovie(String jsonString) {
		try {
			JSONObject jObject   = new JSONObject(jsonString);
			//Log.i(this.getClass().toString(), "JSON Object: " + jObject);
			JSONArray entries    = jObject.getJSONArray("movies");
			Log.i(this.getClass().toString(), "Entries are: " + entries.length());
			JSONObject entry      = entries.getJSONObject(0);
			long id               = entry.getLong("movieId");
			String title          = entry.getString("title");
			float rating		  = entry.getLong("rating");
			
			return new Movie(id, title, rating);
			
		} catch (JSONException e) {
			return null;
		}
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
			if (statusCode == 200) {
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
		int firstComma = movieTitle.indexOf(',');
		movieTitle = (firstComma == -1) ? movieTitle : movieTitle.substring(0,firstComma);

		String friendlyMovieTitle = (firstParenthesis == -1) ? movieTitle : movieTitle.substring(0, firstParenthesis-1);
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
			Log.i(MovieImageTask.class.toString(), "imageValue is: " + bm);
		} catch (MalformedURLException e) {
			Log.e(MovieImageTask.class.toString(), e.getMessage());
		} catch (IOException e) {
			Log.e(MovieImageTask.class.toString(), e.getMessage());
		}

		return bm;		

	}

}
