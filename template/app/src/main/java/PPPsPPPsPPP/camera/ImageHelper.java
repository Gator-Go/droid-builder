package ppp.ppp.ppp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Lllll
 *
 * This is an ImageHelper utility class for loading, resizing, rotating, and saving bitmaps.
 * It is a helper for efficient image loading, resizing, orientation correction, and saving
 * (especially for camera photos).
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class ImageHelper {

    private static final String TAG = "ImageHelper";

    public ImageHelper() {
    }

    public Bitmap getDisplaySizeBitmap(Resources res, File displayFile) {
        if (displayFile == null || !displayFile.exists()) {
            Log.e(TAG, "Display file does not exist: " + (displayFile != null ? displayFile.getPath() : "null"));
            return null;
        }

        DisplayMetrics metrics = res.getDisplayMetrics();
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(displayFile.getAbsolutePath(), options);
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            Log.e(TAG, "Invalid image dimensions for file: " + displayFile.getPath());
            return null;
        }
        options.inSampleSize = calcSampleSize(options.outWidth, options.outHeight, width, height);
        options.inJustDecodeBounds = false;
        Bitmap displayBitmap = BitmapFactory.decodeFile(displayFile.getAbsolutePath(), options);
        if (displayBitmap == null) {
            Log.e(TAG, "Failed to decode bitmap: " + displayFile.getPath());
        }
        return displayBitmap;
    }

    public int calcSampleSize(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public int calcSampleSizeHeight(int height, int reqHeight) {
        int inSampleSize = 1;
        if (height > reqHeight) {
            final int halfHeight = height / 2;
            while ((halfHeight / inSampleSize) >= reqHeight) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null || bitmap.isRecycled()) {
            Log.e(TAG, "Invalid bitmap: " + (bitmap == null ? "null" : "recycled"));
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            Log.e(TAG, "Invalid dimensions: width=" + newWidth + ", height=" + newHeight);
            return null;
        }

        try {
            Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
            float ratioX = newWidth / (float) bitmap.getWidth();
            float ratioY = newHeight / (float) bitmap.getHeight();
            float middleX = newWidth / 2.0f;
            float middleY = newHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            return scaledBitmap;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error resizing bitmap: width=" + newWidth + ", height=" + newHeight, e);
            return null;
        }
    }


    public boolean processAndSaveImage(File tempFile, File imageFile, int maxSize) {
        if (!tempFile.exists()) {
            Log.e(TAG, "Temporary image file does not exist: " + tempFile.getPath());
            return false;
        }
        int rotationAngle = getExifRotation(tempFile);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            Log.e(TAG, "Invalid image dimensions: " + tempFile.getPath());
            return false;
        }
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;


        int imgWidth, imgHeight;

        if (originalWidth > originalHeight) {
            double ratio = originalHeight / (double) originalWidth;
            imgWidth = maxSize;
            imgHeight = (int) (maxSize * ratio);
        } else {
            double ratio = originalWidth / (double) originalHeight;
            imgHeight = maxSize;
            imgWidth = (int) (maxSize * ratio);
        }
        if (imgWidth <= 0 || imgHeight <= 0) {
            Log.e(TAG, "Invalid image dimensions: width=" + imgWidth + ", height=" + imgHeight);
            return false;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = calcSampleSize(originalWidth, originalHeight, imgWidth, imgHeight);
        Bitmap imgBitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        if (imgBitmap == null) {
            Log.e(TAG, "Failed to decode bitmap: " + tempFile.getPath() + " -to- " + imageFile.getPath());
            return false;
        }
        Bitmap imgPost = rotationAngle == 0 ? Bitmap.createScaledBitmap(imgBitmap, imgWidth, imgHeight, true) :
                rotateBitmap(imgBitmap, imgWidth, imgHeight, rotationAngle);
        imgBitmap.recycle();
        if (imgPost == null) {
            Log.e(TAG, "Failed to resize bitmap -" + imageFile.getPath());
            return false;
        }

        if (imageFile.exists()) {
            Log.d(TAG, "Deleting existing ImageFile: " + imageFile.getPath());
            imageFile.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            imgPost.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            Log.e(TAG, "Failed to save PostFile image", e);
            if (imageFile.exists()) {
                imageFile.delete();
            }
            imgPost.recycle();
            return false;
        }

        imgPost.recycle();
        return true;
    }


    private int getExifRotation(File file) {
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to read EXIF data: " + e.getMessage());
            return 0;
        }
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int width, int height, int rotationAngle) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, width, height, matrix, true);
        scaledBitmap.recycle();
        return rotatedBitmap;
    }

}
