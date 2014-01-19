package hioa.mappe2.s180475;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;

public class SMSService extends Service{
	
	private DBHandler db;
	private String tlf;
	private int message;
	private String sms;
	private SmsManager smsManager;
	
	public int onStartCommand(Intent intent, int flags, int startid){
		db = new DBHandler(this);
		db.open();
		Cursor cur = db.todaysBirthdays();
		smsManager = SmsManager.getDefault();
		
		if(cur.moveToFirst()){
			tlf = cur.getString(0);
			message = cur.getInt(4);
			if(message == 1){
				sms = db.getPrivateMessage(tlf);
			}
			else
				sms = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.prefEditTextKey), "");
			smsManager.sendTextMessage(tlf, null, sms, null, null);
		}while(cur.moveToNext());
		
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}