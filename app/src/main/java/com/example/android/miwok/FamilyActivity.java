package com.example.android.miwok;

//import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {
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
        ArrayList<Word> family = new ArrayList<>();
        family.add(new Word("father","әpә",R.drawable.family_father, R.raw.family_father));
        family.add(new Word("mother","әṭa", R.drawable.family_mother, R.raw.family_mother));
        family.add(new Word("son","angsi", R.drawable.family_son, R.raw.family_son));
        family.add(new Word("daughter","tune", R.drawable.family_daughter, R.raw.family_daughter));
        family.add(new Word("older brother\n" +
                "\n","taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        family.add(new Word("younger brother\n" +
                "\n","chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        family.add(new Word("older sister\n" +
                "\n","teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        family.add(new Word("younger sister\n" +
                "\n","kolliti", R.drawable.family_younger_sister, R.raw.family_younger_brother));
        family.add(new Word("grandmother","ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        family.add(new Word("grandfather","paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        ListView rootView = findViewById(R.id.word_list);
        WordAdapter itemAdapter = new WordAdapter(this, family, R.color.category_family);
        rootView.setAdapter(itemAdapter);
        rootView.setOnItemClickListener((adapterView, view, position, id)->{
            Word word = family.get(position);
            releaseMediaPlayer();
            int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                mMediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getmMiwokAudioID());
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
