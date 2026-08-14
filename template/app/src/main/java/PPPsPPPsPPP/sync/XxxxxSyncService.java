
package ppp.ppp.ppp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Lllll
 *
 * This is the XxxxxSyncService that hosts the app’s SyncAdapter.
 * It is the required service that binds the XxxxxSyncAdapter to
 * Android’s sync framework.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class XxxxxSyncService extends Service {
    private static final String TAG = "XxxxxSyncService";

    private static final Object xxxxxSyncAdapterLock = new Object();
    private static XxxxxSyncAdapter xxxxxSyncAdapter = null;

    /**
     * Thread-safe constructor, creates static {@link SyncAdapter} instance.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "********************************************** Service created XxxxxSyncService");
        synchronized (xxxxxSyncAdapterLock) {
            if (xxxxxSyncAdapter == null) {
                xxxxxSyncAdapter = new XxxxxSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    /**
     * Logging-only destructor.
     */
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    /**
     * Return Binder handle for IPC communication with {@link SyncAdapter}.
     *
     * <p>New sync requests will be sent directly to the SyncAdapter using this channel.
     *
     * @param intent Calling intent
     * @return Binder handle for {@link SyncAdapter}
     */
    @Override
    public IBinder onBind(Intent intent) {
        return xxxxxSyncAdapter.getSyncAdapterBinder();
    }
}
