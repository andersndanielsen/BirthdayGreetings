package hioa.mappe2.s180475;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	private DBHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);		//sets the defaultvalues for the first use of app
		
		db = new DBHandler(this);
		db.open();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		db.open();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	//get called when user click on menu
		public boolean onOptionsItemSelected(MenuItem i){
			Intent intent;
			if(i.getItemId() == R.id.menu_settings){
				intent =  new Intent(this, SettingsActivity.class);
				startActivity(intent);
			}
			else{
				finish();
			}
			return true;
		}
	
	public void addPerson(View v){
		Intent intent = new Intent(this, AddPersonActivity.class);
		startActivity(intent);
	}
	
	//starts a new activity with birthday childs.
	public void listBirthdays(View v){
		Intent intent = new Intent(this, BirthdaysActivity.class);
		int listType = 0;
		
		if(v.getId() == R.id.main_buttonAll)
			listType = 1;
		
		intent.putExtra("listType", listType);
		startActivity(intent);
	}
	
	//starts a new activity with birtday childs.
	public void listSettings(View v){
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
}
