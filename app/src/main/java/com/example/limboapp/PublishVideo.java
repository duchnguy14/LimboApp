package com.example.limboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

public class PublishVideo extends AppCompatActivity {

    VideoView videoPreview;
    MediaPlayer mediaPlayer;
    Button publishButton;
    EditText inputDescription;

    String description;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_video);
        
        videoPreview = findViewById(R.id.videoPreview);

        //TODO: Get stream-able video path
        videoPreview.setVideoPath("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.krispy);

        videoPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
                mediaPlayer.setLooping(true);
            }
        });
        videoPreview.start();

        videoPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoPreview.isPlaying()) {
                    mediaPlayer.pause();
                }
                else {
                    mediaPlayer.start();
                }
            }
        });

        inputDescription = findViewById(R.id.inputDescription);

        publishButton = findViewById(R.id.publishButton);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = inputDescription.getText().toString();

                //TODO: upload video to firebase storage
                //TODO: get storage path
                //TODO: put in database under videos/{unique path}/
                // likes set to 0
                // description set to user's input description
            }
        });
    }
}
