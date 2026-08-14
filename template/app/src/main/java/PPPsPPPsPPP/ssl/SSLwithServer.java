
package ppp.ppp.ppp.ssl;

import android.util.Log;
import android.os.AsyncTask;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import android.util.Base64;
import java.util.Date;
import javax.net.ssl.HttpsURLConnection;

/**
 * Lllll
 *
 * This is a singleton SSLwithServer helper for making
 * authenticated HTTPS GET requests.
 * It is the core utility used by the app to communicate
 * with the backend server over SSL.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class SSLwithServer
{

    private static final String TAG = "SSLwithServer";
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds
    private static Context myContext;
    private static SSLwithServer mInstance = null;

    private SSLwithServer(Context context) {
      this.myContext = context;
    }

    public static SSLwithServer getInstance(Context context) {
      if (mInstance == null) {
          mInstance = new SSLwithServer(context);
      }
      return mInstance;
    }

    public static String sslInputStream(String url, String authorization) {
      HttpsURLConnection urlConnection = null;
      InputStream stream = null;
      try {
        URL myUrl = new URL(url);
        urlConnection = (HttpsURLConnection) myUrl.openConnection();

        // Add your authorization header
        urlConnection.setRequestProperty("Authorization", "Basic " +
                Base64.encodeToString(authorization.getBytes(), Base64.NO_WRAP));

        urlConnection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
        urlConnection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);

        urlConnection.connect();

        stream = urlConnection.getInputStream();
        String result = convertStreamToString(stream);
        Log.i(TAG, "Result = " + result);

        return result;

      } catch (Exception ex) {
        Log.e(TAG, "Connection failed: " + ex.toString(), ex);
        return "-1";
      } finally {
        if (stream != null) {
            try { stream.close(); } catch (IOException ignored) {}
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
      }
    }

    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
