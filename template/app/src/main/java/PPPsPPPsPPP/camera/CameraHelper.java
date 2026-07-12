package ppp.ppp.ppp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Environment;
import android.content.pm.PackageManager;
import android.widget.Toast;
import android.util.Log;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;

public class CameraHelper {

    private static final String TAG = "*** CameraHelper ***";
    private final Context context;
    private final android.content.res.Resources res;

    public CameraHelper(Context context) {
        this.context = context;
        this.res = context.getResources();
    }

    public int getCamera() {
        int cameraId = -1;
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(context, res.getString(R.string.noCamera), Toast.LENGTH_SHORT).show();
        } else {
            int numberOfCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numberOfCameras; i++) {
                CameraInfo info = new CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                    cameraId = i;
                    break;
                }
            }
            if (cameraId < 0) {
                Toast.makeText(context, res.getString(R.string.noFront), Toast.LENGTH_LONG).show();
            }
        }
        Log.d(TAG, "Found camera: " + cameraId);
        return cameraId;
    }

    public int getRotationDegree() {
        int rotation = 0;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                rotation = info.orientation;
                break;
            }
        }
        Log.d(TAG, "Found rotation: " + rotation);
        return rotation;
    }

    public String makeDir(String dir, String subDir) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, res.getString(R.string.noPermission), Toast.LENGTH_LONG).show();
            Log.e(TAG, "WRITE_EXTERNAL_STORAGE permission not granted");
            return null;
        }

        File[] sdDirs = context.getExternalFilesDirs(Environment.DIRECTORY_PICTURES);
        if (sdDirs == null || sdDirs.length == 0) {
            Log.e(TAG, "No external storage directories available");
            return null;
        }

        int pick = sdDirs.length - 1;
        File fileDir = new File(sdDirs[pick], dir);
        Log.d(TAG, "Picture directory: " + fileDir.getPath());

        if (!fileDir.exists() && !fileDir.mkdirs()) {
            Toast.makeText(context, res.getString(R.string.noDir), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Failed to create directory: " + fileDir.getPath());
            return null;
        }

        File pictureFileDir = new File(fileDir, subDir);
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            Toast.makeText(context, res.getString(R.string.noDir), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Failed to create subdirectory: " + pictureFileDir.getPath());
            return null;
        }

        Log.d(TAG, "Found directory: " + pictureFileDir.getPath());
        return pictureFileDir.getPath();
    }

    public File makeFile(String fileStr) {
        if (fileStr == null || fileStr.isEmpty()) {
            Log.e(TAG, "Invalid file path provided");
            return null;
        }

        File photoFile = new File(fileStr);
        try {
            File parentDir = photoFile.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                Log.e(TAG, "Failed to create parent directory: " + parentDir.getPath());
                return null;
            }
            if (photoFile.createNewFile()) {
                photoFile.setWritable(true, false);
                Log.d(TAG, "Created file: " + fileStr);
            } else {
                Log.d(TAG, "File already exists: " + fileStr);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error creating file: " + fileStr, e);
            return null;
        }
        return photoFile;
    }

    public boolean saveImage(File imageFile, Bitmap imageBitmap) {
        if (imageFile == null || imageBitmap == null) {
            Log.e(TAG, "Invalid file or bitmap: file=" + imageFile + ", bitmap=" + imageBitmap);
            return false;
        }

        int bufferSize = 1024 * 8;
        try {
            if (!imageFile.exists() && !imageFile.createNewFile()) {
                Log.e(TAG, "Failed to create file: " + imageFile.getPath());
                return false;
            }
            FileOutputStream fos = new FileOutputStream(imageFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos, bufferSize);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            fos.close();
            Log.d(TAG, "Image saved: " + imageFile.getPath());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving image: " + imageFile.getPath(), e);
            return false;
        } finally {
            if (imageBitmap != null && !imageBitmap.isRecycled()) {
                imageBitmap.recycle();
            }
        }
    }

    public void savePhoto(String tempFile, String newFile) {
        FileAsyncTask task = new FileAsyncTask(tempFile, newFile);
        task.execute();
    }

    private static class FileAsyncTask extends android.os.AsyncTask<Void, Void, Boolean> {
        private final String tempFile;
        private final String newFile;

        public FileAsyncTask(String tempFile, String newFile) {
            this.tempFile = tempFile;
            this.newFile = newFile;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            File myTempFile = new File(tempFile);
            File myNewFile = new File(newFile);

            if (!myTempFile.exists()) {
                Log.e(TAG, "Temporary file does not exist: " + tempFile);
                return false;
            }

            if (myTempFile.renameTo(myNewFile)) {
                Log.d(TAG, "Photo renamed to: " + newFile);
                return true;
            } else {
                Log.e(TAG, "Rename failed: " + tempFile + " to " + newFile);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Log.e(TAG, "File rename operation failed");
            }
        }
    }
}

