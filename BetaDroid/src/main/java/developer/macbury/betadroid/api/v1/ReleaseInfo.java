package developer.macbury.betadroid.api.v1;

import android.os.Environment;

import com.google.gson.annotations.SerializedName;

import java.io.File;

/**
 * Created by Arkadiusz on 23.10.13.
 */
public class ReleaseInfo {
  private String name;
  @SerializedName("version_code")
  private int versionCode;
  @SerializedName("package")
  private String packageName;
  @SerializedName("build_time")
  private String buildTime;
  private String apk;
  private String icon;

  public int getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(int versionCode) {
    this.versionCode = versionCode;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getBuildTime() {
    return buildTime;
  }

  public void setBuildTime(String buildTime) {
    this.buildTime = buildTime;
  }

  public String getApk() {
    return apk;
  }

  public void setApk(String apk) {
    this.apk = apk;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  @Override
  public boolean equals(Object o) {
    if (ReleaseInfo.class.isInstance(o)) {
      ReleaseInfo other = (ReleaseInfo)o;
      return other.getPackageName().equalsIgnoreCase(this.getPackageName());
    } else {
      return super.equals(o);
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public File getApkFile() {
    File target = new File(ReleaseInfo.getCacheDir(), this.getVersionCode() + this.getPackageName() + ".apk");
    return target;
  }

  public static File getCacheDir() {
    File cacheDir = new File(Environment.getExternalStorageDirectory() + "/betadroid-cache/");
    cacheDir.mkdir();
    return cacheDir;
  }
}
