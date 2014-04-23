package api;

public class Rating {
	
    long userId;
    long movieId;
    float rating;

    public Rating(long userId, long movieId, float ratingVal) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = ratingVal;
    }

    public float getRatingVal() {
        return rating;
    }

    public long getUserId() {
        return userId;
    }

    public long getMovieId() {
        return movieId;
    }

}
