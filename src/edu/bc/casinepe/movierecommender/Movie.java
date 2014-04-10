package edu.bc.casinepe.movierecommender;

public class Movie {
	
	private long id;
	private String title;
	private float rating;
	
	public Movie() {}
	public Movie(long id, String title, float rating) {
		this.id = id;
		this.title = title;
		this.rating = rating;
	}
	
	public long getId() { return this.id; }
	public String getTitle() { return this.title; }
	public float getRating() { return this.rating; }

}
