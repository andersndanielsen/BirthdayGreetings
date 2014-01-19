package hioa.mappe2.s180475;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class AddPersonActivity extends FragmentActivity{

	private EditText fname, lname, tlf, bday, pMessage;
	private String personalMessage;
	private DBHandler db;
	private String date;
	private DialogFragment datePicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_person);
		
		db = new DBHandler(this);
		db.open();
		personalMessage = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.prefEditTextKey), "");
		fname = (EditText) findViewById(R.id.ET_first_name);
		lname = (EditText) findViewById(R.id.ET_last_name);
		tlf = (EditText) findViewById(R.id.ET_tlf);
		bday = (EditText) findViewById(R.id.TW_Birthday);
		pMessage = (EditText) findViewById(R.id.TW_message);
		
		//when we touch the edittextfield we wan't the datepickerfragment to pop-up
		bday.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			    if(hasFocus){
			        showDatePickerFragment(v);
			    }
			  }
			});
		
		pMessage.setText(personalMessage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_person, menu);
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
	
	//Adds a person to the database and gives to possibility to make an unique message
	public void addPerson(View v){
		StringBuilder builder = new StringBuilder(this.getString(R.string.regexFaultStart));
		String firstname = fname.getText().toString();
		String lastname = lname.getText().toString();
		String number = tlf.getText().toString();
		String birthdate = bday.getText().toString();
		String newMessage = pMessage.getText().toString();	//Maybe this one is changed?
		boolean personSuccess = true;
		boolean messageSuccess = true;
		Toast toast;
		
		//use regex to control input
		int numErrors = 0;
		if(!firstname.matches("\\D+")){
			builder.append(" '").append(this.getString(R.string.hintAddFirstName)).append("'");
			numErrors++;
		}
		if(!lastname.matches("\\D+")){
			builder.append(" '").append(this.getString(R.string.hintAddLastName)).append("'");
			numErrors++;
		}
		if(!number.matches("\\d{8}")){
			builder.append(" '").append(this.getString(R.string.hintAddTlf)).append("'");
			numErrors++;
		}
		if(birthdate.equals("")){
			builder.append(" '").append(this.getString(R.string.hintAddDate)).append("'");
			numErrors++;
		}
		if(newMessage.equals("")){
			builder.append(" '").append(this.getString(R.string.hintAddMessage)).append("'");
			numErrors++;
		}
		
		if(numErrors > 0){	//One or more errors in input
			toast = Toast.makeText(this, builder.append(" " + getString(R.string.regexFaultEnd).toString()), Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		
		//inserting person to database
		date = bday.getText().toString();
		ContentValues cv = new ContentValues();
		cv.put(DBHandler.ID, number);
		cv.put(DBHandler.FIRST_NAME, firstname);
		cv.put(DBHandler.LAST_NAME, lastname);
		cv.put(DBHandler.DATE, date);
		if(!newMessage.equals(personalMessage)){		//Tests if user made a personal message for this person
			cv.put(DBHandler.HAS_PERSONAL_MESSAGE, 1);
			ContentValues cv2 = new ContentValues();
			cv2.put(DBHandler.ID, number);
			cv2.put(DBHandler.MESSAGE, newMessage);
			messageSuccess = db.insert(DBHandler.TABEL_PERSONAL_MESSAGES,cv2);	//insert personal message into the database table 'messages'
		}
		else
			cv.put(DBHandler.HAS_PERSONAL_MESSAGE, 0);
		personSuccess = db.insert(DBHandler.TABEL_PERSONS,cv);	//insert person into the database table 'persons'
		if(personSuccess && messageSuccess)	
			toast = Toast.makeText(this, this.getString(R.string.insertPersonSuccess), Toast.LENGTH_LONG);
		else
			toast = Toast.makeText(this, this.getString(R.string.insertPersonFailed), Toast.LENGTH_LONG);
		toast.show();
		Intent intent = new Intent(this, BirthdaysActivity.class);
		startActivity(intent);
		this.finish();
	}
	
	public void showDatePickerFragment(View v){
		datePicker = new DatePickerFragment();
		datePicker.show(getSupportFragmentManager(), "addPersonDP");
	}
	
	//User pressed cancel
	public void cancelClicked(View v){
		Intent intent = new Intent(this, BirthdaysActivity.class);
		startActivity(intent);
		this.finish();
	}
	
	public EditText getBDay(){
		return bday;
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
}
