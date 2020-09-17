package com.alert.jobsrefter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alert.jobsrefter.Activities.Categories_Activity;
import com.alert.jobsrefter.Activities.ImagesActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //declare views-------------------------------------------------------------------
    CardView btn_dpt, btn_latest, btn_int, btn_qulf, btn_pdate, btn_prof;

    ImageView btn_search;

    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-9448997413076062/3838535092";
    private UnifiedNativeAd nativeAd;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("JobNews");

        MobileAds.initialize(this, "ca-app-pub-9448997413076062~4924278257");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //initialize view-----------------------------------------------------------
        initializeViews();



        //Views clicks--------------------------------------------------------------
        btn_dpt.setOnClickListener(this);
        btn_latest.setOnClickListener(this);
        btn_int.setOnClickListener(this);
        btn_qulf.setOnClickListener(this);
        btn_pdate.setOnClickListener(this);
        btn_prof.setOnClickListener(this);
        btn_search.setOnClickListener(this);
      refreshAd();
    }


    //method initialize views------------------------------------------------------------
    private void initializeViews() {
        btn_dpt = findViewById(R.id.btn_dpt);
        btn_latest = findViewById(R.id.btn_latest);
        btn_int = findViewById(R.id.btn_int);
        btn_qulf = findViewById(R.id.btn_qulf);
        btn_pdate = findViewById(R.id.btn_pdate);
        btn_prof = findViewById(R.id.btn_prof);
        btn_search = findViewById(R.id.btn_search);
    }


    //method onClick----------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_dpt) {
            toCategoriesActivity("Job By Department", "department");

        } else if (id == R.id.btn_latest) {
            Intent i = new Intent(MainActivity.this, ImagesActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("category", "latest");
            bundle.putString("heading", "Latest Jobs");
            i.putExtras(bundle);
            startActivity(i);
        } else if (id == R.id.btn_int) {
            toCategoriesActivity("International Jobs", "type");
        } else if (id == R.id.btn_qulf) {
            toCategoriesActivity("Job By Qualification", "qualification");
        } else if (id == R.id.btn_pdate) {
            toCategoriesActivity("Job By Date Posted", "date");
        } else if (id == R.id.btn_prof) {
            toCategoriesActivity("Job By Profession", "profession");
        } else if (id == R.id.btn_search) {
            Intent i = new Intent(MainActivity.this, ImagesActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("category", "Search");
            i.putExtras(bundle);
            startActivity(i);
        }
    }

    private void toCategoriesActivity(String heading, String category) {
        Intent intent = new Intent(MainActivity.this, Categories_Activity.class);
        intent.putExtra("heading", heading);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void refreshAd() {
        //  refresh.setEnabled(false);

        AdLoader.Builder builder = new AdLoader.Builder(this, ADMOB_AD_UNIT_ID);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // If this callback occurs after the activity is destroyed, you must call
                // destroy and return or you may get a memory leak.
                if (isDestroyed()) {
                    unifiedNativeAd.destroy();
                    return;
                }
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                FrameLayout frameLayout =
                        findViewById(R.id.fl_adplaceholder);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                        .inflate(R.layout.ad_unified, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                //    refresh.setEnabled(true);
//                Toast.makeText(MainActivity.this, "Failed to load native ad: "
//                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

        //videoStatus.setText("");
    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
//            videoStatus.setText(String.format(Locale.getDefault(),
//                    "Video status: Ad contains a %.2f:1 video asset.",
//                    vc.getAspectRatio()));

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
//                    refresh.setEnabled(true);
//                    videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
//            videoStatus.setText("Video status: Ad does not contain a video asset.");
//            refresh.setEnabled(true);
        }
    }
}