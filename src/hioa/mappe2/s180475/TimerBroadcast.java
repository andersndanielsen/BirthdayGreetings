package hioa.mappe2.s180475;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
public class TimerBroadcast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		//Tests if settings is set to send Sms
		boolean okToSend = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.prefCheckboxKey), false);
		if(okToSend){
			Intent service = new Intent(context, ClockService.class);
			context.startService(service);
		}
	}

}
