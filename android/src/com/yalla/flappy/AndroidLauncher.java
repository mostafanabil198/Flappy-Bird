package com.yalla.flappy;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.yalla.flappy.GameMain;

public class AndroidLauncher extends AndroidApplication {
    private static final String TAG = "AndroidLauncher";
    protected AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");
        final RelativeLayout layout = new RelativeLayout(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        final View gameView = initializeForView(new GameMain(), config);
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        adView = new AdView(this);

        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice("ca-app-pub-3940256099942544/6300978111");
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
        setContentView(layout);
    }
}
