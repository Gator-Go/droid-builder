package ppp.ppp.ppp;

import android.app.Activity;
import android.widget.Toast;
import android.util.Log;

import java.io.File;
import android.os.AsyncTask;
import android.os.Environment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;


/**
 * Developed by PojoMax Incorporated
 *  
 */

/**
 * This entity bean is used to manage the SERVICE_RECORD database table.<br>
 * This class follows the POJO model so there is a standard get<br>
 * and set method on each data item.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */


public class FileDeleteTask extends AsyncTask<Void, Void, Void> {

  private File photoFile;

  public FileDeleteTask(File photoFile) {
    this.photoFile = photoFile;
  }

  @Override
  protected Void doInBackground(Void... params) {

    try{
      if(photoFile.delete())
        Log.d("*** FileDeleteTask ***", photoFile.getName() + " is deleted!");
    } catch (Exception error) {
      Log.d("*** FileDeleteTask ***", photoFile.getName() + " is not deleted! " + error.getMessage());
    }
    return null;
  }
}


