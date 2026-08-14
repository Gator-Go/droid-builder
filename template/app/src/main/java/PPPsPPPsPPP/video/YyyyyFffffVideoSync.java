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
 * This is a YyyyyFffffVideoSync helper that uploads/syncs
 * a video file to the server.
 * It is a background task that syncs a video to the remote
 * server.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyFffffVideoSync {
  private static String TAG = "YyyyyFffffVideoSync";
  private final Context context;

  public YyyyyFffffVideoSync(Context context) {
    this.context = context;
  }



  public void syncVideo(String cloudId, String fffffVersion) {

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

        String urlDown = "https://" + prefServerIP + "/xxxxxws/sync?";
        String videoPath = "/Yyyyy/Fffff/" + cloudId + "-" + fffffVersion + ".mp4";
        String videoUrl = urlDown + "op=YyyyyFffffUploadSyncAAAAAcloudId=" + cloudId+ "AAAAAfffffVersion=" + fffffVersion;
        xxxxxSyncDownFile.syncDownFile(context, videoPath, videoUrl, AUTHORIZATION);

        Log.d("*** FileSyncTask ***", "videoPath: " + videoPath + "   videoUrl: " + videoUrl);

        return null;
    }
  }

}
