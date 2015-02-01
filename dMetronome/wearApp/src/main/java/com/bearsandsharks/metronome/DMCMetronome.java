package com.bearsandsharks.metronome;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.wearable.view.CircledImageView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DMCMetronome {

    private static final int MSG = 1;
    protected static boolean mRunning = false;

    Vibrator mVibrator;
//    SharedPreferences mSharedPreferences;

    View mBackground;
    TextView tCount;
    Drawable mDefaultBackground;

    int mCount = 0;
    int mPeriod;
    long mTickDuration;
    boolean isFlashEnabled, alwaysOnStatus;
    // set this somwhere
    boolean isClient = true;

    Context mContext;

    public DMCMetronome(Context context, Vibrator vibrator, View view) {
        mContext = context;
        mVibrator = vibrator;
        mBackground = view;
        tCount = (TextView) view.findViewById(R.id.Count);
        mDefaultBackground = view.getBackground();
//        mSharedPreferences = context.getSharedPreferences("dMetronome", 0);
    }

    public int getMPeriod() {
        return mPeriod;
    }

    public void startTick(int ticksPerSec) {

            mRunning = true;
            mCount = 0;
            //mPeriod is count default set to 4s
            if(DMAMain.isClient) {
                mPeriod = ClientTempo.timeSig;
            }
            else {
                mPeriod = DMFSetTempo.mPeriod;
            }

//            isFlashEnabled = mSharedPreferences.getBoolean("flash", true);
//            alwaysOnStatus = mSharedPreferences.getBoolean("alwaysOn", false);
            tCount.setText(Integer.toString(1));

//            if (alwaysOnStatus) {
//                ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            }
            //RENAME THIS tick duration represents the delay on the tick
            mTickDuration = 60000 / ticksPerSec;
            //call tick when you start tick
            tick();
    }

    private void tick() {
        if (!mRunning) return;

            //mPeriod is count not sure why we need != 1
            if (mCount - mPeriod == 0) {
                mCount = 0;
//                if(isFlashEnabled) {
//                    mBackground.setBackgroundColor(Color.parseColor("#000000"));
//                    tCount.setTextColor(Color.parseColor("#ffffff"));
//                }
                    mVibrator.vibrate(120);
                }
                else {
//                if(isFlashEnabled) {
//                    mBackground.setBackground(mDefaultBackground);
//                    tCount.setTextColor(Color.parseColor("#ffffff"));
//                }
                    mVibrator.vibrate(100);
                    }


            //calls the Handler with the delay of mTickDuration for example 60000 / 60bpm = a tick every 1 second
            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), mTickDuration);
    }

    // override the quiet mode and actually exit
    public void stopTick(boolean force) {
            mRunning = false;
            mCount = 0;
            mBackground.setBackground(mDefaultBackground);
            tCount.setTextColor(Color.parseColor("#000000"));
            //I think removeMessages means make MSG 0 or something???
            mHandler.removeMessages(MSG);

//            if (alwaysOnStatus) {
//                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            }
    }
    public void stopTick() {
            mRunning = false;
            mCount = 0;
            mBackground.setBackground(mDefaultBackground);
            tCount.setTextColor(Color.parseColor("#000000"));
            //I think removeMessages means make MSG 0 or something???
            mHandler.removeMessages(MSG);

//            if (alwaysOnStatus) {
//                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            }
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            mCount++;
            tick();
            tCount.setText(Integer.toString(mCount + 1));

            //maybe do rotation animations here
        }
    };
}