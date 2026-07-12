package ppp.ppp.ppp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;
import android.widget.Toast;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import android.net.Uri;
import android.os.Environment;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class XxxxxPostVidView extends AppCompatActivity {

  private Resources res;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.video_view);
    res = getResources();

    // Initialize Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    if (toolbar != null) {
        setSupportActionBar(toolbar);
    }

    Bundle extras = getIntent().getExtras();
    String videoPath = extras.getString("postPath");

    VideoView video = (VideoView)findViewById(R.id.video); 

    File[] sdDirs = this.getExternalFilesDirs(Environment.DIRECTORY_PICTURES);
    int pick = sdDirs.length - 1;
    File videoFile = new  File(sdDirs[pick],videoPath);
    if(videoFile.exists()){
      Uri uri = Uri.fromFile(videoFile);
      video.setVideoURI(uri);
      video.requestFocus();
      video.start();

    } else {
      Toast.makeText(XxxxxPostVidView.this, res.getString(R.string.noVideo), Toast.LENGTH_SHORT).show();
      finish();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.video_menu, menu);
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
