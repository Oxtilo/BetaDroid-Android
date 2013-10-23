package developer.macbury.betadroid.api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import developer.macbury.betadroid.Utils;
import developer.macbury.betadroid.api.v1.ReleaseInfo;

/**
 * Created by Arkadiusz on 23.10.13.
 */
public class Store {
  private ArrayList<ReleaseInfo> infos;
  private ArrayList<BarcodeResult> barcodes;

  public Store() {
    infos    = new ArrayList<ReleaseInfo>();
    barcodes = new ArrayList<BarcodeResult>();
  }

  public void clearNotes() {
    infos.clear();
  }

  public void add(ReleaseInfo info) {
    int index = infos.indexOf(info);
    if (index != -1) {
      infos.remove(index);
    }
    infos.add(info);
  }

  public void remove(ReleaseInfo info) {
    infos.remove(info);
  }

  public static String getPath(Context context) {
    return context.getFilesDir() + "/apps.json";
  }

  public void save(Context context) {
    Gson g          = new Gson();
    Utils.saveString(Store.getPath(context), g.toJson(this));
  }

  public static Store load(Context context) {
    Store store     = null;
    Gson g          = new Gson();
    String rawJson  = Utils.loadString(getPath(context));

    if (rawJson != null) {
      store  = g.fromJson(rawJson, Store.class);
    }

    if (store == null) {
      store = new Store();
    }
    return store;
  }

  public ArrayList<BarcodeResult> getBarcodes() {
    return barcodes;
  }

  public void add(BarcodeResult result) {
    int index = barcodes.indexOf(result);
    if (index != -1) {
      barcodes.remove(index);
    }

    barcodes.add(result);
  }

  public void setInfos(ArrayList<ReleaseInfo> infos) {
    this.infos = infos;
  }

  public ArrayList<ReleaseInfo> getInfos() {
    return infos;
  }

  public ArrayList<ReleaseInfo> getPendingUpdates(Context context) {
    ArrayList<ReleaseInfo> out     = new ArrayList<ReleaseInfo>();
    PackageManager pm              = context.getPackageManager();
    List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

    for (ReleaseInfo info : infos) {
      boolean foundPackage = false;
      for (ApplicationInfo packageInfo : packages) {
        if (packageInfo.packageName.equalsIgnoreCase(info.getPackageName())) {
          foundPackage = true;
          try {
            PackageInfo pInfo = pm.getPackageInfo(info.getPackageName(), 0);
            if (pInfo.versionCode < info.getVersionCode()) {
              out.add(info);
            }
          } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
          }
        }
      }

      if (!foundPackage) {
        out.add(info);
      }
    }

    return out;
  }
}
