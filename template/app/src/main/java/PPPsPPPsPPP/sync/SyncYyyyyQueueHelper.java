
package ppp.ppp.ppp.sync;

import ppp.ppp.ppp.sync.YyyyyParser;
import ppp.ppp.ppp.pojos.Yyyyy;
import ppp.ppp.ppp.ssl.SSLwithServer;
import ppp.ppp.ppp.alert.XxxxxCryptoUtils;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

/**
 * Lllll
 *
 * This is a SyncYyyyyQueueHelper that processes a local offline
 * sync queue for Yyyyy records.
 * It is a helper that flushes any previously queued (offline)
 * Yyyyy changes to the server.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class SyncYyyyyQueueHelper {


  public static final String TAG = "SyncYyyyyQueue";
  private SSLwithServer sslWithServer;
  private Context context;

  private SharedPreferences sharedPrefs;
  private String prefServerIP = "1.1.1.1";
  private String prefDeviceId = "-1";
  private String url = "";
  private String authorization = "";

  public SyncYyyyyQueueHelper(Context context) {
        this.context = context;
        sslWithServer = SSLwithServer.getInstance(context);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefServerIP = sharedPrefs.getString("prefServerIP", "1.1.1.1");
        prefDeviceId = sharedPrefs.getString("prefDeviceId", "-1");
        String serverUser = sharedPrefs.getString("prefServerUser", "");
        String serverPassword = sharedPrefs.getString("prefServerPass", "");
        if ( !serverUser.isEmpty() && !serverPassword.isEmpty() )
           authorization = serverUser.trim() + ":" + XxxxxCryptoUtils.decrypt(serverPassword);
  }


  protected String doSyncYyyyyQueue() {

    Log.i(TAG, "***************************** My SyncYyyyyServer Beginning server sync");
    String result = "Falure";

    if (prefDeviceId.equals("-1"))
        return result;

    File syncDir = new File(context.getFilesDir() + File.separator + "SyncQueue");

    if (!syncDir.exists() && !syncDir.mkdirs()) {
        Log.i(TAG, "***************************** Can not create SyncQueue " + syncDir.getPath());
        return result;
    }

    File syncFile = new File(syncDir, "YyyyySyncQueue.txt");

    if (!syncFile.exists())
        return "Success";

    Log.i(TAG, "***************************** found file. " + syncFile.getPath());

    String syncFileStr = syncFile.getPath();

    File syncDatafile = null;
    FileInputStream fileInputStream = null;
    FileOutputStream fileOutputStream = null;
    String yyyyyStr = "";

    try{

       syncDatafile = new File(syncFileStr);
       fileInputStream = new FileInputStream(syncDatafile);

       BufferedReader r = new BufferedReader(new InputStreamReader(fileInputStream));

       StringBuilder totalLines = new StringBuilder();
       String line;
       while ((line = r.readLine()) != null) {
           totalLines.append(line);
       }

       yyyyyStr = totalLines.toString();
       fileInputStream.close();

    }catch(Exception e){
        e.printStackTrace();
        return result;
    }finally{
      try{
          if (fileInputStream != null)
              fileInputStream.close();
      } catch (IOException e) {
          Log.e(TAG, "Could not close: " + e.toString());
          return result;
      }
    }
 

    Log.i(TAG, "***************************** Data from Queue file: " + yyyyyStr);
    if (yyyyyStr.isEmpty())
        return "Success";

    url = "https://" + prefServerIP.trim() + "/xxxxxws/rest/syncYyyyys/" + yyyyyStr;

    Log.i(TAG, "***************************** Streaming data from server: " + url);
    result = sslWithServer.sslInputStream(url, authorization);

    if ( !result.equals("Success"))
        return result;

    syncDatafile.delete();

    Log.i(TAG, "***************************** Queue synchronization complete");
       return"Success";

  }

}
