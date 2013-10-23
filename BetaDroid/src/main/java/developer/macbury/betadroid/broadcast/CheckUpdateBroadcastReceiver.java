package developer.macbury.betadroid.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import developer.macbury.betadroid.servicies.UpdateAppInfoService;

/**
 * Created by Arkadiusz on 23.10.13.
 */
public class CheckUpdateBroadcastReceiver extends BroadcastReceiver {
  private static final String TAG = "CheckUpdateBroadcastReceiver";

  public void onReceive(Context context, Intent intent) {
    context.startService(new Intent(context, UpdateAppInfoService.class));
  }
}
