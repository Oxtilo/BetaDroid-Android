package developer.macbury.betadroid.servicies;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import developer.macbury.betadroid.R;
import developer.macbury.betadroid.activities.MainActivity;
import developer.macbury.betadroid.api.BarcodeResult;
import developer.macbury.betadroid.api.Store;
import developer.macbury.betadroid.api.v1.ReleaseInfo;
import developer.macbury.betadroid.fragments.AppsFragment;

/**
 * Created by Arkadiusz on 23.10.13.
 */
public class UpdateAppInfoService extends IntentService {
  private static final String TAG                 = "UpdateAppInfoService";
  private static final String KEY_STATUS          = "status";
  private static final String STATUS_NEW_RELEASES = "NEW_RELEASE";
  private static final String KEY_RELEASE         = "release";
  public static final String  EXTRA_VERBOSE       = "EXTRA_VERBOSE";
  private static final int NOTIFICATION_REFRESH_ID         = 1;
  private static final int NOTIFICATION_PENDING_UPDATES_ID = 2;
  private Store store;

  public UpdateAppInfoService() {
    super("UpdateAppInfoService");
  }

  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent.hasExtra(EXTRA_VERBOSE)) {
      NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
      builder.setTicker(getString(R.string.notification_update_title));
      builder.setContentTitle(getString(R.string.notification_update_title));
      builder.setProgress(10, 0, true);
      builder.setSmallIcon(android.R.drawable.ic_popup_sync);
      builder.setContentInfo(getString(R.string.notification_update_content));
      startForeground(NOTIFICATION_REFRESH_ID, builder.build());
    }
    this.store  = Store.load(this);
    clearCache();
    checkForUpdates();

    sendBroadcast(new Intent(AppsFragment.ACTION_REFRESH));
    stopForeground(true);

    ArrayList<ReleaseInfo> pendingForUpdate = store.getPendingUpdates(this);

    if (pendingForUpdate.size() > 0 && !intent.hasExtra(EXTRA_VERBOSE)) {
      NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
      builder.setSmallIcon(R.drawable.ic_launcher);
      String contentTitle = String.format(getString(R.string.notification_new_update_title), pendingForUpdate.size());
      builder.setContentTitle(contentTitle);

      String names = "";

      for (ReleaseInfo info : pendingForUpdate) {
        names += info.getName() + ", ";
      }

      builder.setContentText(names);
      builder.setAutoCancel(true);
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
      builder.setContentIntent(contentIntent);
      NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      mNotificationManager.notify(NOTIFICATION_PENDING_UPDATES_ID, builder.build());
    }
  }

  private void clearCache() {
    for(String fileName : ReleaseInfo.getCacheDir().list()) {
      File file = new File(ReleaseInfo.getCacheDir() + "/" + fileName);
      file.delete();
      Log.i(TAG, "Found file: " + file.getAbsoluteFile());
    }

    Log.i(TAG, "Cleared cache!");
  }

  private void checkForUpdates() {
    AQuery query = new AQuery(this);

    Gson gson   = new Gson();
    ArrayList<ReleaseInfo> infos = new ArrayList<ReleaseInfo>();

    for(BarcodeResult result : store.getBarcodes()) {
      AjaxCallback<JSONObject> callback = new AjaxCallback<JSONObject>();
      callback.url(result.getUrl());
      callback.type(JSONObject.class);
      callback.header("Accept", "application/json");
      query.sync(callback);
      int code = callback.getStatus().getCode();
      if (code == 200) {
        JSONObject object = callback.getResult();
        if (STATUS_NEW_RELEASES.equalsIgnoreCase(object.optString(KEY_STATUS))) {
          ReleaseInfo info = gson.fromJson(object.optString(KEY_RELEASE).toString(), ReleaseInfo.class);
          if (info != null) {
            infos.add(info);
          }
        }
      } else {
        Log.i(TAG, "Invalid response " + callback.getStatus().getCode() + " for " + result.getUrl());
      }
    }
    store.setInfos(infos);
    store.save(this);
  }
}
