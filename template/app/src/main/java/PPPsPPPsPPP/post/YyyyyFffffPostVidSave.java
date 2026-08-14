package ppp.ppp.ppp;

import ppp.ppp.ppp.YyyyyFffffPostVidSync;
import ppp.ppp.ppp.YyyyyFffffVidThumbSync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

/**
 * Lllll
 *
 * This is a YyyyyFffffPostVidSave helper that processes and saves
 * a post video (plus a thumbnail frame), then triggers sync.
 * It performs background processing that finalizes a captured/selected video,
 * generates a thumbnail from it, and starts the sync.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyFffffPostVidSave {

  private Context context;
  private String theCloudId;
  private String theFffffVersion;
    
  public YyyyyFffffPostVidSave(Context context, String theCloudId, String theFffffVersion) {
    this.context = context;
    this.theCloudId = theCloudId;
    this.theFffffVersion = theFffffVersion;
  }

  public void savePost() {

    VideoAsyncTask task = new VideoAsyncTask(context, theCloudId, theFffffVersion);
    task.execute();
  }

  private static class VideoAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "YyyyyFffffPostVidSave";
    private Context context;
    private String theCloudId;
    private String theFffffVersion;
    private CameraHelper cameraHelper = null;
    private ImageHelper imageHelper = new ImageHelper();
    private int thumbHeight = 150;

    public VideoAsyncTask(Context context, String theCloudId, String theFffffVersion) {
      this.context = context;
      this.theCloudId = theCloudId;
      this.theFffffVersion = theFffffVersion;
      cameraHelper = new CameraHelper(context);
    }

    @Override
    protected Void doInBackground(Void... params) {

      String videoDir = cameraHelper.makeDir("Yyyyy", "Fffff");
      String videoTempFile = videoDir + "/" + "TEMP.mp4";
      String videoNewFile = videoDir + "/" + theCloudId + "-" + theFffffVersion + ".mp4";

      File myTempFile = new File(videoTempFile);
      File myNewFile = new File(videoNewFile);

      MediaMetadataRetriever retriever = new MediaMetadataRetriever();
      Bitmap bitmap = null;
      Bitmap thumbnail = null;

      try {

         retriever.setDataSource(videoTempFile);

         bitmap = retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST);

         int originalWidth = bitmap.getWidth();
         int originalHeight = bitmap.getHeight();
         double originalWidthToHeightRatio =  1.0 * originalWidth / originalHeight;
         int width = (int) (thumbHeight * originalWidthToHeightRatio);

         thumbnail = imageHelper.resizeBitmap(bitmap, width, thumbHeight);

      } catch (IllegalArgumentException e) {
        Log.e(TAG, "*** VideoAsyncTask *** MediaMetadataRetriever.setDataSource() fail:" + e.getMessage());
        e.printStackTrace();
      }

      String jpgStr = videoDir + "/" + theCloudId + "VidThumb" + theFffffVersion + ".jpg";
      File jpgFile = new File(jpgStr);
      if (jpgFile.exists()) {
          Log.e(TAG, "jpgFile.delete: " + jpgStr);
          jpgFile.delete();
      }

      int bufferSize = 1024 * 8;
      try {
        jpgFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(jpgFile);
        final BufferedOutputStream bos = new BufferedOutputStream(fos, bufferSize);
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        fos.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }

      if(myTempFile.renameTo(myNewFile)){
        Log.d("*** VideoAsyncTask ***", "New Video saved: " + myNewFile);
      }else{
        Log.d("*** VideoAsyncTask ***", "Video Rename failed: " + myTempFile);
      }


      YyyyyFffffPostVidSync yyyyyFffffPostVidSync = new YyyyyFffffPostVidSync(context);
      yyyyyFffffPostVidSync.syncVideo(theCloudId, theFffffVersion);

      YyyyyFffffVidThumbSync yyyyyFffffVidThumbSync = new YyyyyFffffVidThumbSync(context);
      yyyyyFffffVidThumbSync.syncVidThumb(theCloudId, theFffffVersion);

      return null;
    }
  }

}
