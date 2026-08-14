package ppp.ppp.ppp;

import ppp.ppp.ppp.pojos.Yyyyy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager; 

import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Lllll
 *
 * This is a utility class YyyyyString that converts Yyyyy objects
 * into formatted display strings.
 * It is a helper that produces consistent text representations of
 * Yyyyy records for lists and other UI displays.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyString {

    private Context context;
    private SharedPreferences sharedPrefs;
    private String dateFormat;
    private SimpleDateFormat formatDate;
    private SimpleDateFormat dateTimeFormatter;

    public YyyyyString(Context context) {
        this.context = context;
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.dateFormat = sharedPrefs.getString("prefDateFormat", "yyyy/MM/dd");
        this.formatDate = new SimpleDateFormat (dateFormat);
        this.dateTimeFormatter = new SimpleDateFormat (dateFormat + " hh:mm");
    }

    public String getString(Yyyyy obj) {

       String result = String.format(
       ___LOAD_LIST_FIELDS___
        );
       return result;
    }

    public String getDisplayString(Yyyyy obj) {

       String result = String.format(
       ___LOAD_DISPLAY_FIELDS___
        );
       return result;
    }

    public ArrayList<String> getStringArray(ArrayList<Yyyyy> yyyyyValues) {

      ArrayList<String> yyyyysArray = new ArrayList<String>();

      for (Yyyyy yyyyy : yyyyyValues) {
        yyyyysArray.add(yyyyy.getCloudId() + "|" + this.getString(yyyyy));
      }
      return yyyyysArray;
    }

}
