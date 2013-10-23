package developer.macbury.betadroid.fragments;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import developer.macbury.betadroid.R;
import developer.macbury.betadroid.api.Store;

public class AppsFragment extends Fragment {
  private static final String TAG = "AppsFragment";
  private Store store;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_apps, container, false);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PackageManager pm = getActivity().getPackageManager();
    List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

    for (ApplicationInfo packageInfo : packages) {
      try {
        PackageInfo pInfo = pm.getPackageInfo(packageInfo.packageName, 0);
        Log.d(TAG, "Installed package :" + packageInfo.packageName + " version: " + pInfo.versionCode);
      } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
      }
      Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    this.store = Store.load(this.getActivity());
  }
}