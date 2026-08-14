package ppp.ppp.ppp.pojos;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.sql.Clob;
import java.sql.Blob;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

import android.os.Parcel;
import android.os.Parcelable;
 
/**
 * Lllll
 *
 * This is the main Yyyyy POJO (data model) that implements Parcelable.
 * It is the central data entity representing a Yyyyy record, designed
 * to be generated for many different field types.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class Yyyyy implements Parcelable
{

   // ID field from database
   private Long id;
   private Long deviceId;
   private Long yyyyyId;
   private Date lastUpdate = new Date();
   private Integer deleteFlag = new Integer(0);

___ENTITY_DEFINITION_INTEGER___
___ENTITY_DEFINITION_STRING___
___ENTITY_DEFINITION_DOUBLE___
___ENTITY_DEFINITION_MONEY___
___ENTITY_DEFINITION_CLOB___
___ENTITY_DEFINITION_BLOB___
___ENTITY_DEFINITION_IMAGE___
___ENTITY_DEFINITION_ENUM___
___ENTITY_DEFINITION_TAG___
___ENTITY_DEFINITION_DATE___
___ENTITY_DEFINITION_DATE_TIME___
___ENTITY_DEFINITION_BOOLEAN___
___ENTITY_DEFINITION_CAMERA___
___ENTITY_DEFINITION_VIDEO___
___ENTITY_DEFINITION_THUMBNAIL___
___ENTITY_DEFINITION_POST___
___ENTITY_DEFINITION_LOC___
___ENTITY_DEFINITION_CURRENT_LOC___
___ENTITY_DEFINITION_ONE2MANY_CHILD___
___ENTITY_DEFINITION_ONE2MANY_CHILD_ALERT___
___ENTITY_DEFINITION_LOC_SERVICE___
___ENTITY_DEFINITION_SECURITY___

   /**
    * Constructor
    * 
    */
   public Yyyyy()
   {

   }



   /**
    * Get method for id data item
    * 
    * @return <code>Long</code> object that indicates the id.
    */
   public Long getId()
   {
      return id;
   }
   /**
    * Set method for id data item
    * 
    * @param id <code>Long</code> object that sets the id.
    */
   public void setId(Long id)
   {
      this.id = id;
   }


   /**
    * Get method for deviceId data item
    * 
    * @return <code>Long</code> object that indicates the deviceId.
    */
   public Long getDeviceId()
   {
      return deviceId;
   }
   /**
    * Set method for deviceId data item
    * 
    * @param deviceId <code>Long</code> object that sets the deviceId.
    */
   public void setDeviceId(Long deviceId)
   {
      this.deviceId = deviceId;
   }


   /**
    * Get method for yyyyyId data item
    * 
    * @return <code>Long</code> object that indicates the yyyyyId.
    */
   public Long getYyyyyId()
   {
      return yyyyyId;
   }
   /**
    * Set method for yyyyyId data item
    * 
    * @param yyyyyId <code>Long</code> object that sets the yyyyyId.
    */
   public void setYyyyyId(Long yyyyyId)
   {
      this.yyyyyId = yyyyyId;
   }

   /**
    * Get method for lastUpdate data item
    * 
    * @return <code>Date</code> object that indicates the lastUpdate.
    */
   public Date getLastUpdate()
   {
      return lastUpdate;
   }
   /**
    * Set method for lastUpdate data item
    * 
    * @param lastUpdate <code>Date</code> object that sets the lastUpdate.
    */
   public void setLastUpdate(Date lastUpdate)
   {
      this.lastUpdate = lastUpdate;
   }

   /**
    * Get method for deleteFlag data item
    * 
    * @return <code>Integer</code> object that indicates the deleteFlag.
    */
   public Integer getDeleteFlag()
   {
      return deleteFlag;
   }
   /**
    * Set method for deleteFlag data item
    * 
    * @param deleteFlag <code>Integer</code> object that sets the deleteFlag.
    */
   public void setDeleteFlag(Integer deleteFlag)
   {
      this.deleteFlag = deleteFlag;
   }

___ENTITY_METHOD_INTEGER___
___ENTITY_METHOD_STRING___
___ENTITY_METHOD_DOUBLE___
___ENTITY_METHOD_MONEY___
___ENTITY_METHOD_CLOB___
___ENTITY_METHOD_BLOB___
___ENTITY_METHOD_IMAGE___
___ENTITY_METHOD_ENUM___
___ENTITY_METHOD_TAG___
___ENTITY_METHOD_DATE___
___ENTITY_METHOD_DATE_TIME___
___ENTITY_METHOD_BOOLEAN___
___ENTITY_METHOD_CAMERA___
___ENTITY_METHOD_VIDEO___
___ENTITY_METHOD_THUMBNAIL___
___ENTITY_METHOD_POST___
___ENTITY_METHOD_LOC___
___ENTITY_METHOD_CURRENT_LOC___
___ENTITY_METHOD_ONE2MANY_CHILD___
___ENTITY_METHOD_ONE2MANY_CHILD_ALERT___
___ENTITY_METHOD_LOC_SERVICE___
___ENTITY_METHOD_SECURITY___



   public String getCloudId() {

      return deviceId.toString() + "-" + yyyyyId.toString();
   }



   public Yyyyy(Parcel in) {
      readFromParcel(in);
   } 

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeLong(id);
      dest.writeLong(deviceId);
      dest.writeLong(yyyyyId);
      dest.writeLong(lastUpdate.getTime());
      dest.writeInt(deleteFlag);
___PARCEL_WRITE_INTEGER___
___PARCEL_WRITE_STRING___
___PARCEL_WRITE_CLOB___
___PARCEL_WRITE_ENUM___
___PARCEL_WRITE_TAG___
___PARCEL_WRITE_DOUBLE___
___PARCEL_WRITE_MONEY___
___PARCEL_WRITE_DATE___
___PARCEL_WRITE_DATE_TIME___
___PARCEL_WRITE_BOOLEAN___
___PARCEL_WRITE_CAMERA___
___PARCEL_WRITE_VIDEO___
___PARCEL_WRITE_THUMBNAIL___
___PARCEL_WRITE_POST___
___PARCEL_WRITE_LOC___
___PARCEL_WRITE_CURRENT_LOC___
___PARCEL_WRITE_ONE2MANY_CHILD___
___PARCEL_WRITE_ONE2MANY_CHILD_ALERT___
___PARCEL_WRITE_LOC_SERVICE___
___PARCEL_WRITE_SECURITY___
   }


   private void readFromParcel(Parcel in) {
      id = in.readLong();
      deviceId = in.readLong();
      yyyyyId = in.readLong();
      lastUpdate = new Date(in.readLong());
      deleteFlag = in.readInt();
___PARCEL_READ_INTEGER___
___PARCEL_READ_STRING___
___PARCEL_READ_CLOB___
___PARCEL_READ_ENUM___
___PARCEL_READ_TAG___
___PARCEL_READ_DOUBLE___
___PARCEL_READ_MONEY___
___PARCEL_READ_DATE___
___PARCEL_READ_DATE_TIME___
___PARCEL_READ_BOOLEAN___
___PARCEL_READ_CAMERA___
___PARCEL_READ_VIDEO___
___PARCEL_READ_THUMBNAIL___
___PARCEL_READ_POST___
___PARCEL_READ_LOC___
___PARCEL_READ_CURRENT_LOC___
___PARCEL_READ_ONE2MANY_CHILD___
___PARCEL_READ_ONE2MANY_CHILD_ALERT___
___PARCEL_READ_LOC_SERVICE___
___PARCEL_READ_SECURITY___
   }


   public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
      public Yyyyy createFromParcel(Parcel in) {
         return new Yyyyy(in);
      }

      public Yyyyy[] newArray(int size) {
         return new Yyyyy[size];
      }
  };

}
