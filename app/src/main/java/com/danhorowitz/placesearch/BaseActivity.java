package com.danhorowitz.placesearch;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by danielhorowitz on 10/24/15.
 */
public abstract class BaseActivity extends AppCompatActivity{
    protected boolean mIsPaused;
    @Override
    protected void onResume() {
        super.onResume();
        mIsPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPaused = true;
    }
}
