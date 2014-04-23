package api;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Movie {
	
	private long id;
	private String title;
	private float rating;
	private Bitmap bm = null;
	private boolean bitmapLoaded = false;
	
	public Movie() {}
	public Movie(long id, String title, float rating) {
		this.id = id;
		this.title = title;
		this.rating = rating;
	}
	
	public void setBitmap(Bitmap bm) {
		this.bm = bm;
		this.bitmapLoaded = true;
	}
	
	public void setBitmapLoaded(boolean b) {
		this.bitmapLoaded = b;
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

	public boolean bitmapIsLoaded() { return this.bitmapLoaded; }
	public Bitmap getBitmap() { return this.bm; }
	public long getId() { return this.id; }
	public String getTitle() { return this.title; }
	public float getRating() { return this.rating; }

}
