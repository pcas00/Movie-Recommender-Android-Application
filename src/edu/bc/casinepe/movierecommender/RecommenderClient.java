package edu.bc.casinepe.movierecommender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RecommenderClient {
	
	private HttpClient client;
	private long userId;
	public static final String URI = "http://localhost:8080/";
	
	public RecommenderClient(long userId) {
		this.userId = userId;
		client = new DefaultHttpClient();
	}
	
	// @return Recommended list of movies
	public Movies getMovies() {
		HttpGet httpGet = new HttpGet(URI + "movies/");
		
		String jsonResponse = makeRequest(httpGet);
		
		parseJsonMovies(jsonResponse);
		return new Movies();
		
	}
	
	/* 	@param userId A user identifier who is rating the movie
	 *	@param m A movie with movie id and rating
	 */
	public Movie rateMovie(long userId, Movie m) {		
		HttpPost httpPost = new HttpPost(URI + "rate/" + m.getId());
		
		String jsonResponse = makeRequest(httpPost);
		
		parseJsonMovie(jsonResponse);
		return new Movie();
	}
	
	/* helper function that parses JSON for a Movies object
	 * @param jsonString a JSON formatted string
	 * @return movies list of movies extracted from JSON string
	 */
	private Movies parseJsonMovies(String jsonString) {
		String result = "";
		try {
			JSONObject jObject   = new JSONObject(jsonString);
			Movies movies = new Movies();
			JSONArray entries    = jObject.getJSONObject("responseData").getJSONArray("movies");
			for (int i=0; i<entries.length(); i++) {
				JSONObject entry      = entries.getJSONObject(i);
				long id               = entry.getLong("id");
				String title          = entry.getString("title");
				float rating		  = entry.getLong("rating");
				movies.addMovie(new Movie(id, title, rating));				
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
		String result = "";
		try {
			
			JSONObject jObject   = new JSONObject(jsonString);
			JSONArray entries    = jObject.getJSONObject("responseData").getJSONArray("movies");
			JSONObject entry      = entries.getJSONObject(0);
			long id               = entry.getLong("id");
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

	
}
