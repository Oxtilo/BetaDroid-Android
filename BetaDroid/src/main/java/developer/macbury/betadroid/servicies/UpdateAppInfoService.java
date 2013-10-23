package developer.macbury.betadroid.servicies;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import developer.macbury.betadroid.api.Store;

/**
 * Created by Arkadiusz on 23.10.13.
 */
public class UpdateAppInfoService extends IntentService {

  public UpdateAppInfoService() {
    super("UpdateAppInfoService");
  }

  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Store store = Store.load(this);

    store.save(this);
  }

}
