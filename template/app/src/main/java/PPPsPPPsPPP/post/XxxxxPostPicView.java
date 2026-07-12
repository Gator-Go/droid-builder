package ppp.ppp.ppp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.github.chrisbanes.photoview.PhotoView;

public class XxxxxPostPicView extends AppCompatActivity {

  private Resources res;
  private ImageHelper imageHelper = new ImageHelper();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.post_view);
    res = getResources();

    // Initialize Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    if (toolbar != null) {
        setSupportActionBar(toolbar);
    }

    Bundle extras = getIntent().getExtras();
    String postPath = extras.getString("postPath");

    PhotoView post = (PhotoView)findViewById(R.id.post); 

    File[] sdDirs = this.getExternalFilesDirs(Environment.DIRECTORY_PICTURES);
    int pick = sdDirs.length - 1;
    File postFile = new  File(sdDirs[pick],postPath);
    if(postFile.exists()){
      Bitmap postBitmap = imageHelper.getDisplaySizeBitmap(res, postFile);
      post.setImageBitmap(postBitmap);

    } else {
      Toast.makeText(XxxxxPostPicView.this, res.getString(R.string.noPost), Toast.LENGTH_SHORT).show();
      finish();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.post_menu, menu);
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
