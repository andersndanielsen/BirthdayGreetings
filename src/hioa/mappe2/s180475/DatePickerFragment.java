package hioa.mappe2.s180475;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		final Calendar cal = Calendar.getInstance(); //Default date for the picker
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	//When date is changed in the datepickerfragment this method is called
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		int correctMonth = month + 1;	//Januar=0
		Activity a = getActivity();
		if(a instanceof AddPersonActivity){
			EditText bday = ((AddPersonActivity) getActivity()).getBDay();
			bday.setText(day + "." + correctMonth + "." + year);
		}
		else{
			EditText bday = ((EditPersonActivity) getActivity()).getBDay();
			bday.setText(day + "." + correctMonth + "." + year);
		}
		
	}
	
	
}
