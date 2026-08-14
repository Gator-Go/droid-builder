
package ppp.ppp.ppp.ssl;

import android.os.Environment;
import android.content.Context;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import android.util.Log;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import javax.net.ssl.HttpsURLConnection;
import android.util.Base64;

/**
 * Lllll
 *
 * This is a SyncServerFile singleton that downloads a file
 * from the server over HTTPS and saves it locally.
 * It is a helper used to pull files (photos, videos, etc.)
 * down from the server during sync.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class SyncServerFile {
    public static final String TAG = "SyncServerFile";
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000; // 15 seconds
    private static final int NET_READ_TIMEOUT_MILLIS = 10000; // 10 seconds
    private static SyncServerFile mInstance = null;

    private SyncServerFile() {
    }

    public static SyncServerFile getInstance() {
        if (mInstance == null) {
            mInstance = new SyncServerFile();
        }
        return mInstance;
    }

    public void syncFile(Context context, String photoPath, String photoUrl, String authorization) {
        File[] sdDirs = context.getExternalFilesDirs(Environment.DIRECTORY_PICTURES);
        int pick = sdDirs.length - 1;
        int index = photoPath.lastIndexOf('/');
        String photoDirPath = photoPath.substring(0, index);
        File photoDirFile = new File(sdDirs[pick], photoDirPath);
        photoDirFile.mkdirs();
        File photoFile = new File(sdDirs[pick], photoPath);

        Log.i(TAG, "**************** File Path: " + photoFile.toString());
        Log.i(TAG, "**************** File URL: " + photoUrl);

        HttpsURLConnection urlConnection = null;
        InputStream in = null;
        try {
            URL url = new URL(photoUrl);
            urlConnection = (HttpsURLConnection) url.openConnection();

            // NO custom SSLContext or SSLSocketFactory needed — system default is used

            urlConnection.setRequestProperty("Authorization", "Basic " +
                    Base64.encodeToString(authorization.getBytes(), Base64.NO_WRAP));
            urlConnection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
            urlConnection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            urlConnection.connect();

            in = new BufferedInputStream(urlConnection.getInputStream());
            copyInputStreamToFile(in, photoFile);

        } catch (Exception e) {
            Log.e(TAG, "Failed to sync file from " + photoUrl, e);
            // Optionally show a user message or retry logic here
        } finally {
            if (in != null) {
                try { in.close(); } catch (IOException ignored) {}
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error writing file: " + file.getPath(), e);
        } finally {
            if (out != null) {
                try { out.close(); } catch (IOException ignored) {}
            }
            if (in != null) {
                try { in.close(); } catch (IOException ignored) {}
            }
        }
    }
}
