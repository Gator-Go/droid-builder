
package ppp.ppp.ppp.sync;

import ppp.ppp.ppp.ssl.MultipartUtility;

import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;

import java.util.List;
import java.lang.StringBuilder;

/** Service to handle sync file.
 *
 */
public class XxxxxSyncDownFile {

    public static final String TAG = "XxxxxSyncDownFile";

    public void syncDownFile(Context context, String photoPath, String photoUrl, String authorization) {

        String charset = "UTF-8";
        File[] sdDirs = context.getExternalFilesDirs(Environment.DIRECTORY_PICTURES);
        int pick = sdDirs.length - 1;
        File photoFile = new  File(sdDirs[pick],photoPath);

        boolean photoExists = photoFile.exists();
        for(int l=0; l<5 && photoExists == false; l++){
            SystemClock.sleep(1000);
            photoExists = photoFile.exists();
        }
        if (photoExists == false) {
            Log.i(TAG, "File does not exist after 5 seconds: " + photoFile.getPath());
            return;
        }

        try {
            MultipartUtility multipart = MultipartUtility.getInstance();
            multipart.sslConnection(context, photoUrl, authorization);
            multipart.addFilePart("photoFile", photoFile);
            List<String> response = multipart.finish(); // response from server.

            StringBuilder sb = new StringBuilder();
            for (String s : response)
                sb.append(s + "**");
            Log.i(TAG, "**************** HttpPost Sync Down File Result: " + sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
