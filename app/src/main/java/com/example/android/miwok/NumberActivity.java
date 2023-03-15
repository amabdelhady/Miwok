package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NumberActivity extends AppCompatActivity {
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
        final ArrayList<Word> numbers = new ArrayList<>();
        numbers.add(new Word("One","Luttie", R.drawable.number_one, R.raw.number_one));
        numbers.add(new Word("Two","Otiiko", R.drawable.number_two, R.raw.number_two));
        numbers.add(new Word("Three","Tolookosu", R.drawable.number_three, R.raw.number_three));
        numbers.add(new Word("Four","Oyyisa", R.drawable.number_four, R.raw.number_four));
        numbers.add(new Word("Five","Massokka",R.drawable.number_five, R.raw.number_five));
        numbers.add(new Word("Six","Temmokka", R.drawable.number_six, R.raw.number_six));
        numbers.add(new Word("Seven","Kenekaku",R.drawable.number_seven, R.raw.number_seven));
        numbers.add(new Word("Eight","Kawinta", R.drawable.number_eight, R.raw.number_eight));
        numbers.add(new Word("Nine","Wo'e", R.drawable.number_nine, R.raw.number_nine));
        numbers.add(new Word("Ten","Na'aacha", R.drawable.number_ten, R.raw.number_ten));

        ListView rootView = findViewById(R.id.word_list);
        WordAdapter itemsAdapter = new WordAdapter(this, numbers, R.color.category_numbers);
        rootView.setAdapter(itemsAdapter);

        rootView.setOnItemClickListener((adapterView, view, position, id) -> {
            releaseMediaPlayer();
            Word word = numbers.get(position);
            int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                mMediaPlayer = MediaPlayer.create(NumberActivity.this, word.getmMiwokAudioID());
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
