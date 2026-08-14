package ppp.ppp.ppp;

import ppp.ppp.ppp.pojos.Yyyyy;
import ppp.ppp.ppp.daos.YyyyyDao;
import ppp.ppp.ppp.checks.DataCheck;
___VIEW_IMPORT_ENUM___
___VIEW_IMPORT_TAG___
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
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Lllll
 *
 * This is an Android YyyyySearch activity that provides a form for searching Yyyyy records.
 * It is a flexible search screen that filters Yyyyy records and hands the results to the list view.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyySearch extends AppCompatActivity {

  private SimpleDateFormat formatter = null;
  private SimpleDateFormat dateTimeFormatter = null;
  private Boolean showButtons = new Boolean(true);
  private Resources res;
  private YyyyyDao yyyyyDao;
  private GpsTracker gpsTracker = null;
  private DataCheck dataCheck = new DataCheck();
___DAO_ONE2MANY_CHILD___
___DAO_DEF_TAG___
___SEARCH_BOOLEAN___

___VIEW_CLASS_ONE2MANY_CHILD___
___SEARCH_DATE___
___SEARCH_DATE_TIME___

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.mytheme);
    setContentView(R.layout.yfyfy_search);
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

    addListenerOnButton();

    yyyyyDao = new YyyyyDao(this);
___DAO_CREATE_ONE2MANY_CHILD___
___CREATE_CLASS_ONE2MANY_CHILD___
___DAO_NEW_TAG___

___SEARCH_SET_INTEGER___
___SEARCH_SET_STRING___
___SEARCH_SET_CLOB___
___SEARCH_SET_ENUM___
___SEARCH_SET_TAG___
___SEARCH_SET_DOUBLE___
___SEARCH_SET_MONEY___
___SEARCH_SET_LOC___
___SEARCH_SET_CURRENT_LOC___
___SEARCH_SET_BOOLEAN___

___SEARCH_SET_DATE___
___SEARCH_SET_DATE_TIME___
___SEARCH_SET_ONE2MANY_CHILD___
  }


  // Will be called via the onClick attribute
  public void onClick(View view) {

    int rId = view.getId();

    if (rId == R.id.back) {
      finish();

___LOAD_LOC_BUTTON___
___LOAD_CURRENT_LOC_BUTTON___

    } else if (rId == R.id.search) {
       Yyyyy yyyyy = new Yyyyy();
       Yyyyy yyyyy2 = new Yyyyy();
       boolean errors = false;

___SEARCH_LOAD_INTEGER___
___SEARCH_LOAD_STRING___
___SEARCH_LOAD_CLOB___
___SEARCH_LOAD_ENUM___
___SEARCH_LOAD_TAG___
___SEARCH_LOAD_DOUBLE___
___SEARCH_LOAD_MONEY___
___SEARCH_LOAD_BOOLEAN___

___SEARCH_LOAD_DATE___
___SEARCH_LOAD_DATE_TIME___
___SEARCH_LOAD_LOC___
___SEARCH_LOAD_CURRENT_LOC___
___EDIT_LOAD_ONE2MANY_CHILD___

      if (errors)
         return;

      ArrayList<Yyyyy> values = yyyyyDao.searchYyyyy(yyyyy, yyyyy2);
      if (values.size() == 0) {
         Toast.makeText(YyyyySearch.this, res.getString(R.string.noMatches), Toast.LENGTH_SHORT).show();
         return;
      }
      Intent intent = new Intent(YyyyySearch.this, YyyyyList.class);
      Bundle extra = new Bundle();
      extra.putParcelableArrayList("Yyyyys", values);
      intent.putExtra("YyyyysContent", extra);
___SET_PARENT_ONE2MANY_CHILD___
      startActivity(intent);
      return;

___SEARCH_CLICK_BOOLEAN___


___SEARCH_HELP_FOR_INTEGER___
___SEARCH_HELP_FOR_STRING___
___SEARCH_HELP_FOR_CLOB___
___SEARCH_HELP_FOR_ENUM___
___SEARCH_HELP_FOR_TAG___
___SEARCH_HELP_FOR_DOUBLE___
___SEARCH_HELP_FOR_MONEY___
___SEARCH_HELP_FOR_DATE___
___SEARCH_HELP_FOR_DATE_TIME___
___SEARCH_HELP_FOR_BOOLEAN___

___SEARCH_HELP_FOR_LOC___
___SEARCH_HELP_FOR_CURRENT_LOC___

    }

  }


  public void addListenerOnButton() {

___SEARCH_LISTENER_DATE___
___SEARCH_LISTENER_DATE_TIME___
 
  }


  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {

___SEARCH_DIALOG_DATE___
___SEARCH_DIALOG_DATE_TIME___

    }

    return null;
  }

___SEARCH_PICKER_DATE___
___SEARCH_PICKER_DATE_TIME___

  // Initiating Sub Menu XML file (search_menu.xml)
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.search_menu, menu);
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

    if (rId == R.id.menu_search) {
       Yyyyy yyyyy = new Yyyyy();
       Yyyyy yyyyy2 = new Yyyyy();
       boolean errors = false;

___SEARCH_LOAD_INTEGER___
___SEARCH_LOAD_STRING___
___SEARCH_LOAD_CLOB___
___SEARCH_LOAD_ENUM___
___SEARCH_LOAD_TAG___
___SEARCH_LOAD_DOUBLE___
___SEARCH_LOAD_MONEY___
___SEARCH_LOAD_BOOLEAN___

___SEARCH_LOAD_DATE___
___SEARCH_LOAD_DATE_TIME___
___SEARCH_LOAD_LOC___
___SEARCH_LOAD_CURRENT_LOC___
___EDIT_LOAD_ONE2MANY_CHILD___

        if (errors)
           return true;

        ArrayList<Yyyyy> values = yyyyyDao.searchYyyyy(yyyyy, yyyyy2);
        if (values.size() == 0) {
           Toast.makeText(YyyyySearch.this, res.getString(R.string.noMatches), Toast.LENGTH_SHORT).show();
           return true;
        }
        Intent intent = new Intent(YyyyySearch.this, YyyyyList.class);
        Bundle extra = new Bundle();
        extra.putParcelableArrayList("Yyyyys", values);
        intent.putExtra("YyyyysContent", extra);
        startActivity(intent);
        return true;
 
    } else if (rId == R.id.menu_cancel) {

        finish();
 
    }
    return super.onOptionsItemSelected(item);
  
  }  


  @Override
  protected void onResume() {
    super.onResume();

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
