package net.azurewebsites.sprintmood;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SubmittedActivity extends FragmentActivity {

	private static String sendStatus = "(sending...)";
	
	// from: http://stackoverflow.com/questions/9723106/get-activity-instance
	public static Activity activity = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submitted);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		else {
			sendStatus = savedInstanceState.getString("Status");
		}
		
		activity = this; 
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	   super.onSaveInstanceState(outState);
	   outState.putString("Status", (String) ((TextView) findViewById(R.id.submit_status_text)).getText());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.submitted, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			String submitStr;
			
			//get intent info (from udc lesson 3/10)
			Intent intent = getActivity().getIntent();
			
			View rootView = inflater.inflate(R.layout.fragment_submitted, container, false);
			
			if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
				submitStr = intent.getStringExtra(Intent.EXTRA_TEXT);
			}
			else {
				submitStr = "oooops - something's wrong";
				sendStatus = "";
			}
			((TextView) rootView.findViewById(R.id.submitted_text)).setText(submitStr);
			((TextView) rootView.findViewById(R.id.submit_status_text)).setText(sendStatus);

			return rootView;
		}
	}
}
