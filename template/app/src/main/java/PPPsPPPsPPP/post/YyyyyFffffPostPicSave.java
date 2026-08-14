package ppp.ppp.ppp;

import ppp.ppp.ppp.YyyyyFffffPostPicSync;
import ppp.ppp.ppp.YyyyyFffffPicThumbSync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.File;

/**
 * Lllll
 *
 * This is a YyyyyFffffPostPicSave helper that processes and saves
 * a “post” image (plus thumbnail), then triggers sync.
 * It performs background processing that turns a temporary post image
 * into permanent full-size + thumbnail files and starts the sync.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyFffffPostPicSave {

    private static final String TAG = "YyyyyFffffPostPicSave";
    private final Context context;
    private final String theCloudId;
    private final String theFffffVersion;

    public YyyyyFffffPostPicSave(Context context, String theCloudId, String theFffffVersion) {
        this.context = context;
        this.theCloudId = theCloudId;
        this.theFffffVersion = theFffffVersion;
    }

    public void savePost() {
        PostAsyncTask task = new PostAsyncTask(context, theCloudId, theFffffVersion);
        task.execute();
    }

    private static class PostAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private final String theCloudId;
        private final String theFffffVersion;
        private final CameraHelper cameraHelper;
        private final ImageHelper imageHelper = new ImageHelper();
        private final int maxThumbSize = 150;
        private final int maxPicSize = 1024;

        public PostAsyncTask(Context context, String theCloudId, String theFffffVersion) {
            this.context = context;
            this.theCloudId = theCloudId;
            this.theFffffVersion = theFffffVersion;
            this.cameraHelper = new CameraHelper(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String postDir = cameraHelper.makeDir("Posting", "Posting");
            if (postDir == null) {
                Log.e(TAG, "Failed to create posting directory");
                return false;
            }

            String postTempFile = postDir + "/" + "TEMP.jpg";

            File myTempFile = new File(postTempFile);
            if (!myTempFile.exists()) {
                Log.e(TAG, "Temporary image file does not exist: " + postTempFile);
                return false;
            }

            String imageNewFile = postDir + "/" + theCloudId + "-" + theFffffVersion + ".jpg";
            File imageFile = new File(imageNewFile);
            if (imageHelper.processAndSaveImage( myTempFile, imageFile, maxPicSize) == false)
                return false;

            String newPicThumb = postDir + "/" + theCloudId + "PicThumb" + theFffffVersion + ".jpg";
            File picThumb = new File(newPicThumb);
            if (imageHelper.processAndSaveImage( myTempFile, picThumb, maxThumbSize) == false)
                return false;

            YyyyyFffffPostPicSync yyyyyFffffPostPicSync = new YyyyyFffffPostPicSync(context);
            yyyyyFffffPostPicSync.syncPost(theCloudId, theFffffVersion);

            YyyyyFffffPicThumbSync yyyyyFffffPicThumbSync = new YyyyyFffffPicThumbSync(context);
            yyyyyFffffPicThumbSync.syncPicThumb(theCloudId, theFffffVersion);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Log.e(TAG, "Post processing failed");
            }
        }
    }
}