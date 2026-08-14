package ppp.ppp.ppp;

import ppp.ppp.ppp.alert.AlertServer;
import ppp.ppp.ppp.alert.XxxxxCryptoUtils;
import ppp.ppp.ppp.alert.RegisterWithServer;
import ppp.ppp.ppp.checks.DataCheck;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import android.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.util.Log;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * Lllll
 *
 * This is an Android SettingsFragment (PreferenceFragmentCompat) that manages app
 * configuration preferences.
 * It is a preference screen for server connection details, device registration,
 * and related settings, with encryption, validation, and server notification support.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static String TAG = "SettingsFragment";
    private Resources res;
    private String deviceId;
    private String serverIP;
    private String serverUser;
    private String serverPassword;
    private SharedPreferences prefs;
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private boolean connectionChanged = false;
    private DataCheck checker = new DataCheck();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Let PreferenceFragmentCompat create and attach the preference view
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            // Force black background
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black));
            Log.d(TAG, "Preference view background set to black");
        } else {
            Log.e(TAG, "Preference view is null");
        }
        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Wrap the context with PreferenceThemeOverlay
        Context themedContext = new ContextThemeWrapper(requireContext(), R.style.PreferenceThemeOverlay);
        setPreferencesFromResource(R.xml.settings, rootKey);
        res = getResources();
        prefs = PreferenceManager.getDefaultSharedPreferences(themedContext);
        setupPreferences();
        customizePreferenceDialogs();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up button click listeners
        setupButtons();
    }

    private void setupButtons() {
        // Access buttons from the activity's view hierarchy
        View helpButton = requireActivity().findViewById(R.id.help);
        View registerButton = requireActivity().findViewById(R.id.register);
        View backButton = requireActivity().findViewById(R.id.back);

        if (helpButton == null) {
            Log.e(TAG, "Help button not found in activity");
        } else {
            helpButton.setOnClickListener(v -> {
                Log.d(TAG, "Help button clicked");
                helpAlert(res.getString(R.string.settingsHelp));
            });
        }

        if (registerButton == null) {
            Log.e(TAG, "Register button not found in activity");
        } else {
            registerButton.setOnClickListener(v -> {
                Log.d(TAG, "Register button clicked");
                handleRegister();
            });
        }

        if (backButton == null) {
            Log.e(TAG, "Back button not found in activity");
        } else {
            backButton.setOnClickListener(v -> {
                Log.d(TAG, "Back button clicked");
                if (connectionChanged) {
                    AlertServer alertServer = new AlertServer(requireContext());
                    alertServer.alertToServer("config", "SettingsFragment", "New Config Settings");
                }
                requireActivity().finish();
            });
        }
    }

    private void customizePreferenceDialogs() {
    // Apply dark theme to EditTextPreference dialogs
        EditTextPreference[] editPrefs = {
            findPreference("prefDeviceId"),
            findPreference("prefServerIP"),
            findPreference("prefServerUser"),
            findPreference("prefServerPass"),
            findPreference("lastSyncDate")
        };
        for (EditTextPreference pref : editPrefs) {
            if (pref != null) {
                Log.d(TAG, "Configured EditTextPreference: " + pref.getKey());
            }
        }
        // Apply dark theme to ListPreference dialog
        ListPreference listPref = findPreference("prefDateFormat");
        if (listPref != null) {
            Log.d(TAG, "Configured ListPreference: prefDateFormat");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prefs.registerOnSharedPreferenceChangeListener(this);
        updatePreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupPreferences() {
        EditTextPreference prefDeviceId = findPreference("prefDeviceId");
        PreferenceCategory serverConn = findPreference("serverConn");
        if (serverConn != null && prefDeviceId != null) {
            serverConn.removePreference(prefDeviceId);
        }
    }

    private void updatePreferences() {
        deviceId = prefs.getString("prefDeviceId", "-1");
        serverIP = prefs.getString("prefServerIP", "sw-builder.com");

        Preference connStatus = findPreference("connStatus");
        if (connStatus != null) {
            if (deviceId.equals("-1")) {
                connStatus.setTitle("Not Connected");
            } else {
                connStatus.setTitle("Connected to server: " + serverIP + " and assigned device id: " + deviceId);
            }
        }

        boolean showButtons = prefs.getBoolean("showButtons", true);
        LinearLayout buttonGroup = requireActivity().findViewById(R.id.buttonGroup);
        if (buttonGroup != null) {
            if (!showButtons) {
                buttonGroup.removeAllViews();
            } else {
                buttonGroup.setVisibility(View.VISIBLE);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int rId = item.getItemId();
        if (rId == R.id.menu_help) {
            helpAlert(res.getString(R.string.settingsHelp));
            return true;
        } else if (rId == R.id.menu_register) {
            handleRegister();
            return true;
        } else if (rId == R.id.menu_return) {
            if (connectionChanged) {
                AlertServer alertServer = new AlertServer(requireContext());
                alertServer.alertToServer("config", "SettingsFragment", "New Config Settings");
            }
            requireActivity().finish();
            return true;
        }
        return false;
    }

    private void handleRegister() {
        String result = "";
        try {
            serverIP = prefs.getString("prefServerIP", "").trim();
            serverUser = prefs.getString("prefServerUser", "").trim();
            serverPassword = prefs.getString("prefServerPass", "").trim();
            String plainPassword = "";
            if (!serverUser.isEmpty() && !serverPassword.isEmpty()) {
                plainPassword = XxxxxCryptoUtils.decrypt(serverPassword);
            } else {
                return;
            }

            result = new RegisterWithServer(requireContext(), serverIP, serverUser, plainPassword)
                        .execute()
                        .get();

	    if (result.equals("-1")) {
                helpAlert("Connection problem");
            } else {
                helpAlert("New device id is " + result);
            }
        } catch (CancellationException e) {
            helpAlert("Cancelled");
            result = "-1";
        } catch (ExecutionException e) {
            helpAlert("Execution Exception");
            result = "-1";
        } catch (InterruptedException e) {
            helpAlert("Interrupted");
            result = "-1";
        }

        if (checker.isNumeric(result)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("prefDeviceId", result);
            editor.apply();
            deviceId = result;
        } else {
            deviceId = "-1";
        }

        Preference connStatus = findPreference("connStatus");
        if (connStatus != null) {
            if (deviceId.equals("-1")) {
                connStatus.setTitle("Not Connected");
            } else {
                connStatus.setTitle("Connected to server: " + serverIP + " and assigned device id: " + deviceId);
            }
        }
        connectionChanged = true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!(key.equals("showButtons") || key.equals("getServerData"))) {
            String pref = prefs.getString(key, "");

            if (key.equals("lastSyncDate")) {
                try {
                    Date lastSync = dateFormat.parse(pref);
                } catch (ParseException e) {
                    Toast.makeText(requireContext(), "ERROR - Bad Last Sync Date: " + pref, Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("lastSyncDate", "11-11-2011");
                    editor.apply();
                    updatePreferences();
                }
            }

            if (key.equals("prefServerPass")) {
                Log.i(TAG, "prefServerPass value: " + pref);
                if (!XxxxxCryptoUtils.isEncrypted(pref)) {
                    String encryptedPassword = XxxxxCryptoUtils.encrypt(pref);
                    Log.i(TAG, "Encrypted password: " + encryptedPassword);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("prefServerPass", encryptedPassword);
                    editor.apply();
                    updatePreferences();
                }
            }
        }
    }

    private void helpAlert(String helpText) {
        AlertDialog.Builder helpAlert = new AlertDialog.Builder(requireContext());
        helpAlert.setTitle(res.getString(R.string.helpText));
        helpAlert.setMessage(helpText);
        helpAlert.setCancelable(false);

        helpAlert.setPositiveButton(res.getString(R.string.ok), (dialog, whichButton) -> dialog.dismiss());

        final AlertDialog helpAlertDialog = helpAlert.create();
        helpAlertDialog.show();
    }
}
