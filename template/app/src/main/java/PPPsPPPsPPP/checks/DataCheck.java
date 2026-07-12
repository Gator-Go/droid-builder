package ppp.ppp.ppp.checks;


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


public class DataCheck {

  public DataCheck() {
  }

  public boolean validLatitude(Double latitude) {

    if (latitude.doubleValue() >= -90.0 && latitude.doubleValue() <= 90.0)
      return true;
    else
      return false; 
  }

  public boolean validLongitude(Double longitude) {

    if (longitude.doubleValue() >= -180.0 && longitude.doubleValue() <= 180.0)
      return true;
    else
      return false; 
  }

  public static boolean isNumeric(String str) {  
      try  {  
        double d = Double.parseDouble(str);  
      }  catch(NumberFormatException nfe) {  
        return false;  
      }  
      return true;  
  }

}
