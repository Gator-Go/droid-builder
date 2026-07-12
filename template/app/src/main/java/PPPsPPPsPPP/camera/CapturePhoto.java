package ppp.ppp.ppp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.media.MediaActionSound;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageCapture.OutputFileOptions;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.camera.lifecycle.ProcessCameraProvider;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CapturePhoto extends AppCompatActivity {
    private ImageCapture imageCapture;
    private PreviewView previewView;
    private ImageView captureImage;
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private Preview previewUseCase;
    private String photoPath;
    private File photoFile;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_photo);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Bundle extras = getIntent().getExtras();
        photoPath = extras.getString("PhotoPath");

        previewView = findViewById(R.id.previewView);
        captureImage = findViewById(R.id.captureImage);
        progressBar = findViewById(R.id.progressBar);

        // Initialize CameraX
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                startCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));

        // Set up buttons
        findViewById(R.id.captureButton).setOnClickListener(v -> capturePhoto());
        findViewById(R.id.cancelButton).setOnClickListener(v -> cancelPhoto());
        findViewById(R.id.backButton).setOnClickListener(v -> goBack());
    }

    private void startCamera() {
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        previewUseCase = new Preview.Builder().build();

        imageCapture = new ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)  // Or MAX_QUALITY for better res
            .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())  // Handles rotation automatically
            .build();
        previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());

        try {
            cameraProvider.unbindAll();
            Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, previewUseCase, imageCapture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void capturePhoto() {
        // Show progress indicator (optional)
        showProgress(true);

        // Freeze the preview
        freezePreview(true);

        // Configure output file
        photoFile = new File(photoPath);
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        // Play capture sound (optional)
        playCameraShutterSound();

        // Take picture
        Executor executor = ContextCompat.getMainExecutor(this);
        imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                // Image saved successfully
                Toast.makeText(CapturePhoto.this, "Photo Captured", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }

            @Override
            public void onError(ImageCaptureException exception) {
                // Handle error
                Toast.makeText(CapturePhoto.this, "Photo Capture failed", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }

    private void cancelPhoto() {
        photoFile.delete();
        freezePreview(false);
    }

    private void goBack() {
        finish();
    }

    private void freezePreview(boolean freeze) {
        if (freeze) {
            Bitmap bitmap = previewView.getBitmap();
            if (bitmap != null) {
                captureImage.setImageBitmap(bitmap);
                captureImage.setVisibility(View.VISIBLE);
                previewView.setVisibility(View.INVISIBLE);
            }
        } else {
            captureImage.setVisibility(View.INVISIBLE);
            previewView.setVisibility(View.VISIBLE);
        }
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void playCameraShutterSound() {
        MediaActionSound sound = new MediaActionSound();
        sound.play(MediaActionSound.SHUTTER_CLICK);
        sound.release();
    }
}
