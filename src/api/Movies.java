package api;

import java.util.ArrayList;
import java.util.List;

public class Movies {
	
	private List<Movie> movies;
	
	public Movies() {
		movies = new ArrayList<Movie>();
	}
	
	public List<Movie> getMovies() { 
		if (movies.isEmpty()) {
			return null;
		} else {
			return this.movies;
		}
	}
	
	public void addMovie(Movie m) { movies.add(m); }

}
