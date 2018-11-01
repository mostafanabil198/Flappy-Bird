package com.yalla.flappy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import helpers.GameManager;


public class AndroidLauncher extends AndroidApplication implements RewardedVideoAdListener, AdHandler {
    protected AdView adView;
    private RewardedVideoAd mRewardedVideoAd;
    private final int LOAD_VIDEO = 0;
    private final int OPEN_VIDEO = 1;
    private final int CANT_BUY = 2;
    private int forWhat;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_VIDEO:
                    loadRewardedVideoAd();
                    break;
                case OPEN_VIDEO:
                    openRewardedVideoAd();
                    break;
                case CANT_BUY:
                    cantBuy();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        //BANNER ADS
        //App ID HERE....
        MobileAds.initialize(this, "ca-app-pub-2204069511670732~4021708863");
        final RelativeLayout layout = new RelativeLayout(this);
        final View gameView = initializeForView(new GameMain(this), config);
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        adView = new AdView(this);

        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-2204069511670732/7456738602");

        //FOR TEST adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest.Builder builder = new AdRequest.Builder();
        //builder.addTestDevice("ca-app-pub-3940256099942544/6300978111");
        final LinearLayout.LayoutParams adParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;

        final LinearLayout.LayoutParams gameParms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
        adView.loadAd(builder.build());

        linearLayout.addView(gameView, gameParms);
        linearLayout.addView(adView, adParams);

        layout.addView(linearLayout);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                final LinearLayout.LayoutParams gameParms1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height - adView.getAdSize().getHeightInPixels(getContext()));
                gameView.setLayoutParams(gameParms1);
            }
        });

        //REWARDED VIDEOS
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        setContentView(layout);


        //leaderBoard
    }


    private void cantBuy() {
        Toast.makeText(this, "You Dont Have Enough Coins! watch video for free coins", Toast.LENGTH_LONG).show();
    }


    private void loadRewardedVideoAd() {

        //FOR TEST ca-app-pub-3940256099942544/5224354917
        mRewardedVideoAd.loadAd("ca-app-pub-2204069511670732/6665405267",
                new AdRequest.Builder().build());
    }

    private void openRewardedVideoAd() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            Toast.makeText(this, "There's No videos to watch now .. Make sure network is opened OR try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onRewarded(RewardItem reward) {
        if (forWhat == 1) {
            Toast.makeText(this, "Good! you earned " + reward.getAmount() + " " + reward.getType(), Toast.LENGTH_SHORT).show();
            GameManager.getInstance().coinsReward();
            forWhat = 0;
        } else if (forWhat == 2) {
            Toast.makeText(this, "Good! you have extra life now..", Toast.LENGTH_SHORT).show();
            GameManager.getInstance().extraLifeReward();
            forWhat = 0;
        } else if (forWhat == 3) {
            Toast.makeText(this, "you earned 1 " + reward.getType(), Toast.LENGTH_SHORT).show();
            GameManager.getInstance().coinsReward1();
            forWhat = 0;
        }
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
        forWhat = 0;
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
    }

    @Override
    public void onRewardedVideoAdLoaded() {
    }

    @Override
    public void onRewardedVideoAdOpened() {
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoCompleted() {
    }

    @Override
    public void loadVideo() {
        handler.sendEmptyMessage(LOAD_VIDEO);
    }

    @Override
    public void openVideo(int forWhat) {
        handler.sendEmptyMessage(OPEN_VIDEO);
        this.forWhat = forWhat;
    }

    @Override
    public void toast(String s) {
        handler.sendEmptyMessage(CANT_BUY);


    }

}
