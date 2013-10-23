package developer.macbury.betadroid.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arkadiusz on 23.10.13.
 */
public class BarcodeResult {
  private String url;
  @SerializedName("api_version")
  private int version;

  @Override
  public boolean equals(Object o) {
    if (BarcodeResult.class.isInstance(o)) {
      return ((BarcodeResult)o).getUrl().equalsIgnoreCase(this.getUrl());
    } else {
      return super.equals(o);
    }
  }

  public String getUrl() {
    return url;
  }

  public int getVersion() {
    return version;
  }
}
