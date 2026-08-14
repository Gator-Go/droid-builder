package ppp.ppp.ppp;

import ppp.ppp.ppp.checks.DataCheck;
import ppp.ppp.ppp.sync.XxxxxSyncUtils;
___ACTIVITY_IMPORT_FUNCTION___

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Lllll
 *
 * This is the main Android XxxxxActivity (AppCompatActivity).
 * It is the app’s primary entry-point screen that provides navigation to data tables,
 * settings, and sync functionality.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class XxxxxActivity extends AppCompatActivity {

___ACTIVITY_DAO_FUNCTION___

  private DataCheck checker = new DataCheck();
  private ActivityResultLauncher<Intent> settingsLauncher;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.mytheme);
    setContentView(R.layout.activity);

    // Initialize Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    if (toolbar != null) {
        setSupportActionBar(toolbar);
    }

    // Initialize DAOs
___ACTIVITY_NEW_DAO_FUNCTION___


    // Initialize SharedPreferences
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    boolean showButtons = prefs.getBoolean("showButtons", true);
    if (!showButtons) {
        LinearLayout buttonGroup = findViewById(R.id.buttonGroup);
        if (buttonGroup != null) {
            buttonGroup.setVisibility(View.GONE);
        }
    }

    // Setup ListView
    ListView listView = findViewById(R.id.list); 
    String[] tables = {
            getString(R.string.selectOne)
___ACTIVITY_TABLE_FUNCTION___
    };
    ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            tables
    );
    listView.setAdapter(adapter);



      // Handle ListView item clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selection = parent.getItemAtPosition(position).toString();
            Intent intent = null;

___ACTIVITY_SELECTION_FUNCTION___

            if (intent != null) {
                startActivity(intent);
            }
        });

    // Setup sync account
    XxxxxSyncUtils.CreateSyncAccount(this);

    // Initialize ActivityResultLauncher for settings
    settingsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Handle settings result if needed
            }
    );
  }


  // Will be called via the onClick attribute
  public void onClick(View view) {
      int rId = view.getId();

      if (rId == R.id.help) {
          helpAlert(getString(R.string.activityHelp));
      } else if (rId == R.id.settings) {
          Intent intent = new Intent(this, SettingsActivity.class);
          settingsLauncher.launch(intent);
      } else if (rId == R.id.sync) {
          SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
          String deviceId = sharedPrefs.getString("prefDeviceId", "-1");
          if ("-1".equals(deviceId) || !checker.isNumeric(deviceId)) {
              helpAlert(getString(R.string.registryNonexist));
          } else {
              XxxxxSyncUtils.TriggerRefresh();
          }
      } else if (rId == R.id.exit) {
          finishAffinity(); 
      } else if (rId == R.id.xxxxxLink) {
          Uri uri = Uri.parse("http://www.homeberrycloud.com");
          Intent urlIntent = new Intent(Intent.ACTION_VIEW, uri);
          startActivity(urlIntent);
      }
  }


  // Initiating Sub Menu XML file (activity_menu.xml)
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.activity_menu, menu);
      return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {

    int rId = item.getItemId();        

    if (rId == R.id.menu_help) {
        helpAlert(getString(R.string.activityHelp));
        return true;

    } else if (rId == R.id.menu_settings) {
        Intent intent = new Intent(this, SettingsActivity.class);
        settingsLauncher.launch(intent);
        return true;

    } else if (rId == R.id.menu_sync) {
      SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
      String deviceId = sharedPrefs.getString("prefDeviceId", "-1");
      if (deviceId.equals("-1") || !checker.isNumeric(deviceId)) {
          helpAlert(getString(R.string.registryNonexist));
      } else {
          XxxxxSyncUtils.TriggerRefresh();
      }
      return true;
 
    } else if (rId == R.id.menu_exit) {
      finishAffinity();
      return true;
 
    } else {
        return super.onOptionsItemSelected(item);
    }
  }


    @Override
    protected void onStop() {
        super.onStop();
        // Close database connections
//        if (bookDao != null) bookDao.close();
//        if (chapterDao != null) chapterDao.close();
//        if (sentenceDao != null) sentenceDao.close();
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
