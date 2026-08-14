
package ppp.ppp.ppp.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Lllll
 *
 * This is the YyyyyContract class that defines the content URI
 * and column constants for Yyyyy data.
 * It is the contract that standardizes how the rest of the app
 * accesses Yyyyy data through the ContentProvider.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyContract {
    private YyyyyContract() {
    }

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "ppp.ppp.ppp.provider.XxxxxProvider";

    /**
     * Base URI. (content://ppp.ppp.ppp.provider.XxxxxProvider)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "yyyyy"-type resources..
     */
    private static final String PATH_YYYYYS = "yyyyys";

    /**
     * Columns supported by "yyyyys" records.
     */
    public static class Yyyyy implements BaseColumns {
        /**
         * MIME type for lists of yyyyys.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/xxxxx.yyyyys";
        /**
         * MIME type for individual yyyyys.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/xxxxx.yyyyy";

        /**
         * Fully qualified URI for "yyyyy" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_YYYYYS).build();


        public static final String TABLE_YYYYY = "YYYYY";

___SYNC_CONTRACT_INTEGER___
___SYNC_CONTRACT_STRING___
___SYNC_CONTRACT_CLOB___
___SYNC_CONTRACT_ENUM___
___SYNC_CONTRACT_TAG___
___SYNC_CONTRACT_DOUBLE___
___SYNC_CONTRACT_MONEY___
___SYNC_CONTRACT_BOOLEAN___
___SYNC_CONTRACT_CAMERA___
___SYNC_CONTRACT_VIDEO___
___SYNC_CONTRACT_THUMBNAIL___
___SYNC_CONTRACT_LOC___
___SYNC_CONTRACT_CURRENT_LOC___
___SYNC_CONTRACT_DATE___
___SYNC_CONTRACT_DATE_TIME___

    }
}