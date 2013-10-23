package developer.macbury.betadroid.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.io.File;
import java.util.ArrayList;

import developer.macbury.betadroid.R;
import developer.macbury.betadroid.adapters.UpdatesAdapter;
import developer.macbury.betadroid.api.Store;
import developer.macbury.betadroid.api.v1.ReleaseInfo;

public class AppsFragment extends ListFragment {
  private static final String TAG = "AppsFragment";
  public  static final int REQUEST_INSTALL = 123;
  public static final String ACTION_REFRESH = "developer.macbury.betadroid.action.ACTION_REFRESH";
  private Store store;
  private AQuery query;
  private UpdatesAdapter updatesAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    query = new AQuery(getActivity());
  }

  @Override
  public void onResume() {
    super.onResume();
    refreshList();
    IntentFilter filter = new IntentFilter();
    filter.addAction(ACTION_REFRESH);
    getActivity().registerReceiver(onNewActionBroadcastReceiver, filter);
  }

  private void refreshList() {
    this.store          = Store.load(this.getActivity());
    this.updatesAdapter = new UpdatesAdapter(this.getActivity());
    ArrayList<ReleaseInfo> pendingUpdatesArray = this.store.getPendingUpdates(this.getActivity());
    this.updatesAdapter.setReleases(pendingUpdatesArray);
    this.setListAdapter(updatesAdapter);
    setEmptyText(getActivity().getString(R.string.list_view_empty));
    getActivity().getActionBar().setSubtitle(getActivity().getString(R.string.actionbar_subtitle));
  }

  @Override
  public void onPause() {
    super.onPause();

    getActivity().unregisterReceiver(onNewActionBroadcastReceiver);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    ReleaseInfo info = updatesAdapter.getItem(position);

    File target = info.getApkFile();
    query.progress(getApkDownloadProgressDialog()).download(info.getApk(), target, new AjaxCallback<File>() {
      public void callback(String url, File file, AjaxStatus status) {
        if (status.getCode() == 200) {
          Intent promptInstall = new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
          AppsFragment.this.getActivity().startActivity(promptInstall);
        }
      }
    });

  }

  private ProgressDialog getApkDownloadProgressDialog() {
    ProgressDialog dialog = new ProgressDialog(this.getActivity());
    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    dialog.setIndeterminate(false);
    dialog.setCancelable(false);
    dialog.setTitle(getActivity().getString(R.string.download_dialog_title));
    dialog.setCanceledOnTouchOutside(false);
    return dialog;
  }

  private BroadcastReceiver onNewActionBroadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      AppsFragment.this.refreshList();
    }
  };
}