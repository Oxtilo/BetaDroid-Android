package developer.macbury.betadroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import developer.macbury.betadroid.R;
import developer.macbury.betadroid.api.BarcodeResult;
import developer.macbury.betadroid.api.Store;
import developer.macbury.betadroid.fragments.AppsFragment;
import developer.macbury.betadroid.servicies.UpdateAppInfoService;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AppsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
              IntentIntegrator integrator = new IntentIntegrator(this);
              integrator.initiateScan();
              return true;
          case R.id.action_refresh:
            startRefreshService();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

  private void startRefreshService() {
    Intent intent = new Intent(this, UpdateAppInfoService.class);
    intent.putExtra(UpdateAppInfoService.EXTRA_VERBOSE, true);
    startService(intent);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if (scanResult != null) {
      Gson g               = new Gson();
      BarcodeResult result = g.fromJson(scanResult.getContents(), BarcodeResult.class);

      if (result == null) {
        Toast.makeText(this, getString(R.string.invalid_qr_code), Toast.LENGTH_LONG).show();
      } else {
        Store store = Store.load(this);
        store.add(result);
        store.save(this);

        startRefreshService();
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
    Intent intent = new Intent(this, UpdateAppInfoService.class);
    intent.putExtra(UpdateAppInfoService.EXTRA_VERBOSE, true);
    startService(intent);
  }
}
