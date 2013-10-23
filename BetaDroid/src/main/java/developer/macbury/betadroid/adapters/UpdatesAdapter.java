package developer.macbury.betadroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;

import java.util.ArrayList;

import developer.macbury.betadroid.R;
import developer.macbury.betadroid.api.v1.ReleaseInfo;

/**
 * Created by Arkadiusz on 23.10.13.
 */
public class UpdatesAdapter extends ArrayAdapter<ReleaseInfo> {
  private AQuery query;
  private LayoutInflater inflater;

  public UpdatesAdapter(Context context) {
    super(context, 0);
    query = new AQuery(context);

    inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
  }

  public void setReleases(ArrayList<ReleaseInfo> pendingUpdates) {
    for(ReleaseInfo info : pendingUpdates) {
      add(info);
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.list_view_release, parent, false);
    }

    AQuery listQuery = query.recycle(convertView);
    ReleaseInfo info = getItem(position);

    listQuery.id(R.id.app_icon).image(info.getIcon());
    listQuery.id(R.id.app_name).text(info.getName());
    listQuery.id(R.id.package_name).text(info.getPackageName());
    listQuery.id(R.id.version_code_text_view).text("Build: " + info.getVersionCode());

    return convertView;
  }
}
