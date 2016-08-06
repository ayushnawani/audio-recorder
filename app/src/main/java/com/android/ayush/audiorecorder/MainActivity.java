package com.android.ayush.audiorecorder;

import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    Button play,stop,record,save;
    private MediaRecorder audioRecorder;
    private String outputFile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        play = (Button)findViewById(R.id.playButton);
        stop = (Button)findViewById(R.id.stopButton);
        record = (Button)findViewById(R.id.recordButton);
        save = (Button)findViewById(R.id.saveButton);


        play.setOnClickListener(audioRecorderListener);
        stop.setOnClickListener(audioRecorderListener);
        record.setOnClickListener(audioRecorderListener);
        save.setOnClickListener(audioRecorderListener);

        stop.setEnabled(false);
        play.setEnabled(false);
        save.setEnabled(false);

        File outputFolder = new File(Environment.getExternalStorageDirectory() + "/tmp");

        if(outputFolder.isDirectory()) {
          outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp/audioFile.3gp";
        }
        else
        {
            File audioDirectory = new File("/sdcard/tmp");
            audioDirectory.mkdirs();
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp/audioFile.3gp";
        }

        Log.d("MainActivity :" , outputFile);

        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audioRecorder.setOutputFile(outputFile);

    }

    private View.OnClickListener audioRecorderListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.playButton :

                    MediaPlayer mediaPlayer = new MediaPlayer();

                    try {
                        mediaPlayer.setDataSource(outputFile);
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        Toast.makeText(getApplicationContext(), "Playing Audio" , Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                case R.id.recordButton :

                    try {
                        audioRecorder.prepare();
                        audioRecorder.start();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    record.setEnabled(false);
                    stop.setEnabled(true);
                    save.setEnabled(false);

                    Toast.makeText(getApplicationContext(), "Recording Started" , Toast.LENGTH_LONG).show();

                    break;

                case R.id.stopButton :

                    audioRecorder.stop();
                    audioRecorder.release();
                    audioRecorder = null;

                    stop.setEnabled(false);
                    play.setEnabled(true);
                    save.setEnabled(true);

                    Toast.makeText(getApplicationContext(), "Audio recorded Successfully" , Toast.LENGTH_LONG).show();

                    break;

                case R.id.saveButton :

                    File outputFolder = new File(Environment.getExternalStorageDirectory() + "/AudioRecorded/audioFile.3gp");
                    File inputFolder = new File(Environment.getExternalStorageDirectory() + "/tmp/audioFile.3gp");

                    try {
                        copyFile(inputFolder,outputFolder);
                        Toast.makeText(getApplicationContext(), "Audio Saved Successfully" , Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    public void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
