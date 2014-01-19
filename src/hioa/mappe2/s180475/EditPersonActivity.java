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
import android.widget.TextView;
import android.widget.Toast;

public class EditPersonActivity extends FragmentActivity {

	private EditText fname, lname, bday, pMessage;
	private TextView tlf;
	private String personalMessage;
	private DBHandler db;
	private String date;
	private DialogFragment datePicker;
	private Person person;
	private String personal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_person);
		Intent i = getIntent();
		person = (Person) i.getSerializableExtra("person");
		
		db = new DBHandler(this);
		db.open();
		
		fname = (EditText) findViewById(R.id.ET_editfirst_name);
		lname = (EditText) findViewById(R.id.ET_editlast_name);
		tlf = (TextView) findViewById(R.id.TW_edittlf);
		bday = (EditText) findViewById(R.id.TW_editBirthday);
		pMessage = (EditText) findViewById(R.id.TW_editmessage);
		
		
		fname.setText(person.getFirstName());
		lname.setText(person.getLastName());
		tlf.setText(" " + person.getTlf());
		bday.setText(person.getDate());
		personal = db.getPrivateMessage(person.getTlf());
		if(personal != null)	//Then this person has a personal message
			personalMessage = personal;
		else
			personalMessage = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.prefEditTextKey), "");
		pMessage.setText(personalMessage);
		
		bday.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			    if(hasFocus){
			        showDatePickerFragment(v);
			    }
			  }
			});
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
	
	public void editPerson(View v){
		StringBuilder builder = new StringBuilder(this.getString(R.string.regexFaultStart));
		String firstname = fname.getText().toString();
		String lastname = lname.getText().toString();
		String number = person.getTlf();
		String birthdate = bday.getText().toString();
		String newMessage = pMessage.getText().toString();
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
		
		//edit person and update database
		date = bday.getText().toString();
		
		ContentValues cv = new ContentValues();
		cv.put(DBHandler.ID, number);
		cv.put(DBHandler.FIRST_NAME, firstname);
		cv.put(DBHandler.LAST_NAME, lastname);
		cv.put(DBHandler.DATE, date);
		if(!newMessage.equals(personalMessage)){		//Tests if user changed the sms
			cv.put(DBHandler.HAS_PERSONAL_MESSAGE, 1);
			ContentValues cv2 = new ContentValues();
			cv2.put(DBHandler.ID, number);
			cv2.put(DBHandler.MESSAGE, newMessage);
			if(personal == null)	//The person didn't have any personal message before the edit and we will save this new one in the database.
				messageSuccess = db.insert(DBHandler.TABEL_PERSONAL_MESSAGES, cv2);
			else
				messageSuccess = db.update(DBHandler.TABEL_PERSONAL_MESSAGES,cv2);
		}
		else
			cv.put(DBHandler.HAS_PERSONAL_MESSAGE, 0);
		personSuccess = db.update(DBHandler.TABEL_PERSONS,cv);
		if(personSuccess && messageSuccess)	
			toast = Toast.makeText(this, this.getString(R.string.editPersonSuccess), Toast.LENGTH_LONG);
		else
			toast = Toast.makeText(this, this.getString(R.string.editPersonFailed), Toast.LENGTH_LONG);
		toast.show();
		Intent intent = new Intent(this, BirthdaysActivity.class);
		startActivity(intent);
		this.finish();
	}
	
	//Thiss will show a dialog where user can pick a date
	public void showDatePickerFragment(View v){
		datePicker = new DatePickerFragment();
		datePicker.show(getSupportFragmentManager(), "editPersonDP");
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
}