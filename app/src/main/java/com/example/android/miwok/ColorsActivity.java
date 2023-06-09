package com.example.android.miwok;

//import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

   private MediaPlayer mMediaPlayer;
   private AudioManager mAudioManager;
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    private MediaPlayer.OnCompletionListener mOnCompletionListener = mediaPlayer -> releaseMediaPlayer();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ArrayList<Word> colors = new ArrayList<>();
        colors.add(new Word("red","weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        colors.add(new Word("green","chokokki", R.drawable.color_green, R.raw.color_green));
        colors.add(new Word("brown","ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        colors.add(new Word("gray","ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        colors.add(new Word("black","kululli", R.drawable.color_black, R.raw.color_black));
        colors.add(new Word("white","kelelli", R.drawable.color_white, R.raw.color_white));
        colors.add(new Word("dusty yellow\n" +
                "\n","ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));

        colors.add(new Word("mustard yellow","chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        ListView rootView = findViewById(R.id.word_list);
        WordAdapter itemAdapter = new WordAdapter(this, colors, R.color.category_colors);
        rootView.setAdapter(itemAdapter);

        rootView.setOnItemClickListener((adapterView, view, position, id) -> {
            Word word = colors.get(position);
            releaseMediaPlayer();
            int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getmMiwokAudioID());
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            }
        });
    }

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener(){
                public void onAudioFocusChange (int focusChange){
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    }else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }
                }
            };

    @Override
    protected void onStop(){
        super.onStop();
        releaseMediaPlayer();
    }
}
