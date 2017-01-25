package activities;

import android.app.Activity;
import android.os.Bundle;

import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.the.bestna.cleaner.R;


import Utilities.IDs;


////////////////////// Runs when device complete Boot

public class AdLauncherActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        StartAppSDK.init(this, IDs.StartApp, true);
        setContentView(R.layout.activity_ad_launcher);
    }catch (Exception e){e.printStackTrace();}
        this.finish();
    }


}
