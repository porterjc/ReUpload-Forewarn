package com.forewarn.forewarn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProcessingActivity extends AppCompatActivity {

    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private String foundText;

    private Handler mHandler = new Handler();

    // currently being done in UI thread - might want to stick it in another thread
    private Bitmap loadAndRotateImage(final String path) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        // TODO:  Look more into these options
        int scaleFactor = 10;
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap oBitmap = BitmapFactory.decodeFile(path, bmOptions);
        // image loaded - now we need to rotate it
        try {
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Matrix matrix = new Matrix();
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
                default:
                    matrix.postRotate(0);
                    break;
            }
            oBitmap = Bitmap.createBitmap(oBitmap, 0, 0, oBitmap.getWidth(), oBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap grayscale = Bitmap.createBitmap(oBitmap.getWidth(), oBitmap.getHeight(), oBitmap.getConfig());
        Canvas c = new Canvas(grayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(oBitmap, 0, 0, paint);


        return grayscale;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_processing);

        foundText = "";

        ImageView thumbnail = (ImageView) findViewById(R.id.imageThumbnail);
        int targetW = thumbnail.getWidth();
        int targetH = thumbnail.getHeight();


        Intent callingIntent = getIntent();
        final String photoPath = callingIntent.getStringExtra("photoPath");
        final Bitmap bitmap = loadAndRotateImage(photoPath);

        thumbnail.setImageBitmap(bitmap);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {

            public void post() {
                // Update the progress bar
                mHandler.post(new Runnable() {
                    public void run() {
                        mProgress.setProgress(mProgressStatus);
                    }
                });
            }

            public void run() {
                mProgressStatus = 0;
                post();

                // put assets into cache
                String[] toCache = {"eng.cube.bigrams", "eng.cube.fold", "eng.cube.lm", "eng.cube.nn", "eng.traineddata",
                                    "eng.cube.params", "eng.cube.size", "eng.cube.word-freq", "eng.tesseract_cube.nn"};
                File tessDir = new File(getExternalFilesDir(null) + "/tessdata/");
                if(!tessDir.exists()) {
                    tessDir.mkdirs();
                }
                for(String active : toCache) {
                    File f = new File(getExternalFilesDir(null) + "/tessdata/" + active);
                    Log.d("FOREWARN", active + " path:  " + f.getAbsolutePath());
                    if(!f.exists()) {
                        Log.d("FOREWARN", active + " repopulating.");
                        try {
                            InputStream is = getAssets().open(active);
                            int size = is.available();
                            byte[] buffer = new byte[size];
                            is.read(buffer);
                            is.close();

                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(buffer);
                            fos.close();
                        } catch (Exception  e) {
                            e.printStackTrace();
                        }
                    }
                }
                mProgressStatus = 10;
                post();

                // run OCR
                TessBaseAPI baseApi = new TessBaseAPI();
                String dataPath = getExternalFilesDir(null) + "/";
                Log.d("FOREWARN", "dataPath: " + dataPath);
                String lang = "eng";
                baseApi.init(dataPath, lang);

                baseApi.setImage(bitmap);

                String recognizedText = baseApi.getUTF8Text();
                foundText = recognizedText;
                Log.d("FOREWARN", "Found: " + recognizedText);
                baseApi.end();
                mProgressStatus = 90;
                post();
                File file = new File(photoPath);
                boolean deleted = file.delete();
                Log.d("FOREWARN", "Image deleted: " + Boolean.toString(deleted));
                // delete image
                mProgressStatus = 100;
                post();
                Intent intent = new Intent(getBaseContext(), ResultsActivity.class ); // create the intent
                intent.putExtra("temp", foundText);
                startActivity(intent); // start the results activity
            }
        }).start();

    }

    public String getFoundText(){
        return foundText;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_processing, menu);
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

    public void navResults(View view) {
        Intent intent = new Intent( this, ResultsActivity.class ); // create the intent
        startActivity( intent ); // start the camera activity
    }
}
