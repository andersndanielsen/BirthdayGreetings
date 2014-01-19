package hioa.mappe2.s180475;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BirthdayAdapter extends ArrayAdapter<Person>{
	private Context con;
	private ArrayList<Person> personList;
	private TextView name, date, age, remaining;
	
	public BirthdayAdapter(Context context, int layoutResource, int textViewName, ArrayList<Person> persons){
		super(context, layoutResource, textViewName,persons);
		con = context;
		personList = persons;
	}
	
	//how every row in the list shall look like. Finds out age of person and days left to birthday
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row =  inflater.inflate(R.layout.radlayout2, parent, false);
		name = (TextView) row.findViewById(R.id.personName);
		date = (TextView) row.findViewById(R.id.personDate);
		//ageH = (TextView) row.findViewById(R.id.personAgeHeader);
		age = (TextView) row.findViewById(R.id.personAge);
		//remainingH = (TextView) row.findViewById(R.id.personRemainingHeader);
		remaining = (TextView) row.findViewById(R.id.personRemaining);
		
		//Splits the date-string into days, months,year
		String birth = personList.get(position).getDate();
		String[] split;
		String delimiter = "\\.";
		split = birth.split(delimiter);
		
		int birthDay = Integer.parseInt(split[0]);
		int birthMonth = Integer.parseInt(split[1]);
		int birthYear = Integer.parseInt(split[2]);
		GregorianCalendar birthCal = new GregorianCalendar();	//GregorianCalendar-object with date set to day of birth
		birthCal.set(birthYear, birthMonth, birthDay);
		int birthDayOfYear = birthCal.get(Calendar.DAY_OF_YEAR);

		GregorianCalendar nowCal = new GregorianCalendar();	//GregorianCalendar-object with date set to today
		nowCal.add(Calendar.MONTH, 1);	//Januar=0
		int nowYear = nowCal.get(Calendar.YEAR);
		int nowDayOfYear = nowCal.get(Calendar.DAY_OF_YEAR) + 1;
		
		int howOld = nowYear - birthYear;	//persons age
		if(birthDayOfYear > nowDayOfYear)	//if today is before birthday
			howOld-= 1;
		int daysLeft = birthDayOfYear-nowDayOfYear;
		if(daysLeft < 0 && nowCal.isLeapYear(Calendar.YEAR))	//if birthday is in the past and this year is leapyear
			daysLeft += 366;
		else if(daysLeft < 0)
			daysLeft += 365;
		
		//save what we have and found in the listview rows
		name.setText(personList.get(position).getFirstName() + " " + personList.get(position).getLastName());
		date.setText(personList.get(position).getDate());
		age.setText(howOld+"");
		remaining.setText(daysLeft + "");
		return row;
	}

}