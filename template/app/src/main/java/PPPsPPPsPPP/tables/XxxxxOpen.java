package ppp.ppp.ppp.tables;

___DB_OPEN_IMPORT_FUNCTION___

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

public class XxxxxOpen extends SQLiteOpenHelper
{
  private static final String DATABASE_NAME = "XXXXX.db";
  private static final int DATABASE_VERSION = 1;
  private final Context myContext;
  private static XxxxxOpen mInstance;
  private static SQLiteDatabase myWritableDb;

  private XxxxxOpen(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.myContext = context;
  }

  /**
   * Get default instance of the class to keep it a singleton
   *
   * @param context
   *            the application context
   */
  public static XxxxxOpen getInstance(Context context) {
      if (mInstance == null) {
          mInstance = new XxxxxOpen(context);
      }
      return mInstance;
  }

  /**
   * Returns a writable database instance in order not to open and close many
   * SQLiteDatabase objects simultaneously
   *
   * @return a writable instance to SQLiteDatabase
   */
  public SQLiteDatabase getMyWritableDatabase() {
      if ((myWritableDb == null) || (!myWritableDb.isOpen())) {
          myWritableDb = this.getWritableDatabase();
      }
 
      return myWritableDb;
  }



  @Override
  public void onCreate(SQLiteDatabase database) {

___DB_OPEN_CREATE_FUNCTION___

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(XxxxxOpen.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");

___DB_OPEN_DROP_FUNCTION___

    onCreate(db);
  }

  @Override
  public void close() {
      super.close();
      if (myWritableDb != null) {
          myWritableDb.close();
          myWritableDb = null;
      }
  }
}
