package fragments;

import java.util.ArrayList;
import java.util.List;

import tasks.GetRecommendedMoviesTask;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import api.Movie;

import com.google.gson.Gson;

import edu.bc.casinepe.movierecommender.MainActivity;
import edu.bc.casinepe.movierecommender.MoviesAdapter;
import edu.bc.casinepe.movierecommender.R;
import edu.bc.casinepe.movierecommender.RateMovieActivity;

public class AllMoviesFragment extends ListFragment {

	private List<Movie> movies;
	public static final String MOVIE_ID = "movie_id";

	public AllMoviesFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i(this.getClass().toString(), "ALlMoviesFragment onCreateView");
		View rootView = inflater.inflate(R.layout.recommended_movies_fragment,
				container, false);

		TextView header = (TextView) rootView.findViewById(R.id.movie_list_title);
		header.setText(getActivity().getString(R.string.recommended_movies_title));
		
		movies = new ArrayList<Movie>();
		
		//Need an adapter for movies so that a view can be inflated with data
		MoviesAdapter adapter = new MoviesAdapter(getActivity(), movies, R.layout.movie_item);
		setListAdapter(adapter);

		ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			// Use asynchronous task in background to get recommended movies
			new GetRecommendedMoviesTask(adapter, movies, getActivity()).execute(MainActivity.USER_ID);		

		//No internet, show error dialog
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.no_internet_dialog_title)
				   .setMessage(R.string.no_internet_dialog_msg)
				   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
				   });

			// Create the AlertDialog
			AlertDialog dialog = builder.create();
			dialog.show();
		}

		return rootView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, pos, id);
		Intent intent = new Intent(getActivity(), RateMovieActivity.class);
		Movie m = (Movie) l.getItemAtPosition(pos);
		Gson gson = new Gson();
		intent.putExtra(MyMoviesFragment.MOVIE, gson.toJson(m));
		startActivity(intent);
	}

}