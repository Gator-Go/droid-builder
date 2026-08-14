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
 * Lllll
 *
 * This is a simple AsyncTask (FileDeleteTask) that deletes a file in the background.
 * It takes a File in the constructor and attempts to delete it inside doInBackground,
 * logging success or failure. It is a background helper for safely deleting photo/video files.
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


