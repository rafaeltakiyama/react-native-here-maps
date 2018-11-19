/*
 * Copyright (c) 2011-2018 HERE Global B.V. and its affiliate(s).
 * All rights reserved.
 * The use of this software is conditional upon having a separate agreement
 * with a HERE company for the use or utilization of this software. In the
 * absence of such agreement, the use of the software is not allowed.
 */
package com.heremapstest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import android.util.Log;
import android.view.Menu;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;

public class BasicMapActivity extends Activity {
    private static final String LOG_TAG = BasicMapActivity.class.getSimpleName();

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private Map map = null;
    private MapFragment mapFragment = null;

    private double mLatitude;
    private double mLongitude;
    private double mZoom;
    private String mTitle;
    private String mDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        getExtras(extras);
        checkPermissions();
    }

    private void getExtras(Bundle extras) {
        if (extras != null) {
            if (extras.containsKey("latitude")) {
                mLatitude = extras.getDouble("latitude", 0);
            }
            if (extras.containsKey("longitude")) {
                mLongitude = extras.getDouble("longitude", 0);
            }
            if (extras.containsKey("zoom")) {
                mZoom = extras.getDouble("zoom", 0);
            }
            if (extras.containsKey("title")) {
                mTitle = extras.getString("title", null);
            }
            if (extras.containsKey("description")) {
                mDescription = extras.getString("description", null);
            }
        }
    }

    private MapFragment getMapFragment() {
        return (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
    }

    private void initialize() {
        setContentView(R.layout.activity_main);
        mapFragment = getMapFragment();
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {

                    map = mapFragment.getMap();

                    Image mapMarkerImg = new Image();

                    try {
                        mapMarkerImg.setImageResource(R.drawable.map_marker);
                    } catch (IOException e) {
                        finish();
                    }

                    GeoCoordinate geoCoordinate = new GeoCoordinate(mLatitude, mLongitude);

                    MapMarker mapMarker = new MapMarker(geoCoordinate, mapMarkerImg);

                    if (mTitle != null) {
                        mapMarker.setTitle(mTitle);
                    }

                    if (mDescription != null) {
                        mapMarker.setDescription(mDescription);
                    }

                    map.setCenter(geoCoordinate, Map.Animation.NONE);
                    map.setZoomLevel(mZoom);
                    map.addMapObject(mapMarker);

                    if (mDescription != null || mTitle != null) {
                        mapMarker.showInfoBubble();
                    }

                } else {
                    Log.e(LOG_TAG, "Cannot initialize MapFragment (" + error + ")");
                }
            }
        });
    }

    /**
     * Checks the dynamically controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                initialize();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}