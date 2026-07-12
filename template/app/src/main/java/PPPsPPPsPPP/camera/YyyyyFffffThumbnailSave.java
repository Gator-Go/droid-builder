package ppp.ppp.ppp;

import ppp.ppp.ppp.YyyyyFffffThumbnailSync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

/**
 * Lllll
 *  
 */

/**
 * This entity bean is used to manage the YYYYY database table.<br>
 * This class follows the POJO model so there is a standard get<br>
 * and set method on each data item.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyFffffThumbnailSave {

  private Context context;
  private String theCloudId;
  private String theFffffVersion;

  public YyyyyFffffThumbnailSave(Context context, String theCloudId, String theFffffVersion) {
    this.context = context;
    this.theCloudId = theCloudId;
    this.theFffffVersion = theFffffVersion;
  }

  public void saveThumbnail() {

    ThumbnailAsyncTask task = new ThumbnailAsyncTask(context, theCloudId, theFffffVersion);
    task.execute();
  }

  private static class ThumbnailAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "YyyyyFffffThumbnailSave";
    private Context context;
    private String theCloudId;
    private String theFffffVersion;
    private CameraHelper cameraHelper = null;
    private ImageHelper imageHelper = new ImageHelper();
    private int thumbHeight = 150;

    public ThumbnailAsyncTask(Context context, String theCloudId, String theFffffVersion) {
      this.context = context;
      this.theCloudId = theCloudId;
      this.theFffffVersion = theFffffVersion;
      cameraHelper = new CameraHelper(context);
    }

    @Override
    protected Void doInBackground(Void... params) {

      String thumbnailDir = cameraHelper.makeDir("Yyyyy", "Fffff");
      String thumbnailTempFile = thumbnailDir + "/" + "TEMP.jpg";
      String thumbnailNewFile = thumbnailDir + "/" + theCloudId + "-" + theFffffVersion + ".jpg";

      File myTempFile = new File(thumbnailTempFile);
      File myNewFile = new File(thumbnailNewFile);

      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(myTempFile.getAbsolutePath(), options);

      int originalWidth = options.outWidth;
      int originalHeight = options.outHeight;
      double originalWidthToHeightRatio =  1.0 * originalWidth / originalHeight;
      int width = (int) (thumbHeight * originalWidthToHeightRatio);

      options.inJustDecodeBounds = false;
      options.inSampleSize = imageHelper.calcSampleSizeHeight(options.outHeight, thumbHeight);
      Bitmap bitmap = BitmapFactory.decodeFile(myTempFile.getAbsolutePath(),options);
      Bitmap thumbtmp = imageHelper.resizeBitmap(bitmap, width, thumbHeight);

      Matrix matrix = new Matrix();
      matrix.postRotate(cameraHelper.getRotationDegree());
      Bitmap thumbnail = Bitmap.createBitmap(thumbtmp, 0, 0, thumbtmp.getWidth(), thumbtmp.getHeight(),
                               matrix, true);


      if (myNewFile.exists()) {
          myNewFile.delete();
      }

      int bufferSize = 1024 * 8;
      try {
        myNewFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(myNewFile);
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
      myTempFile.delete();

      YyyyyFffffThumbnailSync yyyyyFffffThumbnailSync = new YyyyyFffffThumbnailSync(context);
      yyyyyFffffThumbnailSync.syncThumbnail(theCloudId, theFffffVersion);

      return null;
    }
  }

}
