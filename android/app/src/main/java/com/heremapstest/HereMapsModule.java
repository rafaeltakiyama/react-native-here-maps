package com.heremapstest;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class HereMapsModule extends ReactContextBaseJavaModule {

    public HereMapsModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "HereMapsModule";
    }

    @ReactMethod
    void openHereMaps(double latitude, double longitude, double zoom, String title, String description) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, BasicMapActivity.class);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("zoom", zoom);
            intent.putExtra("title", title);
            intent.putExtra("description", description);
            activity.startActivity(intent);
        }
    }
}
