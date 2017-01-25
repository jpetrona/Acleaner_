package ads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdmobInterstitial extends AdListener {
	private static String adUnitId = "ADS_INTERSTITIAL_ID";
	public InterstitialAd interstitial;
	private PreferencesHelper prefHelper;
	private Context activity;
	
	public AdmobInterstitial(Context activity,String adUnitId)
	{
		this.adUnitId = adUnitId;
		this.activity = activity;
		this.prefHelper = new PreferencesHelper(activity);
	}
	public void showOnStart(){
			this.showNow();
	}
	
	public void showNow(){
		AdRequest adRequest = new AdRequest.Builder().build();
		this.interstitial = new InterstitialAd(activity);
		this.interstitial.setAdUnitId(adUnitId);
		this.interstitial.loadAd(adRequest);
		this.interstitial.setAdListener(this);
	}
	
	public void loadNewAd(){
		AdRequest adRequest = new AdRequest.Builder().build();
		this.interstitial = new InterstitialAd(activity);
		this.interstitial.setAdUnitId(adUnitId);
		this.interstitial.loadAd(adRequest);		
	}
	
	public void showLoaded(){
		if(this.interstitial !=null && this.interstitial.isLoaded()){
			this.interstitial.show();
		}		
	}
	
	public void showLoaded(boolean loadNext){
		if(this.interstitial !=null && this.interstitial.isLoaded()){
			this.interstitial.show();
			if(loadNext){
				this.loadNewAd();
			}
		}
	}

	@Override
	public void onAdFailedToLoad(int errorCode) {
		super.onAdFailedToLoad(errorCode);
	}

	@Override
	public void onAdLoaded() {
		super.onAdLoaded();
	}

}
