package net.azurewebsites.sprintmood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class FeedbackActivity extends FragmentActivity {

	private String apiKey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		
		//apiKey = "2AB5BA6F-ED8F-4E15-AF7A-F76FFB963";
		
		//create manager
		FragmentManager fm = getSupportFragmentManager();

		//get existing fragment in view
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		//add fragment to view if no existing fragment
		if (fragment == null)
		{
			//assign a new fragment to add to the view
			fragment = new FeedbackFragment();
			//and add it
			fm.beginTransaction()
				.add(R.id.fragmentContainer, fragment)
				.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feedback, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
		}
				
		return super.onOptionsItemSelected(item);
	}

}
