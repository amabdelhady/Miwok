package com.example.android.miwok;

public class Word {
    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private int miImageView = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mMiwokAudioID;


    public Word(String defaultTranslation, String miwokTranslation, int view, int audio){
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation  = miwokTranslation;
        miImageView = view;
        mMiwokAudioID = audio;
    }

    public Word(String defaultTranslation, String miwokTranslation, int miwokAudio){
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation  = miwokTranslation;
        mMiwokAudioID = miwokAudio;
    }


    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }
    public String getmMiwokTranslation(){
        return mMiwokTranslation;
    }
    public int getMiImageView(){return miImageView;} //RETURN IMAGE ID OF THE WORD

    public boolean hasValidImage(){
        return miImageView != NO_IMAGE_PROVIDED;
    }
    public int getmMiwokAudioID(){return mMiwokAudioID;}


}
