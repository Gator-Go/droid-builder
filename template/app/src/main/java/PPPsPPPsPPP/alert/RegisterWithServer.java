
package ppp.ppp.ppp.alert;
import ppp.ppp.ppp.ssl.SSLwithServer;

import android.util.Log;
import android.os.AsyncTask;
import android.content.Context;

/**
 * This class passes Registers to the server.
 *
 */

public class RegisterWithServer extends AsyncTask<Void, Void, String> {

    public static final String TAG = "RegisterWithServer";
    private SSLwithServer sslWithServer;
    private static String url = "";
    private static String authorization = "";

    public RegisterWithServer(Context context, String ip, String user, String passwd) {
        sslWithServer = SSLwithServer.getInstance(context);
        this.authorization = user + ":" + passwd;
        this.url = "https://" + ip + "/xxxxxws/rest/newDevice";
    }


    @Override
    protected void onPostExecute(String result)
    {
        Log.i(TAG, "***************************** IN onPostExecute Result = " + result);
    }

    @Override
    protected String doInBackground(Void... params) {

        Log.i(TAG, "***************************** Streaming data from server: " + url);
        String result = sslWithServer.sslInputStream(url, authorization);

        return result;
    }


}
