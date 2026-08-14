package ppp.ppp.ppp;

import ppp.ppp.ppp.pojos.Yyyyy;
import ppp.ppp.ppp.daos.YyyyyDao;
import ppp.ppp.ppp.checks.DataCheck;
import ppp.ppp.ppp.CapturePhoto;
___VIEW_IMPORT_ENUM___
___VIEW_IMPORT_TAG___
___VIEW_IMPORT_ONE2MANY_CHILD___
___PHOTO_SAVE_INCLUDE_CAMERA___
___PHOTO_SAVE_INCLUDE_VIDEO___
___PHOTO_SAVE_INCLUDE_THUMBNAIL___
___PHOTO_SAVE_INCLUDE_POST___

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.text.SpannableString;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import android.provider.MediaStore;
import android.graphics.Bitmap;

import android.net.Uri;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Lllll
 *
 * This is an Android YyyyyAdd activity for creating a new Yyyyy record.
 * It is a “Add” form screen for inserting a new data entity with
 * rich field support (including media and location).
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyAdd extends AppCompatActivity {

  private static final String TAG = "YyyyyAdd";
  private SimpleDateFormat formatter = null;
  private SimpleDateFormat dateTimeFormatter = null;
  private Boolean showButtons = new Boolean(true);
  private Resources res;
  private YyyyyDao yyyyyDao;
___DAO_ONE2MANY_CHILD___
___DAO_DEF_TAG___
  private Yyyyy yyyyy = new Yyyyy();
  private DataCheck dataCheck = new DataCheck();
  private CameraHelper cameraHelper = null;
  final private int CAMERA_PERMISSIONS = 200;
  final private int LOCATION_PERMISSIONS = 400;

  private String whichStr = "";
  private String whichType = "";
  private String whichField = "";
___DEF_PREF_ONE2MANY_CHILD___
___DEF_PREF_ENUM___
___DEF_PREF_TAG___
___EDIT_FLAG_CAMERA___
___EDIT_FLAG_VIDEO___
___EDIT_FLAG_THUMBNAIL___
___EDIT_FLAG_POST___

  private static final int VIDEO_PIC_REQUEST = 1;
  private static final int UPLOAD_REQUEST = 42;

  private GpsTracker gpsTracker = null;
___VIEW_CLASS_ONE2MANY_CHILD___
___EDIT_DATE___
___EDIT_DATE_TIME___

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.mytheme);
    setContentView(R.layout.yfyfy_add);
    res = getResources();

    // Initialize Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    if (toolbar != null) {
        setSupportActionBar(toolbar);
    }

    // Initialize SharedPreferences
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    boolean showButtons = prefs.getBoolean("showButtons", true);
    if (!showButtons) {
        LinearLayout buttonGroup = findViewById(R.id.buttonGroup);
        if (buttonGroup != null) {
            buttonGroup.setVisibility(View.GONE);
        }
    }

    String dateFormat = prefs.getString("prefDateFormat", "yyyy/MM/dd");
    formatter = new SimpleDateFormat (dateFormat);
    dateTimeFormatter = new SimpleDateFormat (dateFormat + " hh:mm");
    showButtons = prefs.getBoolean("showButtons", true);
___ASK_CAMERA_PERM___
___ASK_POST_PERM___
___ASK_THUMBNAIL_PERM___
___ASK_VIDEO_PERM___
    
    yyyyyDao = new YyyyyDao(this);
___DAO_CREATE_ONE2MANY_CHILD___
___CREATE_CLASS_ONE2MANY_CHILD___
___DAO_NEW_TAG___
 
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    int hour = c.get(Calendar.HOUR);
    int minute = c.get(Calendar.MINUTE);
    cameraHelper = new CameraHelper(this);

___ADD_SET_INTEGER___
___ADD_SET_STRING___
___ADD_SET_CLOB___
___ADD_SET_ENUM___
___ADD_SET_TAG___
___ADD_SET_DOUBLE___
___ADD_SET_MONEY___
___ADD_SET_DATE___
___ADD_SET_DATE_TIME___
___ADD_SET_CAMERA___
___ADD_SET_VIDEO___
___ADD_SET_THUMBNAIL___
___ADD_SET_POST___
___ADD_SET_LOC___
___ADD_SET_CURRENT_LOC___
___ADD_SET_ONE2MANY_CHILD___

    // Set up button clicks
    findViewById(R.id.back).setOnClickListener(v -> goBack());
    findViewById(R.id.save).setOnClickListener(v -> goSave());
___PHOTO_BUTTON_CAMERA_CLICK___
___PHOTO_BUTTON_VIDEO_CLICK___
___PHOTO_BUTTON_THUMBNAIL_CLICK___
___PHOTO_BUTTON_POST_CLICK___
___DATE_BUTTON_CLICK___

    if (savedInstanceState != null) {
        whichStr = savedInstanceState.getString("whichStr", "");
        whichType = savedInstanceState.getString("whichType", "");
        whichField = savedInstanceState.getString("whichField", "");
___GET_PREF_ONE2MANY_CHILD___
___GET_PREF_ENUM___
___GET_PREF_TAG___
    }

  }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("whichStr", whichStr);
        outState.putString("whichType", whichType);
        outState.putString("whichField", whichField);
___SAVE_PREF_ONE2MANY_CHILD___
___SAVE_PREF_ENUM___
___SAVE_PREF_TAG___
    }

    private void goBack() {
        finish();
    }

___PHOTO_BUTTON_CAMERA___
___PHOTO_BUTTON_VIDEO___
___PHOTO_BUTTON_THUMBNAIL___
___PHOTO_BUTTON_POST___
___LOAD_LOC_BUTTON___
___LOAD_CURRENT_LOC_BUTTON___

    private void goSave() {
       boolean errors = false;

___ADD_LOAD_INTEGER___
___ADD_LOAD_STRING___
___ADD_LOAD_CLOB___
___ADD_LOAD_ENUM___
___ADD_LOAD_TAG___
___ADD_LOAD_DOUBLE___
___ADD_LOAD_MONEY___
___EDIT_LOAD_DATE___
___EDIT_LOAD_DATE_TIME___
___EDIT_LOAD_BOOLEAN___
___EDIT_LOAD_LOC___
___EDIT_LOAD_CURRENT_LOC___
___ADD_LOAD_ONE2MANY_CHILD___
___ADD_LOAD_LOC_SERVICE___
___ADD_LOAD_SECURITY___

        if (errors)
           return;

___ADD_PHOTO_VERSION_CAMERA___
___ADD_PHOTO_VERSION_VIDEO___
___ADD_PHOTO_VERSION_THUMBNAIL___
___ADD_PHOTO_VERSION_POST___

      yyyyy = yyyyyDao.createYyyyy(yyyyy);

___PHOTO_SAVE_CAMERA___
___PHOTO_SAVE_VIDEO___
___PHOTO_SAVE_THUMBNAIL___
___PHOTO_SAVE_POST___

      finish();

    }

___EDIT_PICKER_DATE___
___EDIT_PICKER_DATE_TIME___


  // Initiating Sub Menu XML file (edit_menu.xml)
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.edit_menu, menu);
      return true;
  }

  /**
   * Event Handling for Individual menu item selected
   * Identify single menu item by it's id
   * */
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int rId = item.getItemId();

    if (rId == R.id.menu_save) {

        goSave();

    } else if (rId == R.id.menu_return) {
       finish();

    }
    return super.onOptionsItemSelected(item);
 
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

    if ((requestCode == UPLOAD_REQUEST || requestCode == VIDEO_PIC_REQUEST) && resultCode == RESULT_OK) {
        ContentResolver contentResolver = getContentResolver();
        if (contentResolver == null) {
            Log.e(TAG, "ContentResolver is null");
            Toast.makeText(this, "Internal error", Toast.LENGTH_LONG).show();
            return;
        }
        Uri fileData = intent.getData();
        boolean fileSelected = false;
        boolean errors = false;

        if (fileData != null) {
            Log.d(TAG, "fileData found in onActivityResult");
            String mimeType = contentResolver.getType(fileData);
            String extension = null;
            whichType = null;

            if (mimeType != null && mimeType.contains("video/mp4")) {
                Log.d(TAG, "vid data found in onActivityResult");
                whichType = "vid";
                extension = ".mp4";
            } else if (mimeType != null && mimeType.contains("image/jpeg")) {
                Log.d(TAG, "pic data found in onActivityResult");
                whichType = "pic";
                extension = ".jpg";
            }

            if (whichType != null) {
                Log.d(TAG, "whichType != null in onActivityResult :" + whichType);
                whichStr = whichStr + extension;
                fileSelected = true;
___PHOTO_READY___
___THUMBNAIL_READY___
___VIDEO_READY___
___POST_READY___

                // Validate file by attempting to open an InputStream
                try (InputStream inputStream = contentResolver.openInputStream(fileData)) {
                    if (inputStream == null || inputStream.available() <= 0) {
                        Log.e(TAG, "Invalid or empty file");
                        errors = true;
                    } else {
                        Log.d(TAG, "before PostTask:" + whichStr);
                        errors = PostTask.uploadTask(inputStream, whichStr);
                        Log.d(TAG, "after PostTask:" + whichStr);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error accessing file: " + e.getMessage());
                    e.printStackTrace();
                    errors = true;
                }
            } else {
                Log.w(TAG, "Unsupported file type: " + mimeType);
                whichType = "";
                errors = true;
            }

            if (errors) {
                Log.d(TAG, "errors with:" + whichStr);
                Toast.makeText(this, getString(R.string.noPostFile), Toast.LENGTH_LONG).show();
            }
        }

        if (!fileSelected) {
            Toast.makeText(this, getString(R.string.noCamera), Toast.LENGTH_LONG).show();
        }
    }

    if (resultCode == RESULT_CANCELED) {
	if (whichType.equals("vid")) {
            String vidTemp = whichStr + ".mp4";
            File vidFile = new File(vidTemp);
            FileDeleteTask vidTask = new FileDeleteTask(vidFile);
            vidTask.execute();
	}

___PHOTO_NOT_READY___
___THUMBNAIL_NOT_READY___
___VIDEO_NOT_READY___
___POST_NOT_READY___

    }
    super.onActivityResult(requestCode, resultCode, intent);
  }


  @Override
  protected void onResume() {
    super.onResume();

    Log.d(TAG, "onResume whichType:" + whichType + " whichStr: " + whichStr);
    if (whichType.equals("pic")) {
        File whichFile = new File(whichStr);
        if (whichFile.exists()) {
            Log.d(TAG, "onResume whichFile exists");
___PHOTO_READY___
___THUMBNAIL_READY___
___POST_READY___

        } else {
            Log.d(TAG, "onResume File not found");
            whichStr = "";
___PHOTO_NOT_READY___
___THUMBNAIL_NOT_READY___
___POST_NOT_READY___

        }
    }

    if (showButtons.booleanValue() == false) {
      LinearLayout buttonGroup = (LinearLayout)findViewById(R.id.buttonGroup);
      buttonGroup.removeAllViews();
    }
___START_LOC___
___START_CURRENT_LOC___
  }

  @Override
  protected void onPause() {
    super.onPause();
___STOP_LOC___
___STOP_CURRENT_LOC___
  }


  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         switch (requestCode) {
             case CAMERA_PERMISSIONS:
                 if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     Toast.makeText(this, permissions[0] + " Now Allowed", Toast.LENGTH_SHORT)
                             .show();
                 } else {
                     // Permission Denied
                     Toast.makeText(this, permissions[0] + " Denied: ", Toast.LENGTH_SHORT)
                             .show();
                 }
                 break;
___LOC_PERMISSION_GRANTED___
             default:
                 super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         }
   }



  private void helpAlert(String helpText) {
      ContextThemeWrapper themedContext = new ContextThemeWrapper(this, R.style.MyDialogStyle);
      AlertDialog.Builder helpAlert = new AlertDialog.Builder(themedContext);
      helpAlert.setTitle(getString(R.string.helpText));

      SpannableString message = new SpannableString(helpText);
      message.setSpan(new ForegroundColorSpan(Color.WHITE), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      helpAlert.setMessage(message);

      helpAlert.setCancelable(false);
      helpAlert.setPositiveButton(getString(R.string.ok), (dialog, which) -> dialog.dismiss());

      AlertDialog helpAlertDialog = helpAlert.create();
      helpAlertDialog.show();
      helpAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
  }

} 
