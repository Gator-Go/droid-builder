package ppp.ppp.ppp;

import ppp.ppp.ppp.FffffFffffVideoVidThumbSync;
import ppp.ppp.ppp.FffffFffffVideoVideoSync;
import ppp.ppp.ppp.alert.AlertServer;

import ppp.ppp.ppp.pojos.Yyyyy;
import ppp.ppp.ppp.daos.YyyyyDao;
import ppp.ppp.ppp.pojos.Fffff;
import ppp.ppp.ppp.daos.FffffDao;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.ContentResolver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import android.app.PendingIntent;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.graphics.Color;

import android.os.Bundle;
import android.os.IBinder;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.IllegalArgumentException;
import android.provider.MediaStore;

import android.media.MediaRecorder;
import android.media.MediaMetadataRetriever;
import android.app.Notification;
import androidx.core.app.NotificationCompat;
import android.graphics.PixelFormat;
import android.graphics.Bitmap;
import android.media.CamcorderProfile;
import android.os.Build;
import android.widget.LinearLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.Surface;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaCodec;
import android.util.Size;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class YyyyyFffffVideoSecurity extends Service {

  private static final String TAG = "YyyyyFffffVideoSecurity";
  private Yyyyy yyyyy;
  private YyyyyDao yyyyyDao;
  private Fffff fffff;
  private FffffDao fffffDao;
  private Resources res;

  private String cameraId = null;
  private CameraDevice cameraDevice = null;
  private CameraManager cameraManager;
  private CaptureRequest mCaptureRequest = null;
  private CameraCaptureSession mSession = null;
  private Semaphore mCameraOpenCloseLock = new Semaphore(1);

  private Size recSize = null;
  private MediaRecorder mediaRecorder = null;

  private CameraHelper cameraHelper = null;
  private boolean recordingStatus;
  private String tempName;

  private MyTimerTask myTask = new MyTimerTask();
  private Timer myTimer = new Timer();
  private HandlerThread mBackgroundThread;
  private Handler mBackgroundHandler;
  private PowerManager.WakeLock wakeLock;
  private AlertServer alertServer;

  public YyyyyFffffVideoSecurity() {

  }

  @Override
  public IBinder onBind(Intent intent) {
      return null;
  }


  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    res = getResources();
    if (Build.VERSION.SDK_INT >= 26) {

      NotificationManager notificationManager =
            (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
      NotificationChannel channel = new NotificationChannel("default",
                                                          "Security Channel",
                                                          NotificationManager.IMPORTANCE_DEFAULT);
      channel.setDescription("Security Fffff App");
      notificationManager.createNotificationChannel(channel);
    }

    Intent fffffStartService = new Intent(this, YyyyyView.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, fffffStartService, PendingIntent.FLAG_IMMUTABLE);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
    builder.setContentTitle("Security");
    builder.setContentText("Security Fffff App");
    builder.setSmallIcon(R.drawable.logo);
    builder.setTicker("Security Ticker");
    builder.setContentIntent(pendingIntent);

    Notification notification = builder.build();
    startForeground(1, notification);

    PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YyyyyFffffVideoSecurity::LockTag");
    wakeLock.acquire();

    fffffDao = new FffffDao(this);
    recordingStatus = false;

    yyyyy = (Yyyyy)intent.getParcelableExtra("Yyyyy");
    cameraHelper = new CameraHelper(getApplicationContext());
    alertServer = new AlertServer(getApplicationContext());

    startBackgroundThread();
    cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

    myTimer.scheduleAtFixedRate(myTask, 0, 30000);
    alertServer.alertToServer("info", TAG + ":onStartCommand", "YyyyyFffffVideoSecurity Started");

    return START_REDELIVER_INTENT;
  }

  @Override
  public void onDestroy() {

     wakeLock.release();
     myTimer.cancel();
     super.onDestroy();
     closeCamera();
     stopBackgroundThread();
     alertServer.alertToServer("info", TAG + ":onDestroy", "YyyyyFffffVideoSecurity Destroyed");
  }


  private void startBackgroundThread() {
    mBackgroundThread = new HandlerThread("CameraBackground");
    mBackgroundThread.start();
    mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
  }

  private void stopBackgroundThread() {
    mBackgroundThread.quitSafely();
    try {
        mBackgroundThread.join();
        mBackgroundThread = null;
        mBackgroundHandler = null;
    } catch (InterruptedException e) {
        alertServer.alertToServer("error", TAG + ":stopBackgroundThread", e.getMessage());
    }
  }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mSession) {
		mSession.stopRepeating();
                mSession.close();
                mSession = null;
            }
            if (null != mediaRecorder) {
                mediaRecorder.stop();
                mediaRecorder.release();
            }
            if (null != mCaptureRequest) {
                mCaptureRequest = null;
            }
            if (null != cameraDevice) {
                cameraDevice.close();
                cameraDevice = null;
            }

        } catch (Exception e) {
             alertServer.alertToServer("error", TAG + ":closeCamera", e.getMessage());
        } finally {
            mCameraOpenCloseLock.release();
        }
    }


  class MyTimerTask extends TimerTask {

      public void run() {

          if (recordingStatus == true) {

	      closeCamera();

              CreatePictureTask task = new CreatePictureTask(getApplicationContext(),
                  yyyyy, fffff, fffffDao, cameraHelper, tempName, alertServer);
              task.execute();
          }

          tempName = "Temp" + System.currentTimeMillis();
          openCameraDevice();
          setupMediaRecorder();
          captureRequestBuilder();
          startRecording();

      }
  }


  private void openCameraDevice() {

    try {

        String[] cameras = cameraManager.getCameraIdList();
	cameraId = cameras[0];

	if (recSize == null) {

          CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
          StreamConfigurationMap configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
          Size[] sizes = configs.getOutputSizes(MediaCodec.class);
	  int x = sizes.length;
	  for(int xx = 0; xx < x ; xx++) {
		Log.e(TAG, "%%%%%%%%%%%%%%%%%%%%%%% openCameraDevice size" + xx + " : " + sizes[xx].toString());
		if (sizes[xx].getWidth() <= 640) {
		   recSize = sizes[xx];
		   break;
		}
	  }
	}

        if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
            throw new RuntimeException("Time out waiting to lock camera opening.");
        }
        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
           cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {

             @Override
             public void onOpened(CameraDevice camera) {
		        mCameraOpenCloseLock.release();
                cameraDevice = camera;
             }

             @Override
             public void onDisconnected(CameraDevice camera) {
		        mCameraOpenCloseLock.release();
                camera.close();
	            cameraDevice = null;
             }

             @Override
             public void onError(CameraDevice camera, int error) {
		        mCameraOpenCloseLock.release();
	            camera.close();
	            cameraDevice = null;
             }
           }, mBackgroundHandler);
        }  else {
           throw new RuntimeException(res.getString(R.string.noPermission)); 
        }
    } catch (Exception e) {
        alertServer.alertToServer("error", TAG + ":openCameraDevice", e.getMessage());
    }

  }



  private void setupMediaRecorder() {
      try {
          mediaRecorder = new MediaRecorder();

	  mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	  mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);

	  mediaRecorder.setOrientationHint(90);

          // QUALITY_HIGH or QUALITY_LOW
          CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
          profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
          profile.videoFrameWidth = recSize.getWidth();
          profile.videoFrameHeight = recSize.getHeight();
          profile.videoCodec = MediaRecorder.VideoEncoder.H264;
          profile.audioCodec = MediaRecorder.AudioEncoder.AAC;
          mediaRecorder.setProfile(profile);

          String fffffDir = cameraHelper.makeDir("Fffff", "FffffVideo");
          String fffffStr = fffffDir + "/" + tempName + ".mp4";
          File fffffStrFile = new File(fffffStr);
          if (fffffStrFile.exists()) {
              fffffStrFile.delete();
          }

          mediaRecorder.setOutputFile(fffffStr);
          mediaRecorder.prepare();

      } catch (IllegalStateException e) {
          alertServer.alertToServer("error", TAG + ":setupMediaRecorder", e.getMessage());
      } catch (IOException e) {
          alertServer.alertToServer("error", TAG + ":setupMediaRecorder", e.getMessage());
      }
  }


  private void captureRequestBuilder() {
      try {

          CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
          captureRequest.addTarget(mediaRecorder.getSurface());
          mCaptureRequest = captureRequest.build();

          List<Surface> list = new ArrayList<>();
          list.add(mediaRecorder.getSurface());
          cameraDevice.createCaptureSession(list, new CameraCaptureSession.StateCallback() {

              @Override
              public void onConfigured(CameraCaptureSession session) {
                  mSession = session;
              }

              @Override
              public void onConfigureFailed(CameraCaptureSession session) {
                  mSession = session;
              }

          }, mBackgroundHandler);

      } catch (Exception e) {
           alertServer.alertToServer("error", TAG + ":captureRequestBuilder", e.getMessage());
      }
  }


  private void startRecording() {

      recordingStatus = true;
      mediaRecorder.start(); 
      try {
        mSession.setRepeatingRequest(mCaptureRequest, new CameraCaptureSession.CaptureCallback() {

           @Override
           public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
                super.onCaptureStarted(session, request, timestamp, frameNumber);
           }

           @Override
           public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
           }

        }, mBackgroundHandler);
      } catch (Exception e) {
          alertServer.alertToServer("error", TAG + ":startRecording", e.getMessage());
      }
  }


  private static class CreatePictureTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Yyyyy yyyyy;
    private Fffff fffff;
    private FffffDao fffffDao;
    private CameraHelper cameraHelper;
    private String tempName;
    private AlertServer alertServer;

    public CreatePictureTask(Context context, Yyyyy yyyyy, Fffff fffff, FffffDao fffffDao,
			 CameraHelper cameraHelper, String tempName, AlertServer alertServer)
    {
      this.context = context;
      this.yyyyy = yyyyy;
      this.fffff = fffff;
      this.fffffDao = fffffDao;
      this.cameraHelper = cameraHelper;
      this.tempName = tempName;
      this.alertServer = alertServer;
      SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected Void doInBackground(Void... params) {

      MediaMetadataRetriever retriever = new MediaMetadataRetriever();
      Bitmap bitmap = null;
      Bitmap thumbnail = null;
      String mp4Dir = cameraHelper.makeDir("Fffff", "FffffVideo");
      String mp4Str = mp4Dir + "/" + tempName + ".mp4";

      RgbMotionDetection detector = new RgbMotionDetection();

      try {
//        Log.e(TAG, "%%%%%%%%%%%%%%%%%%%%%%% retriever.setDataSource: " + mp4Str);
         retriever.setDataSource(mp4Str);
         String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
         long videoDuration = Long.parseLong(time);// This will give time in millesecond
//        Log.e(TAG, "%%%%%%%%%%%%%%%%%%%%%%% VideoDuration: " + videoDuration);

         boolean detected = false;
         for (long i = 3000; i <= videoDuration; i += 3000) {

             bitmap = retriever.getFrameAtTime(i * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
//             Log.e(TAG, "%%%%%%%%%%%%%%%%%%%%%%% retriever time: "+ i + "  " + System.currentTimeMillis());

             int originalWidth = bitmap.getWidth();
             int originalHeight = bitmap.getHeight();
             double originalWidthToHeightRatio =  1.0 * originalWidth / originalHeight;
             int height = 150;
             int width = (int) (150 * originalWidthToHeightRatio);

             thumbnail = Bitmap.createScaledBitmap(bitmap, width, height, false);

             int[] rgb = new int[width * height];
             thumbnail.getPixels(rgb, 0, width, 0, 0, width, height);
             detected = detector.detect(rgb, width, height);

             if (detected) break;
         }
	try {
             retriever.release();
        } catch (IOException e) {
             alertServer.alertToServer("error", TAG + ":retriever.release", e.getMessage());
        }
         if (detected) {
//             Log.e(TAG, "%%%%%%%%%%%%%%%%%%%%%%% MOTION DETECTED: ");
             createFffff();
             File mp4File = new File(mp4Str);
             String mp4Save = mp4Dir + "/" + fffff.getFffffVideoName() + ".mp4";
             File mp4FileSave = new File(mp4Save);
             if (mp4FileSave.exists()) mp4FileSave.delete();
             if(mp4File.renameTo(mp4FileSave)){
//                 Log.e(TAG, "%%%%%%%%%%%%%%%%%%%%%%% New Video saved: " + mp4FileSave);
             }else{
//                 Log.e(TAG, "%%%%%%%%%%%%%%%%%%%%%%% Video Rename failed: " + mp4File);
             }
             createPhoto(thumbnail);

             FffffFffffVideoVideoSync fffffFffffVideoVideoSync = new FffffFffffVideoVideoSync(context);
             fffffFffffVideoVideoSync.syncVideo(fffff.getCloudId(), fffff.getFffffVideoVersion().toString());

             FffffFffffVideoVidThumbSync fffffFffffVideoVidThumbSync = new FffffFffffVideoVidThumbSync(context);
             fffffFffffVideoVidThumbSync.syncVidThumb(fffff.getCloudId(), fffff.getFffffVideoVersion().toString());

         } else {
             File mp4File = new File(mp4Str);
             mp4File.delete();
         }

      } catch (IllegalArgumentException e) {
        alertServer.alertToServer("error", TAG + ":doInBackground", e.getMessage());
      }

      return null;
    } 


    public void createFffff() {
              fffff = new Fffff();
              fffff.setFffffDate(new Date());
              fffff.setSave(new Integer(0));
              fffff.setFffffVideoVersion(new Integer(1));
              fffff.setYyyyyId(yyyyy.getId());
              fffff.setYyyyyCloudId(yyyyy.getCloudId());
              fffff = fffffDao.createFffff(fffff);
    }


    public void createPhoto(Bitmap bitmap) {

      String jpgDir = cameraHelper.makeDir("Fffff", "FffffVideo");
      String theFffffVersion = fffff.getFffffVideoVersion().toString();
      String jpgStr = jpgDir + "/" + fffff.getCloudId() + "VidThumb" + theFffffVersion + ".jpg";
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        fos.close();
      } catch (FileNotFoundException e) {
        alertServer.alertToServer("error", TAG + ":createPhoto", e.getMessage());
      } catch (IOException e) {
        alertServer.alertToServer("error", TAG + ":createPhoto", e.getMessage());
      }
    }

  }

}
