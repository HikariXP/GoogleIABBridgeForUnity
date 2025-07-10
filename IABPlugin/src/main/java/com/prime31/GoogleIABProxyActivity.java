package com.prime31;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

/* loaded from: IABPlugin.aar:classes.jar:com/prime31/GoogleIABProxyActivity.class */
public class GoogleIABProxyActivity extends Activity {
    private static final String TAG = "Prime31-Proxy";
    private Boolean _created = false;
    private Boolean _didCompletePurcaseFlow = false;
    private static final int RC_REQUEST = 10001;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        if (this._created.booleanValue()) {
            Log.i("Prime31", "activity created twice. stopping one instance");
            return;
        }
        this._created = true;
        try {
            String sku = getIntent().getExtras().getString("sku");
            getIntent().getExtras().getStringArrayList("oldSkus");
            getIntent().getExtras().getString("itemType");
            getIntent().getExtras().getString("developerPayload");
            Log.i(TAG, "proxy received action. sku: " + sku);
            if (getIntent().getExtras().containsKey("extrasBundle")) {
                Log.i(TAG, "Found extrasBundle. Using getBuyIntentExtraParams and billing version 6");
                getIntent().getExtras().getBundle("extrasBundle");
            }
            if (0 == 0) {
                finish();
            }
        } catch (Exception var8) {
            Log.i(TAG, "unhandled exception while attempting to purchase item: " + var8.getMessage());
            Log.i(TAG, "going to end the async operation with null data to clear out the queue");
            this._didCompletePurcaseFlow = true;
            finish();
        }
    }

    @Override // android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this._didCompletePurcaseFlow = true;
        IABConstants.logEntering(getClass().getSimpleName(), "onActivityResult", new Object[]{Integer.valueOf(requestCode), Integer.valueOf(resultCode), data});
        finish();
    }

    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
        if (!this._didCompletePurcaseFlow.booleanValue()) {
            Log.d(TAG, "in onStop but we didnt complete the purchase flow. Canceling it now.");
        }
        Log.d(TAG, "GoogleIABProxyActivity onStop");
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "GoogleIABProxyActivity onDestroy");
    }
}
