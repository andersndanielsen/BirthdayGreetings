package hioa.mappe2.s180475;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BirthdaysActivity extends ListActivity {
	private ListView list;
	private Intent intent;
	private DBHandler db;
	protected static ArrayList<Person> personList;
	protected static ArrayAdapter<Person> adapter;
	private Person person;
	private Cursor cur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_birthdays);
		list = (ListView) findViewById(android.R.id.list);
		registerForContextMenu(list);	//menu when long-clicking on list
		list.setOnCreateContextMenuListener(this);
		intent = getIntent();
		int listType = intent.getIntExtra("listType", 1);
		personList = new ArrayList<Person>();
		
		db = new DBHandler(this);
		db.open();
		
		if(listType == 1)
			cur = db.allBirthdays();
		else
			cur = db.todaysBirthdays();
		
		if(cur.moveToFirst()){
			do{
				String firstname = cur.getString(1);
				String lastname = cur.getString(2);
				String tlf = cur.getString(0);
				String date = cur.getString(3);
				int message = cur.getInt(4);
				person = new Person(firstname, lastname, tlf, date, message);
				personList.add(person);
			}while(cur.moveToNext());
		}
		adapter = new BirthdayAdapter(this, R.layout.radlayout2, R.id.personName, personList);
		list.setAdapter(adapter);
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.delete_person, menu);
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
	
	//Menu that pops up when long-click on list-item
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()){
		case R.id.contextMenu_delete: 
			db.delete(DBHandler.TABEL_PERSONS, DBHandler.ID + "='" + personList.get(info.position).getTlf() + "'", null);
			if(personList.get(info.position).gethasPersonalSMS() == 1)
				db.delete(DBHandler.TABEL_PERSONAL_MESSAGES, DBHandler.ID + "='" + personList.get(info.position).getTlf() + "'", null);
			personList.remove(info.position);
			adapter.notifyDataSetChanged();
			return true;
		case R.id.contextMenu_edit:
			Person p = personList.get(info.position);
			Intent intent = new Intent(this, EditPersonActivity.class);
			intent.putExtra("person",p);
			startActivity(intent);
			finish();
			return true;
		case R.id.contextMenu_cancel:
			return false;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_everyone, menu);
		return true;
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
