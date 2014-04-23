package edu.bc.casinepe.movierecommender;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Movie implements Serializable {
	
	private long id;
	private String title;
	private float rating;
	private Bitmap bm;
	
	public Movie() {}
	public Movie(long id, String title, float rating) {
		this.id = id;
		this.title = title;
		this.rating = rating;
	}
	
	public void setBitmap(Bitmap bm) {
		this.bm = bm;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}

	public Bitmap getBitmap() { return this.bm; }
	public long getId() { return this.id; }
	public String getTitle() { return this.title; }
	public float getRating() { return this.rating; }

}
