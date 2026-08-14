package ppp.ppp.ppp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * Lllll
 *
 * This is an Android SettingsActivity (AppCompatActivity) that hosts a settings screen.
 * It is a thin host activity for a settings UI with toolbar + options menu support.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class SettingsActivity extends AppCompatActivity {

    private static String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.mytheme);
        setContentView(R.layout.settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        Log.d(TAG, "Menu inflated");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "Activity handling menu item: " + item.getItemId());
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.settings_container);
        if (fragment instanceof SettingsFragment) {
            if (((SettingsFragment) fragment).onOptionsItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SettingsActivity onDestroy");
    }
}