package com.forewarn.forewarn;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if( intent.resolveActivity(getPackageManager()) != null ) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the file


            }
            if(photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE ) {
            if( resultCode == RESULT_OK ) {
                Intent intent = new Intent( this, ProcessingActivity.class);
                intent.putExtra("photoPath", mCurrentPhotoPath);
                startActivity(intent);
                finish();
            }  else if (resultCode == RESULT_CANCELED ) {
                Intent intent = new Intent( this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // something went wrong
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void navProcessing(View view) {
        Intent intent = new Intent( this, ProcessingActivity.class ); // create the intent
        startActivity( intent ); // start the camera activity
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        storageDir.mkdirs();
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
