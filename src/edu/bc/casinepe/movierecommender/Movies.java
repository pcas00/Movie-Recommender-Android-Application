package edu.bc.casinepe.movierecommender;

import java.util.LinkedList;
import java.util.List;

public class Movies {
	
	private List<Movie> movies;
	
	public Movies() {
		movies = new LinkedList<Movie>();
	}
	
	public List<Movie> getMovies() { return movies; }
	public void addMovie(Movie m) { movies.add(m); }

}
