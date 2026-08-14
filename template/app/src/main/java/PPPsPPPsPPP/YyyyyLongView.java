package ppp.ppp.ppp;

import ppp.ppp.ppp.pojos.Yyyyy;
import ppp.ppp.ppp.daos.YyyyyDao;
___VIEW_IMPORT_ONE2MANY_PARENT___
___VIEW_IMPORT_ONE2MANY_CHILD___

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

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import android.os.Environment;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Lllll
 *
 * This is an Android YyyyyLongView activity that shows a detailed, read-only
 * view of a single Yyyyy record.
 * It is a comprehensive detail screen for inspecting a full Yyyyy record and
 * its related data.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyLongView extends AppCompatActivity {

  private static String TAG = "YyyyyLongView";
  private YyyyyDao yyyyyDao;
___DELETE_DAO_ONE2MANY_PARENT___
  private Yyyyy yyyyy = new Yyyyy();

  private SimpleDateFormat formatter = null;
  private SimpleDateFormat dateTimeFormatter = null;
  private Boolean showButtons = new Boolean(true);
  private Resources res;
___VIEW_CLASS_ONE2MANY_CHILD___

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.yfyfy_long_view);
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

    yyyyyDao = new YyyyyDao(this);
___CREATE_CLASS_ONE2MANY_CHILD___

___LIST_SET_ONE2MANY_CHILD___

___DELETE_CREATE_ONE2MANY_PARENT___
    Intent intent = getIntent();
    yyyyy = (Yyyyy)intent.getParcelableExtra("Yyyyy");
    Log.d(TAG, "GetParcelableExtra ID=" + yyyyy.getId().toString());
  }


  // Will be called via the onClick attribute
  public void onClick(View view) {

    int rId = view.getId();

    if (rId == R.id.home) {

      Intent homeIntent = new Intent(YyyyyLongView.this, XxxxxActivity.class);
      startActivity(homeIntent);

    } else if (rId == R.id.back) {
      finish();
    }

  }


  // Initiating Sub Menu XML file (view_menu.xml)
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.long_view_menu, menu);
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

    if (rId == R.id.menu_home) {

        Intent homeIntent = new Intent(YyyyyLongView.this, XxxxxActivity.class);
        startActivity(homeIntent);
        return true;

    } else if (rId == R.id.menu_return) {

        finish();
 
    } 
    return super.onOptionsItemSelected(item);

  }


  @Override
  protected void onResume() {
    super.onResume();

    Log.d(TAG, "OnResume ID=" + yyyyy.getId().toString());

    if (showButtons.booleanValue() == false) {
      LinearLayout buttonGroup = (LinearLayout)findViewById(R.id.buttonGroup);
      buttonGroup.removeAllViews();
    }

    TextView idText = (TextView)findViewById(R.id.idText);
    idText.setText(res.getString(R.string.idText));
    TextView idData = (TextView)findViewById(R.id.idData); 
    idData.setText(String.format("%s", yyyyy.getId()));

    TextView deviceIdText = (TextView)findViewById(R.id.deviceIdText);
    deviceIdText.setText(res.getString(R.string.deviceIdText));
    TextView deviceIdData = (TextView)findViewById(R.id.deviceIdData); 
    deviceIdData.setText(String.format("%s", yyyyy.getDeviceId()));

    TextView yyyyyIdText = (TextView)findViewById(R.id.yyyyyIdText);
    yyyyyIdText.setText("Yyyyy ID");
    TextView yyyyyIdData = (TextView)findViewById(R.id.yyyyyIdData);
    yyyyyIdData.setText(String.format("%s", yyyyy.getYyyyyId()));

    TextView cloudIdText = (TextView)findViewById(R.id.cloudIdText);
    cloudIdText.setText(res.getString(R.string.cloudIdText));
    TextView cloudIdData = (TextView)findViewById(R.id.cloudIdData); 
    cloudIdData.setText(String.format("%s", yyyyy.getCloudId()));

    TextView lastUpdateText = (TextView)findViewById(R.id.lastUpdateText);
    lastUpdateText.setText(res.getString(R.string.lastUpdateText));
    TextView lastUpdateData = (TextView)findViewById(R.id.lastUpdateData); 
    lastUpdateData.setText(formatter.format(yyyyy.getLastUpdate()));

    TextView deleteFlagText = (TextView)findViewById(R.id.deleteFlagText);
    deleteFlagText.setText(res.getString(R.string.deleteFlagText));
    TextView deleteFlagData = (TextView)findViewById(R.id.deleteFlagData); 
    deleteFlagData.setText(String.format("%s", yyyyy.getDeleteFlag()));


___LIST_SHOW_ONE2MANY_CHILD___

___VIEW_SET_INTEGER___
___VIEW_SET_STRING___
___VIEW_SET_CLOB___
___VIEW_SET_ENUM___
___VIEW_SET_TAG___
___VIEW_SET_DOUBLE___
___VIEW_SET_MONEY___
___VIEW_SET_DATE___
___VIEW_SET_DATE_TIME___
___VIEW_SET_BOOLEAN___

  }

  @Override
  protected void onPause() {
    super.onPause();
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
