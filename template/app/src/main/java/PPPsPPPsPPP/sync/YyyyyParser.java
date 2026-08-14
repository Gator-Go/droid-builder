
package ppp.ppp.ppp.sync;

import ppp.ppp.ppp.pojos.Yyyyy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Lllll
 *
 * This is a YyyyyParser that converts between Yyyyy objects and JSON.
 * It is the JSON converter used during sync to send and receive Yyyyy data.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class YyyyyParser {

    private JSONObject jsonObject = new JSONObject();
    private JSONArray jsonArray = new JSONArray();

    public YyyyyParser() {
    }

    public List<Yyyyy> parse(String inputStr) {

        List<Yyyyy> yyyyys = new ArrayList<Yyyyy>();

        try {
          JSONArray jsonArray = new JSONArray(inputStr);
          for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);

            String idStr = jsonObject.getString("id");
            Long id = Long.parseLong(idStr);
            String deviceIdStr = jsonObject.getString("deviceId");
            Long deviceId = Long.parseLong(deviceIdStr);
            String yyyyyIdStr = jsonObject.getString("yyyyyId");
            Long yyyyyId = Long.parseLong(yyyyyIdStr);
            String lastUpdateStr = jsonObject.getString("lastUpdate");
            Long lastUpdate = Long.parseLong(lastUpdateStr);
            String deleteFlagStr = jsonObject.getString("deleteFlag");
            Integer deleteFlag = Integer.parseInt(deleteFlagStr);

___SYNC_PARSER_GET_INTEGER___
___SYNC_PARSER_GET_STRING___
___SYNC_PARSER_GET_CLOB___
___SYNC_PARSER_GET_ENUM___
___SYNC_PARSER_GET_TAG___
___SYNC_PARSER_GET_DOUBLE___
___SYNC_PARSER_GET_MONEY___
___SYNC_PARSER_GET_BOOLEAN___
___SYNC_PARSER_GET_CAMERA___
___SYNC_PARSER_GET_VIDEO___
___SYNC_PARSER_GET_THUMBNAIL___
___SYNC_PARSER_GET_POST___
___SYNC_PARSER_GET_LOC___
___SYNC_PARSER_GET_CURRENT_LOC___
___SYNC_PARSER_GET_DATE___
___SYNC_PARSER_GET_DATE_TIME___
___SYNC_PARSER_GET_ONE2MANY_CHILD___
___SYNC_PARSER_GET_ONE2MANY_CHILD_ALERT___


            Yyyyy yyyyy = new Yyyyy();
            yyyyy.setId(id);
            yyyyy.setDeviceId(deviceId);
            yyyyy.setYyyyyId(yyyyyId);
            yyyyy.setLastUpdate(new Date(lastUpdate));
            yyyyy.setDeleteFlag(deleteFlag);

___SYNC_PARSER_SET_INTEGER___
___SYNC_PARSER_SET_STRING___
___SYNC_PARSER_SET_CLOB___
___SYNC_PARSER_SET_ENUM___
___SYNC_PARSER_SET_TAG___
___SYNC_PARSER_SET_DOUBLE___
___SYNC_PARSER_SET_MONEY___
___SYNC_PARSER_SET_BOOLEAN___
___SYNC_PARSER_SET_CAMERA___
___SYNC_PARSER_SET_VIDEO___
___SYNC_PARSER_SET_THUMBNAIL___
___SYNC_PARSER_SET_POST___
___SYNC_PARSER_SET_LOC___
___SYNC_PARSER_SET_CURRENT_LOC___
___SYNC_PARSER_SET_DATE___
___SYNC_PARSER_SET_DATE_TIME___
___SYNC_PARSER_SET_ONE2MANY_CHILD___
___SYNC_PARSER_SET_ONE2MANY_CHILD_ALERT___

            yyyyys.add(yyyyy);
          }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return yyyyys;
    }



    public List<String> parseDelete(String inputStr) {

        List<String> deleteIds = new ArrayList<String>();

        try {
          JSONArray jsonArray = new JSONArray(inputStr);
          for (int i = 0; i < jsonArray.length(); i++) {
            String idStr = jsonArray.getString(i);
            deleteIds.add(idStr);
          }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return deleteIds;
    }



    public String parseYyyyyList(List<Yyyyy> yyyyys) {

        String jsonStr = "[";
        boolean first = true;

        for (Yyyyy yyyyy : yyyyys) {
            if (first)
                first = false;
            else
                jsonStr = jsonStr + ",";

            JSONObject jGroup = new JSONObject();// list Object

            try {
                jGroup.put("id", yyyyy.getId().toString());
                jGroup.put("deviceId", yyyyy.getDeviceId().toString());
                jGroup.put("yyyyyId", yyyyy.getYyyyyId().toString());
                jGroup.put("lastUpdate", Long.toString(yyyyy.getLastUpdate().getTime()));
                jGroup.put("deleteFlag", yyyyy.getDeleteFlag().toString());

___SYNC_PARSER_JGROUP_INTEGER___
___SYNC_PARSER_JGROUP_STRING___
___SYNC_PARSER_JGROUP_CLOB___
___SYNC_PARSER_JGROUP_ENUM___
___SYNC_PARSER_JGROUP_TAG___
___SYNC_PARSER_JGROUP_DOUBLE___
___SYNC_PARSER_JGROUP_MONEY___
___SYNC_PARSER_JGROUP_BOOLEAN___
___SYNC_PARSER_JGROUP_CAMERA___
___SYNC_PARSER_JGROUP_VIDEO___
___SYNC_PARSER_JGROUP_THUMBNAIL___
___SYNC_PARSER_JGROUP_POST___
___SYNC_PARSER_JGROUP_LOC___
___SYNC_PARSER_JGROUP_CURRENT_LOC___
___SYNC_PARSER_JGROUP_DATE___
___SYNC_PARSER_JGROUP_DATE_TIME___
___SYNC_PARSER_JGROUP_ONE2MANY_CHILD___
___SYNC_PARSER_JGROUP_ONE2MANY_CHILD_ALERT___

                jsonStr = jsonStr + jGroup.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        jsonStr = jsonStr + "]";

        try {
          jsonStr = URLEncoder.encode(jsonStr, "ASCII").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
          System.err.println(e);
        }
        return jsonStr;
    }

}
