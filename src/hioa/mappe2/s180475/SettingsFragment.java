package hioa.mappe2.s180475;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	private static SharedPreferences sharedPref;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		sharedPref = getPreferenceManager().getSharedPreferences();
	}
	
	@Override
    public void onResume() {
        super.onResume();
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    //this is called when we change any preferences in the SettingsActivity
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		boolean checked;
		if(key.equals(getString(R.string.prefCheckboxKey))){
			checked = sharedPreferences.getBoolean(key, true);
			if(!checked){ //cancel existing alarmManager if unchecked
				Intent smsIntent = new Intent(getActivity(), SMSService.class);
				PendingIntent pending = PendingIntent.getService(getActivity(), 0, smsIntent, 0);
				AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
				alarm.cancel(pending);
			}
			else{
				Intent service = new Intent(getActivity(), ClockService.class);
				getActivity().startService(service);
			}
		}		
	}	
}