package ppp.ppp.ppp;

import android.app.Activity;
//import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import android.os.Environment;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

/**
 * Lllll
 *
 * This is a simple XxxxxThumbnailView activity that displays
 * a thumbnail image.
 * It is a basic full-screen viewer for thumbnail images stored
 * by the app.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class XxxxxThumbnailView extends Activity {

  private Resources res;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.thumbnail_view);
    res = getResources();

    Bundle extras = getIntent().getExtras();
    String thumbnailPath = extras.getString("thumbnailPath");

    ImageView thumbnail = (ImageView)findViewById(R.id.thumbnail); 
    thumbnail.getLayoutParams().height = 150;

    File[] sdDirs = this.getExternalFilesDirs(Environment.DIRECTORY_PICTURES);
    int pick = sdDirs.length - 1;
    File thumbnailFile = new  File(sdDirs[pick],thumbnailPath);
    if(thumbnailFile.exists()){
      Bitmap thumbnailBitmap = BitmapFactory.decodeFile(thumbnailFile.getAbsolutePath());
      thumbnail.setImageBitmap(thumbnailBitmap);

    } else {
      Toast.makeText(XxxxxThumbnailView.this, res.getString(R.string.noThumbnail), Toast.LENGTH_SHORT).show();
      finish();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.thumbnail_menu, menu);
    return true;
  }

  /**
   * Event Handling for Individual menu item selected
   * Identify single menu item by it's id
   * */
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
        
    int rId = item.getItemId();        

    if (rId == R.id.menu_return) {

        finish();
    }

    return super.onOptionsItemSelected(item);
 
  } 

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

} 
