package ppp.ppp.ppp.daos;

import ppp.ppp.ppp.pojos.Yyyyy;
import ppp.ppp.ppp.sync.SyncYyyyyServerHelper;
import ppp.ppp.ppp.provider.YyyyyContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

/**
 * Lllll
 *  
 */

/**
 * This entity bean is used to manage the YYYYY database table.<br>
 * This class follows the POJO model so there is a standard get<br>
 * and set method on each data item.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */


public class YyyyyDao {

  private static String TAG = "YyyyyDao";

  private Context context;
  private ContentResolver contentResolver;
  private Long prefDeviceId;

  // Database fields
  private String[] allColumns =
      {"_ID" 
      + ",DEVICE_ID"
      + ",YYYYY_ID"
      + ",LAST_UPDATE"
      + ",DELETE_FLAG"
___DAO_COLUMN_INTEGER___
___DAO_COLUMN_STRING___
___DAO_COLUMN_CLOB___
___DAO_COLUMN_ENUM___
___DAO_COLUMN_TAG___
___DAO_COLUMN_DOUBLE___
___DAO_COLUMN_MONEY___
___DAO_COLUMN_BOOLEAN___
___DAO_COLUMN_LOC___
___DAO_COLUMN_CURRENT_LOC___
___DAO_COLUMN_DATE___
___DAO_COLUMN_DATE_TIME___
___DAO_COLUMN_CAMERA___
___DAO_COLUMN_VIDEO___
___DAO_COLUMN_THUMBNAIL___
___DAO_COLUMN_POST___
___DAO_COLUMN_ONE2MANY_CHILD___
___DAO_COLUMN_ONE2MANY_CHILD_ALERT___
___DAO_COLUMN_LOC_SERVICE___
___DAO_COLUMN_SECURITY___
      };
  private String orderBy = "LAST_UPDATE DESC";

  public YyyyyDao(Context context) {
    this.context = context;
    contentResolver = context.getContentResolver();

    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    String deviceIdStr = sharedPrefs.getString("prefDeviceId", "-1");
    if (deviceIdStr.equals("-1"))
      prefDeviceId = new Long(2);
    else
      prefDeviceId = Long.parseLong(deviceIdStr);
  }

  public void close() {

  }



  public Yyyyy getYyyyy(Yyyyy yyyyy) {

    Uri yyyyyUri = Uri.parse(YyyyyContract.Yyyyy.CONTENT_URI + "/" + yyyyy.getId().toString());
    Cursor cursor = contentResolver.query(yyyyyUri, allColumns, null, null, orderBy);

    if (cursor.getCount() == 0) {
      Log.d(TAG, "Not Found " + yyyyy.getId().toString());
      return null;
    }
    cursor.moveToFirst();
    Yyyyy newYyyyy = cursorToYyyyy(cursor);
    cursor.close();
    return newYyyyy;
  }


  public Yyyyy getYyyyyById(Long id) {

    Uri yyyyyUri = Uri.parse(YyyyyContract.Yyyyy.CONTENT_URI + "/" + id.toString());
    Cursor cursor = contentResolver.query(yyyyyUri, allColumns, null, null, orderBy);

    if (cursor.getCount() == 0)
      return null;
    cursor.moveToFirst();
    Yyyyy newYyyyy = cursorToYyyyy(cursor);
    cursor.close();
    return newYyyyy;
  }


  public Yyyyy getYyyyyByCloudId(String cloudId) {

    String[] parts = cloudId.split("-");
    String queryString = "DEVICE_ID=? AND YYYYY_ID=?";
    List<String> argList = new ArrayList<String>();
    argList.add(parts[0]);
    argList.add(parts[1]);
    String[] args = argList.toArray(new String[argList.size()]);

    Uri yyyyyUri = YyyyyContract.Yyyyy.CONTENT_URI;
    Cursor cursor = contentResolver.query(yyyyyUri, allColumns, queryString, args, orderBy);

    if (cursor.getCount() == 0)
      return null;
    cursor.moveToFirst();
    Yyyyy newYyyyy = cursorToYyyyy(cursor);
    cursor.close();
    return newYyyyy;
  }


  public ArrayList<Yyyyy> searchYyyyy(Yyyyy yyyyy, Yyyyy yyyyy2) {

    ArrayList<Yyyyy> yyyyys = new ArrayList<Yyyyy>();
    String queryString = "";
    List<String> argList = new ArrayList<String>();

___DAO_SEARCH_INTEGER___
___DAO_SEARCH_STRING___
___DAO_SEARCH_CLOB___
___DAO_SEARCH_ENUM___
___DAO_SEARCH_TAG___
___DAO_SEARCH_DOUBLE___
___DAO_SEARCH_MONEY___
___DAO_SEARCH_DATE___
___DAO_SEARCH_DATE_TIME___
___DAO_SEARCH_BOOLEAN___
___DAO_SEARCH_CAMERA___
___DAO_SEARCH_VIDEO___
___DAO_SEARCH_THUMBNAIL___
___DAO_SEARCH_POST___
___DAO_SEARCH_LOC___
___DAO_SEARCH_CURRENT_LOC___
___DAO_SEARCH_ONE2MANY_CHILD___
___DAO_SEARCH_ONE2MANY_CHILD_ALERT___


    if (argList.size() < 1)
       return yyyyys;

    String[] args = argList.toArray(new String[argList.size()]);

    Uri yyyyyUri = YyyyyContract.Yyyyy.CONTENT_URI;
    Cursor cursor = contentResolver.query(yyyyyUri, allColumns, queryString, args, orderBy);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Yyyyy yyyyyRtn = cursorToYyyyy(cursor);
      yyyyys.add(yyyyyRtn);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return yyyyys;

  }



  public Yyyyy createYyyyy(Yyyyy yyyyy) {

    ContentValues values = new ContentValues();

    if (yyyyy.getDeviceId() != null)
       values.put("DEVICE_ID", yyyyy.getDeviceId());
    else
       values.put("DEVICE_ID", prefDeviceId);

    if (yyyyy.getYyyyyId() != null)
       values.put("YYYYY_ID", yyyyy.getYyyyyId());
    else
       values.put("YYYYY_ID", new Long(0));

    values.put("LAST_UPDATE", new Date().getTime());
    values.put("DELETE_FLAG", new Integer(0));

___DAO_PUT_INTEGER___
___DAO_PUT_STRING___
___DAO_PUT_CLOB___
___DAO_PUT_ENUM___
___DAO_PUT_TAG___
___DAO_PUT_DOUBLE___
___DAO_PUT_MONEY___
___DAO_PUT_DATE___
___DAO_PUT_DATE_TIME___
___DAO_PUT_BOOLEAN___
___DAO_PUT_CAMERA___
___DAO_PUT_VIDEO___
___DAO_PUT_THUMBNAIL___
___DAO_PUT_POST___
___DAO_PUT_LOC___
___DAO_PUT_CURRENT_LOC___
___DAO_PUT_ONE2MANY_CHILD___
___DAO_PUT_ONE2MANY_CHILD_ALERT___
___DAO_PUT_LOC_SERVICE___
___DAO_PUT_SECURITY___

    Uri yyyyyUri = contentResolver.insert(YyyyyContract.Yyyyy.CONTENT_URI, values);

    Cursor cursor = contentResolver.query(yyyyyUri, allColumns, null, null, orderBy);

    cursor.moveToFirst();
    Yyyyy newYyyyy = cursorToYyyyy(cursor);
    cursor.close();


    if (newYyyyy.getDeviceId().longValue() == prefDeviceId.longValue()) {
        newYyyyy.setYyyyyId(newYyyyy.getId());
        editYyyyy(newYyyyy);
    }

    return newYyyyy;
  }



  public void editYyyyy(Yyyyy yyyyy) {

    ContentValues values = new ContentValues();

    values.put("DEVICE_ID", yyyyy.getDeviceId());
    values.put("YYYYY_ID", yyyyy.getYyyyyId());
    values.put("LAST_UPDATE", new Date().getTime());
    values.put("DELETE_FLAG", yyyyy.getDeleteFlag());

___DAO_PUT_INTEGER___
___DAO_PUT_STRING___
___DAO_PUT_CLOB___
___DAO_PUT_ENUM___
___DAO_PUT_TAG___
___DAO_PUT_DOUBLE___
___DAO_PUT_MONEY___
___DAO_PUT_DATE___
___DAO_PUT_DATE_TIME___
___DAO_PUT_BOOLEAN___
___DAO_PUT_CAMERA___
___DAO_PUT_VIDEO___
___DAO_PUT_THUMBNAIL___
___DAO_PUT_POST___
___DAO_PUT_LOC___
___DAO_PUT_CURRENT_LOC___
___DAO_PUT_ONE2MANY_CHILD___
___DAO_PUT_ONE2MANY_CHILD_ALERT___
___DAO_PUT_LOC_SERVICE___
___DAO_PUT_SECURITY___

    Uri yyyyyUri = Uri.parse(YyyyyContract.Yyyyy.CONTENT_URI + "/" + yyyyy.getId().toString());
    int count = contentResolver.update(yyyyyUri, values, null, null);
    System.out.println("Yyyyy updated with id: " + yyyyy.getId().toString() + " Count = " + count);

    SyncYyyyyServerHelper syncYyyyyServerHelper = new SyncYyyyyServerHelper();
    syncYyyyyServerHelper.syncYyyyy(context,yyyyy);

    return;
  }

  public void deleteYyyyy(Yyyyy yyyyy) {

    long id = yyyyy.getId();
    Uri yyyyyUri = Uri.parse(YyyyyContract.Yyyyy.CONTENT_URI + "/" + yyyyy.getId().toString());
    int count = contentResolver.delete(yyyyyUri, null, null);
    System.out.println("Yyyyy deleted with id: " + id + " Count = " + count);
  }



  public void deleteYyyyyChildren(String parent, Long longId) {

    String id = longId.toString();
    String queryString = parent + "_ID=?";
    String[] args = new String[1];
    args[0] = id;

    Uri yyyyyUri = YyyyyContract.Yyyyy.CONTENT_URI;
    int count = contentResolver.delete(yyyyyUri, queryString, args);
    System.out.println("Yyyyy delete parent " + parent + " rows with id: " + id + " Count = " + count);
  }



  public ArrayList<Yyyyy> getAllYyyyys() {

    ArrayList<Yyyyy> yyyyys = new ArrayList<Yyyyy>();

    Uri yyyyyUri = YyyyyContract.Yyyyy.CONTENT_URI;
    Cursor cursor = contentResolver.query(yyyyyUri, allColumns, null, null, orderBy);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Yyyyy yyyyy = cursorToYyyyy(cursor);
      yyyyys.add(yyyyy);
      cursor.moveToNext();
    }

    cursor.close();
    return yyyyys;
  }



  public Yyyyy cursorToYyyyy(Cursor cursor) {
    Yyyyy yyyyy = new Yyyyy();
    yyyyy.setId(cursor.getLong(0));
    yyyyy.setDeviceId(cursor.getLong(1));
    yyyyy.setYyyyyId(cursor.getLong(2));
    yyyyy.setLastUpdate(new Date(cursor.getLong(3)));
    yyyyy.setDeleteFlag(cursor.getInt(4));
___DAO_SET_INTEGER___
___DAO_SET_STRING___
___DAO_SET_CLOB___
___DAO_SET_ENUM___
___DAO_SET_TAG___
___DAO_SET_DOUBLE___
___DAO_SET_MONEY___
___DAO_SET_BOOLEAN___
___DAO_SET_LOC___
___DAO_SET_CURRENT_LOC___
___DAO_SET_DATE___
___DAO_SET_DATE_TIME___
___DAO_SET_CAMERA___
___DAO_SET_VIDEO___
___DAO_SET_THUMBNAIL___
___DAO_SET_POST___
___DAO_SET_ONE2MANY_CHILD___
___DAO_SET_ONE2MANY_CHILD_ALERT___
___DAO_SET_LOC_SERVICE___
___DAO_SET_SECURITY___
    return yyyyy;
  }
}
