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
 * This is an Android YyyyyView activity that displays a single Yyyyy record
 * (standard detail view).
 * It is the primary detail screen for viewing a Yyyyy record, with direct access
 * to edit and related media/location features.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyView extends AppCompatActivity {

  private static String TAG = "YyyyyView";
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
    setContentView(R.layout.yfyfy_view);
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

    if (rId == R.id.edit) {
      Log.d(TAG, "Edit ID=" + yyyyy.getId().toString());
      Intent editIntent = new Intent(YyyyyView.this, YyyyyEdit.class);
      editIntent.putExtra("Yyyyy", yyyyy);
___EDIT_SET_PARENT_ONE2MANY_CHILD___
      startActivity(editIntent);

    } else if (rId == R.id.home) {
      Intent homeIntent = new Intent(YyyyyView.this, XxxxxActivity.class);
      startActivity(homeIntent);

    } else if (rId == R.id.back) {
      finish();

___VIEW_CLICK_CAMERA___
___VIEW_CLICK_VIDEO___

___VIEW_CLICK_POST___
___VIEW_CLICK_LOC___
___VIEW_CLICK_CURRENT_LOC___

___VIEW_CLICK_LOC_SERVICE___
___HELP_FOR_LOC_SERVICE___
___VIEW_CLICK_SECURITY___

    }

  }


  // Initiating Sub Menu XML file (view_menu.xml)
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.view_menu, menu);
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

    if (rId == R.id.menu_edit) {
        Log.d(TAG, "Menu Edit ID=" + yyyyy.getId().toString());
        Intent editIntent = new Intent(YyyyyView.this, YyyyyEdit.class);
        editIntent.putExtra("Yyyyy", yyyyy);
___EDIT_SET_PARENT_ONE2MANY_CHILD___
        startActivity(editIntent);
        return true;

    } else if (rId == R.id.menu_home) {

        Intent homeIntent = new Intent(YyyyyView.this, XxxxxActivity.class);
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

    yyyyy = yyyyyDao.getYyyyy(yyyyy);
    Log.d(TAG, "After yyyyyDao ID=" + yyyyy.getId().toString());

    TextView id = (TextView)findViewById(R.id.id); 
    id.setText(res.getString(R.string.id) + yyyyy.getCloudId());

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
___VIEW_SET_CAMERA___
___VIEW_SET_VIDEO___
___VIEW_SET_THUMBNAIL___
___VIEW_SET_POST___
___VIEW_SET_LOC___
___VIEW_SET_CURRENT_LOC___

___VIEW_SET_LOC_SERVICE___
___VIEW_SET_SECURITY_SERVICE___
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
