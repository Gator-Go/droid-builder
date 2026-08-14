
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

/**
 * Lllll
 *
 * This is a SyncYyyyyServerHelper that pushes a single
 * Yyyyy record to the server.
 * It performs immediate server sync for a Yyyyy change,
 * with offline queuing as a fallback.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class SyncYyyyyServerHelper {


  public void syncYyyyy(Context context, Yyyyy yyyyy) {

    SyncYyyyyServer task = new SyncYyyyyServer(context,yyyyy);
    task.execute();
  }

  private static class SyncYyyyyServer extends AsyncTask<Void, Void, Void> {

    public static final String TAG = "SyncYyyyyServer";
    private SSLwithServer sslWithServer;
    private Context context;

    private SharedPreferences sharedPrefs;
    private String prefServerIP = "1.1.1.1";
    private String prefDeviceId = "-1";
    private String url = "";
    private String authorization = "";

    private Yyyyy yyyyy;

    public SyncYyyyyServer(Context context,Yyyyy yyyyy) {
        this.context = context;
        this.yyyyy = yyyyy;
        sslWithServer = SSLwithServer.getInstance(context);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefServerIP = sharedPrefs.getString("prefServerIP", "1.1.1.1");
        prefDeviceId = sharedPrefs.getString("prefDeviceId", "-1");
        String serverUser = sharedPrefs.getString("prefServerUser", "");
        String serverPassword = sharedPrefs.getString("prefServerPass", "");
        if ( !serverUser.isEmpty() && !serverPassword.isEmpty() )
             authorization = serverUser.trim() + ":" + XxxxxCryptoUtils.decrypt(serverPassword);    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.i(TAG, "***************************** My SyncYyyyyServer Beginning server sync");

        String result = "Falure";

        if (prefDeviceId.equals("-1"))
          return null;

        List<Yyyyy> newYyyyys = new ArrayList<Yyyyy>();
        newYyyyys.add(yyyyy);

        YyyyyParser yyyyyParser = new YyyyyParser();
        String jsonStr = yyyyyParser.parseYyyyyList(newYyyyys);

        url = "https://" + prefServerIP.trim() + "/xxxxxws/rest/syncYyyyys/" + jsonStr;

        Log.i(TAG, "***************************** Streaming data from server: " + url);
        result = sslWithServer.sslInputStream(url, authorization);

        if ( !result.equals("Success")) {

            File syncDir = new File(context.getFilesDir() + File.separator + "SyncQueue");

            if (!syncDir.exists() && !syncDir.mkdirs()) {
              Log.i(TAG, "***************************** Can not create SyncQueue " + syncDir.getPath());
              return null;
            }

            File syncFile = new File(syncDir, "YyyyySyncQueue.txt");

            if (!syncFile.exists()) {
                try {
                    syncFile.createNewFile();
                }
                catch (IOException e) {
                    Log.i(TAG, "***************************** Error while creating empty file. " + syncFile.getPath() + " " + e);
                    return null;
                }
            }

            Log.i(TAG, "***************************** found file. " + syncFile.getPath());

            String syncFileStr = syncFile.getPath();

            File syncDatafile = null;
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;

            try{

               syncDatafile = new File(syncFileStr);
               fileInputStream = new FileInputStream(syncDatafile);

               BufferedReader r = new BufferedReader(new InputStreamReader(fileInputStream));

               StringBuilder totalLines = new StringBuilder();
               String line;
               while ((line = r.readLine()) != null) {
                   totalLines.append(line);
               }
               String yyyyyStr = totalLines.toString();
               fileInputStream.close();
               Log.i(TAG, "***************************** Data from file: " + yyyyyStr);

               yyyyyParser = new YyyyyParser();
               List<Yyyyy> yyyyys = new ArrayList<Yyyyy>();
               if (yyyyyStr != null && !yyyyyStr.isEmpty()) {
                   try {
                      yyyyyStr = URLDecoder.decode(yyyyyStr, "ASCII");
                   } catch (UnsupportedEncodingException e) {
                      System.err.println(e);
                   }
                   yyyyys = yyyyyParser.parse(yyyyyStr);
               }
               yyyyys.add(yyyyy);
               yyyyyStr = yyyyyParser.parseYyyyyList(yyyyys);
               Log.i(TAG, "***************************** Data going into file: " + yyyyyStr);
               syncDatafile.createNewFile();
               fileOutputStream = new FileOutputStream(syncDatafile);
               OutputStreamWriter myOutWriter = new OutputStreamWriter(fileOutputStream);
               myOutWriter.append(yyyyyStr);
               myOutWriter.close();
               fileOutputStream.close();

            }catch(Exception e){
               e.printStackTrace();
            }finally{
              try{
               if (fileInputStream != null)
                 fileInputStream.close();
               if (fileInputStream != null)
                 fileInputStream.close();
              } catch (IOException e) {
                Log.e(TAG, "Could not close: " + e.toString());
              }
            }

        }

        Log.i(TAG, "***************************** New Yyyyy synchronization complete");
        return null;
     }
  }

}
