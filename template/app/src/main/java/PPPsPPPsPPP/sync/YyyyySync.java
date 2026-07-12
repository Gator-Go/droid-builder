
package ppp.ppp.ppp.sync;

import ppp.ppp.ppp.sync.YyyyyParser;
import ppp.ppp.ppp.provider.YyyyyContract;
import ppp.ppp.ppp.pojos.Yyyyy;
import ppp.ppp.ppp.daos.YyyyyDao;
import ppp.ppp.ppp.FileDeleteTask;
import ppp.ppp.ppp.ssl.SyncServerFile;
___VIEW_IMPORT_ONE2MANY_CHILD___
___VIEW_IMPORT_ONE2MANY_CHILD_ALERT___

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.lang.StringBuffer;

/**
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
class YyyyySync {
    public static final String TAG = "YyyyySync";
    public SyncServerFile syncServerFile = SyncServerFile.getInstance();
    public XxxxxSyncDownFile xxxxxSyncDownFile = new XxxxxSyncDownFile();
    private String AUTHORIZATION = "";
    private Context context;

    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private final String[] PROJECTION = new String[]
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

    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_DEVICE_ID = 1;
    public static final int COLUMN_YYYYY_ID = 2;
    public static final int COLUMN_LAST_UPDATE = 3;
    public static final int COLUMN_DELETE_FLAG = 4;

___SYNC_SET_COLUMN_INTEGER___
___SYNC_SET_COLUMN_STRING___
___SYNC_SET_COLUMN_CLOB___
___SYNC_SET_COLUMN_ENUM___
___SYNC_SET_COLUMN_TAG___
___SYNC_SET_COLUMN_DOUBLE___
___SYNC_SET_COLUMN_MONEY___
___SYNC_SET_COLUMN_BOOLEAN___
___SYNC_SET_COLUMN_LOC___
___SYNC_SET_COLUMN_CURRENT_LOC___
___SYNC_SET_COLUMN_DATE___
___SYNC_SET_COLUMN_DATE_TIME___
___SYNC_SET_COLUMN_CAMERA___
___SYNC_SET_COLUMN_VIDEO___
___SYNC_SET_COLUMN_THUMBNAIL___
___SYNC_SET_COLUMN_POST___
___SYNC_SET_COLUMN_ONE2MANY_CHILD___
___SYNC_SET_COLUMN_ONE2MANY_CHILD_ALERT___

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver contentResolver;
___DAO_ONE2MANY_CHILD___
___DAO_ONE2MANY_CHILD_ALERT___

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public YyyyySync(ContentResolver contentResolver, Context context) {
        this.contentResolver = contentResolver;
        this.context = context;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

___DAO_CREATE_CONTEXT_ONE2MANY_CHILD___
___DAO_CREATE_CONTEXT_ONE2MANY_CHILD_ALERT___
    }


    public void syncYyyyyDelete(final String deleteStr, final SyncResult syncResult, StringBuffer syncInfo)
            throws IOException, RemoteException,
            OperationApplicationException, ParseException {

        final YyyyyParser yyyyyParser = new YyyyyParser();

        Log.i(TAG, "Parsing yyyyy deleteStr");
        final List<String> deleteIds = yyyyyParser.parseDelete(deleteStr);
        Log.i(TAG, "Parsing complete. Found " + deleteIds.size() + " deleteIds");
        syncInfo.append(TAG + " Server sent " + deleteIds.size() + " deleteIds" + "<br>");

        ArrayList<ContentProviderOperation> batchOps = new ArrayList<ContentProviderOperation>();

        for (String id : deleteIds) {
            String[] parts = id.split("-");
            String queryString = "DEVICE_ID=? AND YYYYY_ID=?";
            List<String> argList = new ArrayList<String>();
            argList.add(parts[0]);
            argList.add(parts[1]);
            String[] args = argList.toArray(new String[argList.size()]);

            Uri yyyyyUri = YyyyyContract.Yyyyy.CONTENT_URI;
            Cursor cursor = contentResolver.query(yyyyyUri, PROJECTION, queryString, args, null);

            if (cursor.getCount() == 0)
              continue;
            cursor.moveToFirst();

            // load yyyyy for delete processing
            YyyyyDao yyyyyDao = new YyyyyDao(context);
            Yyyyy yyyyy = yyyyyDao.cursorToYyyyy(cursor);
            cursor.close();

___DELETE_CAMERA___
___DELETE_VIDEO___
___DELETE_THUMBNAIL___
___DELETE_POST___

            Uri existingUri = YyyyyContract.Yyyyy.CONTENT_URI.buildUpon()
                        .appendPath(Long.toString(yyyyy.getId())).build();

            batchOps.add(ContentProviderOperation.newDelete(existingUri).build());
            syncResult.stats.numDeletes++;

        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        contentResolver.applyBatch(YyyyyContract.CONTENT_AUTHORITY, batchOps);
        contentResolver.notifyChange(
                YyyyyContract.Yyyyy.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to cloud
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    /**
     * Read XML from an input stream, storing it into the content provider.
     *
     * <p>This is where incoming data is persisted, committing the results of a sync. In order to
     * minimize (expensive) disk operations, we compare incoming data with what's already in our
     * database, and compute a merge. Only changes (insert/update/delete) will result in a database
     * write.
     *
     * <p>As an additional optimization, we use a batch operation to perform all database writes at
     * once.
     *
     * <p>Merge strategy:
     * 1. Get cursor to all items in feed<br/>
     * 2. For each item, check if it's in the incoming data.<br/>
     *    a. YES: Remove from "incoming" list. Check if data has mutated, if so, perform
     *            database UPDATE.<br/>
     *    b. NO: Schedule DELETE from database.<br/>
     * (At this point, incoming database only contains missing items.)<br/>
     * 3. For any items remaining in incoming list, ADD to database.
     */
    public List<Yyyyy> syncYyyyyData(final String inputStr, final SyncResult syncResult, final String prefServerIP, final String authorSTR, StringBuffer syncInfo)
            throws IOException, RemoteException,
            OperationApplicationException, ParseException {

        String urlDown = "https://" + prefServerIP.trim() + "/xxxxxws/sync?";
        AUTHORIZATION = authorSTR;

        List<Yyyyy> newYyyyys = new ArrayList<Yyyyy>();
        final YyyyyParser yyyyyParser = new YyyyyParser();

        Log.i(TAG, "Parsing yyyyy inputStr");
        final List<Yyyyy> yyyyys = yyyyyParser.parse(inputStr);
        Log.i(TAG, "Parsing complete. Found " + yyyyys.size() + " yyyyys");
        syncInfo.append(TAG + " Server sent " + yyyyys.size() + " yyyyys" + "<br>");


        ArrayList<ContentProviderOperation> batchOps = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming yyyyys
        HashMap<String, Yyyyy> yyyyyMap = new HashMap<String, Yyyyy>();
        for (Yyyyy yyyyy : yyyyys) {
            String idStr = yyyyy.getCloudId();
            yyyyyMap.put(idStr, yyyyy);
            Log.i(TAG, "Map idStr loaded: " + idStr);
        }

        // Get list of all yyyyys  in the list. If they are not found, they are new.
        Log.i(TAG, "Fetching local yyyyys for merge");
        Uri uri = YyyyyContract.Yyyyy.CONTENT_URI; // Get all yyyyys in list

        String queryString = "";
        String[] args = new String[yyyyys.size() * 2];
        int argIndex = 0;
        for (Yyyyy yyyyy : yyyyys) {
            if (argIndex != 0)
              queryString = queryString + " OR ";
            queryString = queryString + "(DEVICE_ID = ? AND YYYYY_ID = ?)";
            args[argIndex] = yyyyy.getDeviceId().toString();
            args[argIndex + 1] = yyyyy.getYyyyyId().toString();
            argIndex += 2;
        }

        Cursor c = contentResolver.query(uri, PROJECTION, queryString, args, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local yyyyys. Computing merge solution...");
        syncInfo.append(TAG + " Local count " + c.getCount() + " yyyyys" + "<br>");

        // Find stale data
        Long id;
        Long deviceId;
        Long yyyyyId;
        Long lastUpdate;
        Integer deleteFlag;

___SYNC_DEF_INTEGER___
___SYNC_DEF_STRING___
___SYNC_DEF_CLOB___
___SYNC_DEF_ENUM___
___SYNC_DEF_TAG___
___SYNC_DEF_DOUBLE___
___SYNC_DEF_MONEY___
___SYNC_DEF_BOOLEAN___
___SYNC_DEF_CAMERA___
___SYNC_DEF_VIDEO___
___SYNC_DEF_THUMBNAIL___
___SYNC_DEF_POST___
___SYNC_DEF_LOC___
___SYNC_DEF_CURRENT_LOC___
___SYNC_DEF_DATE___
___SYNC_DEF_DATE_TIME___
___SYNC_DEF_ONE2MANY_CHILD___
___SYNC_DEF_ONE2MANY_CHILD_ALERT___


        while (c.moveToNext()) {

            syncResult.stats.numEntries++;

            // load fields from db
            id = c.getLong(COLUMN_ID);
            deviceId = c.getLong(COLUMN_DEVICE_ID);
            yyyyyId = c.getLong(COLUMN_YYYYY_ID);
            lastUpdate = c.getLong(COLUMN_LAST_UPDATE);
            deleteFlag = c.getInt(COLUMN_DELETE_FLAG);
            String cloudId = deviceId.toString() + "-" + yyyyyId.toString();

___SYNC_GET_INTEGER___
___SYNC_GET_STRING___
___SYNC_GET_CLOB___
___SYNC_GET_ENUM___
___SYNC_GET_TAG___
___SYNC_GET_DOUBLE___
___SYNC_GET_MONEY___
___SYNC_GET_BOOLEAN___
___SYNC_GET_CAMERA___
___SYNC_GET_VIDEO___
___SYNC_GET_THUMBNAIL___
___SYNC_GET_POST___
___SYNC_GET_LOC___
___SYNC_GET_CURRENT_LOC___
___SYNC_GET_DATE___
___SYNC_GET_DATE_TIME___
___SYNC_GET_ONE2MANY_CHILD___
___SYNC_GET_ONE2MANY_CHILD_ALERT___


            String idStr = deviceId.toString() + "-" + yyyyyId.toString();
            Log.i(TAG, "DB idStr to process: " + idStr);

            // check db idStr with syc idStr
            Yyyyy yyyyy = yyyyyMap.get(idStr);

            // if db idStr in map then check for updates
            if (yyyyy != null) {

                // Yyyyy exists. Remove from entry map to prevent insert later.
                yyyyyMap.remove(idStr);

                // Check to see if the entry needs to be updated
                Uri existingUri = YyyyyContract.Yyyyy.CONTENT_URI.buildUpon()
                        .appendPath(Long.toString(id)).build();

                if ( id != null

___SYNC_IF_CHANGED_INTEGER___
___SYNC_IF_CHANGED_STRING___
___SYNC_IF_CHANGED_CLOB___
___SYNC_IF_CHANGED_ENUM___
___SYNC_IF_CHANGED_TAG___
___SYNC_IF_CHANGED_DOUBLE___
___SYNC_IF_CHANGED_MONEY___
___SYNC_IF_CHANGED_BOOLEAN___
___SYNC_IF_CHANGED_CAMERA___
___SYNC_IF_CHANGED_VIDEO___
___SYNC_IF_CHANGED_THUMBNAIL___
___SYNC_IF_CHANGED_POST___
___SYNC_IF_CHANGED_LOC___
___SYNC_IF_CHANGED_CURRENT_LOC___
___SYNC_IF_CHANGED_DATE___
___SYNC_IF_CHANGED_DATE_TIME___
___SYNC_IF_CHANGED_ONE2MANY_CHILD___
___SYNC_IF_CHANGED_ONE2MANY_CHILD_ALERT___

                   ) {

                    Log.i(TAG, "No action: " + idStr);
                    syncInfo.append(TAG + " No action: " + idStr + "<br>");
                    
                } else {

                    // Update existing record
                    Log.i(TAG, "Update existing record: " + idStr);

___SYNC_GET_PARENT_ID_ONE2MANY_CHILD___
___SYNC_GET_PARENT_ID_ONE2MANY_CHILD_ALERT___

                    batchOps.add(ContentProviderOperation.newUpdate(existingUri)
                    .withValue("_ID", id)
                    .withValue("DEVICE_ID", yyyyy.getDeviceId())
                    .withValue("YYYYY_ID", yyyyy.getYyyyyId())
                    .withValue("LAST_UPDATE", yyyyy.getLastUpdate().getTime())
                    .withValue("DELETE_FLAG", yyyyy.getDeleteFlag())

___SYNC_WITH_VALUE_INTEGER___
___SYNC_WITH_VALUE_STRING___
___SYNC_WITH_VALUE_CLOB___
___SYNC_WITH_VALUE_ENUM___
___SYNC_WITH_VALUE_TAG___
___SYNC_WITH_VALUE_DOUBLE___
___SYNC_WITH_VALUE_MONEY___
___SYNC_WITH_VALUE_BOOLEAN___
___SYNC_WITH_VALUE_CAMERA___
___SYNC_WITH_VALUE_VIDEO___
___SYNC_WITH_VALUE_THUMBNAIL___
___SYNC_WITH_VALUE_POST___
___SYNC_WITH_VALUE_LOC___
___SYNC_WITH_VALUE_CURRENT_LOC___
___SYNC_WITH_VALUE_DATE___
___SYNC_WITH_VALUE_DATE_TIME___
___SYNC_WITH_VALUE_ONE2MANY_CHILD___
___SYNC_WITH_VALUE_ONE2MANY_CHILD_ALERT___

                            .build());
                    syncResult.stats.numUpdates++;

___SYNC_UPDATE_FILE_CAMERA___
___SYNC_UPDATE_FILE_VIDEO___
___SYNC_UPDATE_FILE_THUMBNAIL___
___SYNC_UPDATE_FILE_POST___

                    syncInfo.append(TAG + " Update existing record: " + idStr + "<br>");

                }
            } else {
                // Xxxxx doesn't exist on server. Add it.

___SYNC_GET_PARENT_ID_ONE2MANY_CHILD___
___SYNC_GET_PARENT_ID_ONE2MANY_CHILD_ALERT___

                Yyyyy newYyyyy = new Yyyyy();
                newYyyyy.setId(id);
                newYyyyy.setDeviceId(deviceId);
                newYyyyy.setYyyyyId(yyyyyId);
                newYyyyy.setLastUpdate(new Date(lastUpdate));
                newYyyyy.setDeleteFlag(deleteFlag);

___SYNC_WITH_NEW_VALUE_INTEGER___
___SYNC_WITH_NEW_VALUE_STRING___
___SYNC_WITH_NEW_VALUE_CLOB___
___SYNC_WITH_NEW_VALUE_ENUM___
___SYNC_WITH_NEW_VALUE_TAG___
___SYNC_WITH_NEW_VALUE_DOUBLE___
___SYNC_WITH_NEW_VALUE_MONEY___
___SYNC_WITH_NEW_VALUE_BOOLEAN___
___SYNC_WITH_NEW_VALUE_CAMERA___
___SYNC_WITH_NEW_VALUE_VIDEO___
___SYNC_WITH_NEW_VALUE_THUMBNAIL___
___SYNC_WITH_NEW_VALUE_POST___
___SYNC_WITH_NEW_VALUE_LOC___
___SYNC_WITH_NEW_VALUE_CURRENT_LOC___
___SYNC_WITH_NEW_VALUE_DATE___
___SYNC_WITH_NEW_VALUE_DATE_TIME___
___SYNC_WITH_NEW_VALUE_ONE2MANY_CHILD___
___SYNC_WITH_NEW_VALUE_ONE2MANY_CHILD_ALERT___

                newYyyyys.add(newYyyyy);
                Log.i(TAG, "Add to server action: " + idStr);
                syncInfo.append(TAG + " Add action:  " + idStr + "<br>");
            }
        }
        c.close();

        // Add new items
        for (Yyyyy yyyyy : yyyyyMap.values()) {

            String cloudId = yyyyy.getCloudId();

___SYNC_GET_PARENT_ID2_ONE2MANY_CHILD___
___SYNC_GET_PARENT_ID2_ONE2MANY_CHILD_ALERT___

            batchOps.add(ContentProviderOperation.newInsert(YyyyyContract.Yyyyy.CONTENT_URI)
                    .withValue("DEVICE_ID", yyyyy.getDeviceId())
                    .withValue("YYYYY_ID", yyyyy.getYyyyyId())
                    .withValue("LAST_UPDATE", yyyyy.getLastUpdate().getTime())
                    .withValue("DELETE_FLAG", yyyyy.getDeleteFlag())

___SYNC_WITH_VALUE_INTEGER___
___SYNC_WITH_VALUE_STRING___
___SYNC_WITH_VALUE_CLOB___
___SYNC_WITH_VALUE_ENUM___
___SYNC_WITH_VALUE_TAG___
___SYNC_WITH_VALUE_DOUBLE___
___SYNC_WITH_VALUE_MONEY___
___SYNC_WITH_VALUE_BOOLEAN___
___SYNC_WITH_VALUE_CAMERA___
___SYNC_WITH_VALUE_VIDEO___
___SYNC_WITH_VALUE_THUMBNAIL___
___SYNC_WITH_VALUE_POST___
___SYNC_WITH_VALUE_LOC___
___SYNC_WITH_VALUE_CURRENT_LOC___
___SYNC_WITH_VALUE_DATE___
___SYNC_WITH_VALUE_DATE_TIME___
___SYNC_WITH_VALUE_ONE2MANY_CHILD___
___SYNC_WITH_VALUE_ONE2MANY_CHILD_ALERT___
___SYNC_WITH_VALUE_LOC_SERVICE___
___SYNC_WITH_VALUE_SECURITY___

                    .build());

___SYNC_UP_FILE_CAMERA___
___SYNC_UP_FILE_VIDEO___
___SYNC_UP_FILE_THUMBNAIL___
___SYNC_UP_FILE_POST___

            syncResult.stats.numInserts++;
            Log.i(TAG, "Insert action: " + cloudId);
            syncInfo.append(TAG + " Insert action:  " + cloudId + "<br>");
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        contentResolver.applyBatch(YyyyyContract.CONTENT_AUTHORITY, batchOps);
        contentResolver.notifyChange(
                YyyyyContract.Yyyyy.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to cloud
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.

        return newYyyyys;
    }

 }
