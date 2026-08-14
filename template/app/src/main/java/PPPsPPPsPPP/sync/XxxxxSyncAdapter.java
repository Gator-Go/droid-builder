
package ppp.ppp.ppp.sync;

import ppp.ppp.ppp.alert.AlertServer;
import ppp.ppp.ppp.ssl.SSLwithServer;
import ppp.ppp.ppp.alert.XxxxxCryptoUtils;

___SYNC_IMPORT___

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Date;

/**
 * Lllll
 *
 * This is the main XxxxxSyncAdapter (extends AbstractThreadedSyncAdapter)
 * that performs the app’s cloud synchronization.
 * It is the core sync engine that keeps local Yyyyy (and related) data
 * in sync with the remote server.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

class XxxxxSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "XxxxxSyncAdapter";
    private SSLwithServer sslWithServer;
    private Context context;
    /**
     * URL to fetch content from during a sync.
     *
     * <p>This points to the Android Developers Blog. (Side note: We highly recommend reading the
     * Android Developer Blog to stay up to date on the latest Android platform developments!)
     */

    private AlertServer alertServer;
    private SharedPreferences sharedPrefs;
    private String prefServerIP = "1.1.1.1";
    private String prefDeviceId = "-1";
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Boolean getServerData = new Boolean(true);

___SYNC_URL___

    private String authorization = "";

    /**
     * Content resolver, for performing database operations.
     */

___SYNC_DEF___

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public XxxxxSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.context = context;
        sslWithServer = SSLwithServer.getInstance(context);
        alertServer = new AlertServer(context);
        ContentResolver mContentResolver = context.getContentResolver();

___SYNC_DEF_NEW___

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public XxxxxSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.context = context;
        sslWithServer = SSLwithServer.getInstance(context);
        alertServer = new AlertServer(context);
        ContentResolver mContentResolver = context.getContentResolver();

___SYNC_DEF_NEW___

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the cloud, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "***************************** My XxxxxSyncAdapter Beginning cloud synchronization");
        StringBuffer syncInfo = new StringBuffer();

        try {
            prefServerIP = sharedPrefs.getString("prefServerIP", "1.1.1.1").trim();
            prefDeviceId = sharedPrefs.getString("prefDeviceId", "-1");
	    getServerData = sharedPrefs.getBoolean("getServerData", true);

            if (prefDeviceId.equals("-1"))
              return;

            String lastSyncDateStr = sharedPrefs.getString("lastSyncDate", "");
            Date lastSyncDate = null;
            try {
                lastSyncDate = dateFormat.parse(lastSyncDateStr);
            } catch (ParseException e) {
                Log.i(TAG,"ERROR - Bad lastSyncDate = " + lastSyncDateStr);
                return;
            }
            Long longLastSync = lastSyncDate.getTime();
            String longLastSyncDate = longLastSync.toString();
            syncInfo.append(" Local last sync date " + lastSyncDateStr + "<br>");

            String serverUser = sharedPrefs.getString("prefServerUser", "");
            String serverPassword = sharedPrefs.getString("prefServerPass", "");
            if ( !serverUser.isEmpty() && !serverPassword.isEmpty() )
               authorization = serverUser.trim() + ":" + XxxxxCryptoUtils.decrypt(serverPassword);

___SYNC_URL_LOAD___

___SYNC_DELETE___

___SYNC_CALL___

        } catch (IOException e) {
            Log.e(TAG, "Error reading from cloud: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }

        String newLastSyncDate = dateFormat.format(new Date());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("lastSyncDate", newLastSyncDate);
        editor.apply();

        Log.i(TAG, "Network synchronization complete");
        alertServer.alertToServer("sync", "XxxxxSyncAdapter", syncInfo.toString());
    }

}
