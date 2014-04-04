package edu.bc.casinepe.movierecommender;

public class Movie {
	
	private long id;
	private String title;
	
	public Movie() {}
	public Movie(long id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public long getId() { return this.id; }
	public String getTitle() { return this.title; }

}
