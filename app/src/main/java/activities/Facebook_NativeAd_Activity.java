package activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.bestna.cleaner.R;

import services.FacebookAdService;


public class Facebook_NativeAd_Activity extends Activity {
    ImageView nativeAdIcon;
    TextView nativeAdTitle;
    TextView nativeAdBody;
  //  MediaView nativeAdMedia;
    TextView nativeAdSocialContext;
    Button nativeAdCallToAction;
    LinearLayout nativeAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook__native_ad_);
        // Create native UI using the ad metadata.
        nativeAdContainer = (LinearLayout) findViewById(R.id.ad_unit);
        nativeAdIcon = (ImageView) findViewById(R.id.native_ad_icon);
        nativeAdTitle = (TextView) findViewById(R.id.native_ad_title);
        nativeAdBody = (TextView) findViewById(R.id.native_ad_body);
        //nativeAdMedia = (MediaView) findViewById(R.id.native_ad_media);
        nativeAdSocialContext = (TextView) findViewById(R.id.native_ad_social_context);
        nativeAdCallToAction = (Button) findViewById(R.id.native_ad_call_to_action);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //if (FacebookAdService.nativeAd != null) {
            // Setting the Text.
//            nativeAdSocialContext.setText(FacebookAdService.nativeAd.getAdSocialContext());
//            nativeAdCallToAction.setText(FacebookAdService.nativeAd.getAdCallToAction());
//            nativeAdTitle.setText(FacebookAdService.nativeAd.getAdTitle());
//            nativeAdBody.setText(FacebookAdService.nativeAd.getAdBody());

            // Downloading and setting the ad icon.
//            NativeAd.Image adIcon = FacebookAdService.nativeAd.getAdIcon();
  //          NativeAd.downloadAndDisplayImage(adIcon,nativeAdIcon );

            // Download and setting the cover image.
    //        NativeAd.Image adCoverImage = FacebookAdService.nativeAd.getAdCoverImage();
      //      nativeAdMedia.setNativeAd(FacebookAdService.nativeAd);
        //    FacebookAdService.nativeAd.registerViewForInteraction(nativeAdContainer);
        //}
        //else
          //  this.finish();
    }
}

