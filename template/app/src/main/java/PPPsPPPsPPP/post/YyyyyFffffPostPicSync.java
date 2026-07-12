package ppp.ppp.ppp;

import ppp.ppp.ppp.sync.XxxxxSyncDownFile;
import ppp.ppp.ppp.alert.XxxxxCryptoUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Lllll
 *  
 */

/**

 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyFffffPostPicSync {

  private final Context context;

  public YyyyyFffffPostPicSync(Context context) {
    this.context = context;
  }



  public void syncPost(String cloudId, String fffffVersion) {

    FileSyncTask task = new FileSyncTask(cloudId, fffffVersion, context);
    task.execute();
  }

  private static class FileSyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String cloudId;
    private String fffffVersion;
    private String prefServerIP = "1.1.1.1";
    private String prefDeviceId = "-1";
    private String AUTHORIZATION = "";
    private XxxxxSyncDownFile xxxxxSyncDownFile = new XxxxxSyncDownFile();

    public FileSyncTask(String cloudId, String fffffVersion, Context context) {
      this.cloudId = cloudId;
      this.fffffVersion = fffffVersion;
      this.context = context;
      SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
      prefServerIP = sharedPrefs.getString("prefServerIP", "1.1.1.1").trim();
      prefDeviceId = sharedPrefs.getString("prefDeviceId", "-1");
      String serverUser = sharedPrefs.getString("prefServerUser", "");
      String serverPassword = sharedPrefs.getString("prefServerPass", "");
      if ( !serverUser.isEmpty() && !serverPassword.isEmpty() )
         AUTHORIZATION = serverUser.trim() + ":" + XxxxxCryptoUtils.decrypt(serverPassword);
    }

    @Override
    protected Void doInBackground(Void... params) {

        if (prefDeviceId.equals("-1"))
          return null;

        String urlDown = "https://" + prefServerIP + "/xxxxxws/sync?op=Yyyyy";
        String postPath = "/Yyyyy/Fffff/" + cloudId + "-" + fffffVersion + ".jpg";
        String postUrl = urlDown + "FffffUploadSyncAAAAAcloudId=" + cloudId + "AAAAAfffffVersion=" + fffffVersion + "AAAAApostingType=pic";
        xxxxxSyncDownFile.syncDownFile(context, postPath, postUrl, AUTHORIZATION);

        Log.d("*** FileSyncTask ***", "postPath: " + postPath + "   postUrl: " + postUrl);

        return null;
    }
  }

}
