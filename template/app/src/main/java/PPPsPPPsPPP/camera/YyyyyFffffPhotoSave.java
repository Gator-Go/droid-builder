package ppp.ppp.ppp;

import ppp.ppp.ppp.YyyyyFffffPhotoSync;
import ppp.ppp.ppp.YyyyyFffffPicThumbSync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

/**
 * Lllll
 *
 * This is a YyyyyFffffPhotoSave helper that processes and saves a captured photo
 * (plus thumbnail) for a Yyyyy/Fffff record, then triggers sync.
 * It does background processing that turns a temporary camera capture into permanent
 * photo + thumbnail files and starts the sync.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyFffffPhotoSave {

  private Context context;
  private String theCloudId;
  private String theFffffVersion;

  public YyyyyFffffPhotoSave(Context context, String theCloudId, String theFffffVersion) {
    this.context = context;
    this.theCloudId = theCloudId;
    this.theFffffVersion = theFffffVersion;
  }

  public void savePhoto() {

    PhotoAsyncTask task = new PhotoAsyncTask(context, theCloudId, theFffffVersion);
    task.execute();
  }

  private static class PhotoAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "YyyyyFffffPhotoSave";
    private Context context;
    private String theCloudId;
    private String theFffffVersion;
    private CameraHelper cameraHelper = null;
    private ImageHelper imageHelper = new ImageHelper();
    private int thumbHeight = 150;
    private int maxPicSize = 1024;
    private int bufferSize = 1024 * 8;

    public PhotoAsyncTask(Context context, String theCloudId, String theFffffVersion) {
      this.context = context;
      this.theCloudId = theCloudId;
      this.theFffffVersion = theFffffVersion;
      cameraHelper = new CameraHelper(context);
    }

    @Override
    protected Void doInBackground(Void... params) {

      String photoDir = cameraHelper.makeDir("Yyyyy", "Fffff");
      String photoTempFile = photoDir + "/" + "TEMP.jpg";
      String photoNewFile = photoDir + "/" + theCloudId + "-" + theFffffVersion + ".jpg";

      File myTempFile = new File(photoTempFile);
      File myNewFile = new File(photoNewFile);

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
      Bitmap thumbnail = imageHelper.resizeBitmap(bitmap, width, thumbHeight);

      String jpgStr = photoDir + "/" + theCloudId + "PicThumb" + theFffffVersion + ".jpg";
      File jpgFile = new File(jpgStr);
      if (jpgFile.exists()) {
          Log.e(TAG, "jpgFile.delete: " + jpgStr);
          jpgFile.delete();
      }

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

      int imgWidth;
      int imgHeight;
      if (originalWidth > originalHeight) {
        double origHeightToWidthRatio =  1.0 * originalHeight / originalWidth;
        imgHeight = (int) (maxPicSize * origHeightToWidthRatio);
        imgWidth = maxPicSize;
      } else {
        double origWidthToHeightRatio =  1.0 * originalWidth / originalHeight;
        imgWidth = (int) (maxPicSize * origWidthToHeightRatio);
        imgHeight = maxPicSize;
      }

      options.inJustDecodeBounds = false;
      options.inSampleSize = imageHelper.calcSampleSize(originalWidth, originalHeight, imgWidth, imgHeight);
      Bitmap imgBitmap = BitmapFactory.decodeFile(myTempFile.getAbsolutePath(),options);
      Bitmap imgPhoto = imageHelper.resizeBitmap(imgBitmap, imgWidth, imgHeight);

      if (myNewFile.exists()) {
          Log.e(TAG, "myNewFile.delete: " + photoNewFile);
          myNewFile.delete();
      }

      try {
        myNewFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(myNewFile);
        final BufferedOutputStream bos = new BufferedOutputStream(fos, bufferSize);
        imgPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        fos.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }


      YyyyyFffffPhotoSync yyyyyFffffPhotoSync = new YyyyyFffffPhotoSync(context);
      yyyyyFffffPhotoSync.syncPhoto(theCloudId, theFffffVersion);

      YyyyyFffffPicThumbSync yyyyyFffffPicThumbSync = new YyyyyFffffPicThumbSync(context);
      yyyyyFffffPicThumbSync.syncPicThumb(theCloudId, theFffffVersion);

      return null;
    }
  }

}
