
package ppp.ppp.ppp.alert;

import ppp.ppp.ppp.alert.XxxxxAlert;
import ppp.ppp.ppp.ssl.SSLwithServer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.util.Date;

/**
 * Lllll
 *
 * This is an AlertServer helper that asynchronously sends alerts
 * to a remote server.
 * It is a utility for pushing configuration/event alerts to the
 * backend server securely in the background.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class AlertServer {

    private final Context context;

    public AlertServer(Context context) {
        this.context = context;
    }


    public void alertToServer(String alertType, String alertSource, String alertMessage) {

      XxxxxAlert alert = new XxxxxAlert();
      alert.setXxxxxAlertType(alertType);
      alert.setXxxxxAlertSource(alertSource);
      alert.setXxxxxAlertMessage(alertMessage);
      alert.setOccurredAt(new Date());

      AlertServerTask task = new AlertServerTask(alert, context);
      task.execute();
    }


  private static class AlertServerTask extends AsyncTask<Void, Void, Void> {

    public static final String TAG = "AlertServer";
    private SSLwithServer sslWithServer;
    private static XxxxxAlert alert;
    private SharedPreferences sharedPrefs;
    private Long prefDeviceId;
    private String prefServerIP = "1.1.1.1";
    private String url = "";
    private String authorization = "";

    private JSONObject jsonObject = new JSONObject();
    private JSONArray jsonArray = new JSONArray();

    public AlertServerTask(XxxxxAlert alert, Context context) {
        this.alert = alert;
        sslWithServer = SSLwithServer.getInstance(context);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefServerIP = sharedPrefs.getString("prefServerIP", "1.1.1.1").trim();
        String deviceIdStr = sharedPrefs.getString("prefDeviceId", "-1");
        prefDeviceId = Long.parseLong(deviceIdStr);
        String serverUser = sharedPrefs.getString("prefServerUser", "");
        String serverPassword = sharedPrefs.getString("prefServerPass", "");
        if ( !serverUser.isEmpty() && !serverPassword.isEmpty() )
           authorization = serverUser.trim() + ":" + XxxxxCryptoUtils.decrypt(serverPassword);
    }

    @Override
    protected Void doInBackground(Void... params) {

        if ( authorization.isEmpty() )
          return null;

        Log.i(TAG, "***************************** Beginning Alert Server");

        if (prefDeviceId.longValue() == -1) {
          Log.i(TAG, "***************************** Sever not connected");
          return null;
        }

        String jsonStr = "[";

        JSONObject jGroup = new JSONObject();

        try {
            jGroup.put("deviceId", prefDeviceId.toString());
            jGroup.put("xxxxxAlertType", alert.getXxxxxAlertType());
            jGroup.put("xxxxxAlertSource", alert.getXxxxxAlertSource()
                            + "-" + prefDeviceId.toString());
            jGroup.put("xxxxxAlertMessage", alert.getXxxxxAlertMessage());
            jGroup.put("occurredAt", Long.toString(alert.getOccurredAt().getTime()));

            jsonStr = jsonStr + jGroup.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonStr = jsonStr + "]";

        try {
          jsonStr = URLEncoder.encode(jsonStr, "ASCII").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
          System.err.println(e);
        }

        url = "https://" + prefServerIP + "/xxxxxws/rest/syncXxxxxAlerts/" + jsonStr;

        Log.i(TAG, "***************************** Streaming data from server: " + url);
        String result = sslWithServer.sslInputStream(url, authorization);

        return null;
    }

  }
}
