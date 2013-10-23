package developer.macbury.betadroid;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import developer.macbury.betadroid.broadcast.CheckUpdateBroadcastReceiver;

/**
 * Created by Arkadiusz on 23.10.13.
 */
public class AppDelegate extends Application {
  public AppDelegate() {
    super();
  }

  @Override
  public void onCreate() {
    super.onCreate();

    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, CheckUpdateBroadcastReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT);

    alarmManager.cancel(refreshPendingIntent);
    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 10000, AlarmManager.INTERVAL_HOUR, refreshPendingIntent);
  }
}
