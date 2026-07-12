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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class YyyyyList extends AppCompatActivity {
  private YyyyyDao yyyyyDao;
  private ArrayList<Yyyyy> values = null;
  private boolean reload = false;

  private ListView listView;
___VIEW_CLASS_ONE2MANY_CHILD___

  private ActivityResultLauncher<Intent> settingsLauncher;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.mytheme);
    setContentView(R.layout.yfyfy_list);

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

    yyyyyDao = new YyyyyDao(this);
___CREATE_CLASS_ONE2MANY_CHILD___

___LIST_SET_ONE2MANY_CHILD___

    Bundle extra = this.getIntent().getBundleExtra("YyyyysContent");
    if (extra != null ) {
       values = extra.getParcelableArrayList("Yyyyys");
    } else {
       values = yyyyyDao.getAllYyyyys();
    }

    TextView instruct = (TextView)findViewById(R.id.instruct); 
    if (values.size() > 0)
       instruct.setText(getResources().getString(R.string.selectOne));
    else
       instruct.setText(getResources().getString(R.string.empty));

    listView = (ListView) findViewById(R.id.list);
    YyyyyAdapter adapter = new YyyyyAdapter(this, values);
    listView.setAdapter(adapter);

    // Handle ListView item clicks
    listView.setOnItemClickListener((parent, view, position, id) -> 
	clickItem(parent, view, position, id));

    listView.setOnItemLongClickListener((parent, view, position, id) -> {
            longClickItem(parent, view, position, id);
            return true;
        });
  }

  public void clickItem(AdapterView<?> parent, View view, int position, long id) {
    boolean doClickItem = true;
   reload = true;
   Yyyyy yyyyy = values.get(position);
   Intent intent = new Intent(YyyyyList.this, YyyyyView.class);
   intent.putExtra("Yyyyy", yyyyy);
___LIST_SET_PARENT_ONE2MANY_CHILD___
   startActivity(intent);
  }

  public void longClickItem(AdapterView<?> parent, View view, int position, long id) {
   reload = true;
   Yyyyy yyyyy = values.get(position);
   Intent intent = new Intent(YyyyyList.this, YyyyyLongView.class);
   intent.putExtra("Yyyyy", yyyyy);
___LIST_SET_PARENT_ONE2MANY_CHILD___
   startActivity(intent);
  }

  // Will be called via the onClick attribute
  public void onClick(View view) {

    int rId = view.getId();

    if (rId == R.id.add) {
      reload = true;
      Intent addIntent = new Intent(YyyyyList.this, YyyyyAdd.class);
___ADD_SET_PARENT_ONE2MANY_CHILD___
      startActivity(addIntent);

    } else if (rId == R.id.search) {
      reload = true;
      Intent searchIntent = new Intent(YyyyyList.this, YyyyySearch.class);
___SEARCH_SET_PARENT_ONE2MANY_CHILD___
      startActivity(searchIntent);

___BUTTON_OPTION_LOC___
___BUTTON_OPTION_CURRENT_LOC___

    } else if (rId == R.id.back) {
      finish();

    }

  }

  // Initiating Menu XML file (yfyfy_menu.xml)
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.yfyfy_menu, menu);
      return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
        
    int rId = item.getItemId();

    if (rId == R.id.menu_add) {
        reload = true;
        Intent addIntent = new Intent(YyyyyList.this, YyyyyAdd.class);
___ADD_SET_PARENT_ONE2MANY_CHILD___
        startActivity(addIntent);
        return true;

    } else if (rId == R.id.menu_search) {
        reload = true;
        Intent searchIntent = new Intent(YyyyyList.this, YyyyySearch.class);
___SEARCH_SET_PARENT_ONE2MANY_CHILD___
        startActivity(searchIntent);
        return true;


___MENU_OPTION_LOC___
___MENU_OPTION_CURRENT_LOC___

    } else if (rId == R.id.menu_return) {

        finish();
 
    }
    return super.onOptionsItemSelected(item);
  }  

  @Override
  protected void onResume() {

    super.onResume();

    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    Boolean showButtons = sharedPrefs.getBoolean("showButtons", true);
    if (showButtons.booleanValue() == false) {
      LinearLayout buttonGroup = (LinearLayout)findViewById(R.id.buttonGroup);
      buttonGroup.removeAllViews();
    }

    if (reload) {
      boolean searchFlag = false;
      Yyyyy yyyyy = new Yyyyy();
      Yyyyy yyyyy2 = new Yyyyy();

___RELOAD_SET_PARENT_ONE2MANY_CHILD___

      if (searchFlag)
        values = yyyyyDao.searchYyyyy(yyyyy, yyyyy2);
      else
        values = yyyyyDao.getAllYyyyys();
      reload = false;
    }

    TextView instruct = (TextView)findViewById(R.id.instruct); 
    if (values.size() > 0)
       instruct.setText(getResources().getString(R.string.selectOne));
    else
       instruct.setText(getResources().getString(R.string.empty));

___LIST_SHOW_ONE2MANY_CHILD___

    listView = (ListView) findViewById(R.id.list);
    YyyyyAdapter adapter = new YyyyyAdapter(this, values);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener((parent, view, position, id) -> 
	clickItem(parent, view, position, id));

    listView.setOnItemLongClickListener((parent, view, position, id) -> {
            longClickItem(parent, view, position, id);
            return true;
        });
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

  public static boolean isNumeric(String str)
  {
    for (char c : str.toCharArray())
    {
        if (!Character.isDigit(c)) return false;
    }
    return true;
  }

} 
