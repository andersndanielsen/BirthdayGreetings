package hioa.mappe2.s180475;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference{
	
	private TimePicker timePicker;
	private static final int DEFAULT_HOUR = 12;
	private static final int DEFAULT_MINUTE = 0;

	public TimePreference(Context context, AttributeSet attrs){
		super(context, attrs);
		setPersistent(false);		
	}
	
	public void onBindDialogView(View v){
		super.onBindDialogView(v);
		timePicker = (TimePicker) v.findViewById(R.id.prefTimePicker);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(getSharedPreferences().getInt(getKey() + ".hour", DEFAULT_HOUR));
		timePicker.setCurrentMinute(getSharedPreferences().getInt(getKey() + ".minute", DEFAULT_MINUTE));
	}
	
	//called when dialog is about to close
	protected void onDialogClosed(boolean okToSave){
		super.onDialogClosed(okToSave);
		
		if(okToSave){
			timePicker.clearFocus();
			SharedPreferences.Editor editor = getEditor();
			editor.putInt(getKey() + ".hour", timePicker.getCurrentHour());
			editor.putInt(getKey() + ".minute", timePicker.getCurrentMinute());
			editor.commit();
			//Tests if settings is set to send Sms, and starts Service if it is
			boolean okToSend = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(getContext().getString(R.string.prefCheckboxKey), false);
			if(okToSend){
				Intent service = new Intent(getContext(), ClockService.class);
				getContext().startService(service);
			}
		}
	}
}
