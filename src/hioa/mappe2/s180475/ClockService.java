package hioa.mappe2.s180475;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;

//Service that get the time set to look after birthdays today, and start an  repeating alarmManager
public class ClockService extends Service{
	
	public int onStartCommand(Intent intent, int flags, int startid){
		//gets saved time of sending sms
		int hour = PreferenceManager.getDefaultSharedPreferences(this).getInt(getString(R.string.prefTimePickerKey) + ".hour", 0);
		int minute = PreferenceManager.getDefaultSharedPreferences(this).getInt(getString(R.string.prefTimePickerKey) + ".minute", 0);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		
		Intent smsIntent = new Intent(this, SMSService.class);
		PendingIntent pending = PendingIntent.getService(this, 0, smsIntent, 0);
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		//If the time occurs in the past, we don't want to send sms to prevent the app sending sms each time the phone reboots.
		if(cal.before(Calendar.getInstance()))
			cal.add(Calendar.DAY_OF_MONTH, 1);
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pending);
		
		stopSelf();
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}