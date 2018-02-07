package fayaj.maps.app.maps.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapState;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fayaj.maps.app.maps.R;
import fayaj.maps.app.maps.search.SearchLocationActivity;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Map.OnTransformListener, PositioningManager.OnPositionChangedListener  {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    // permissions request code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    // map embedded in the map fragment
    private Map map = null;

    // map fragment embedded in this activity
    private MapFragment mapFragment = null;

    private PositioningManager posManager;
    private boolean paused;


    @BindView(R.id.et_search_location)
    EditText searchText;

    @BindView(R.id.current_location)
    FloatingActionButton currentLocationFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkPermissions();
    }

    @OnClick(R.id.et_search_location)
    public void onSearchClicked() {
        Intent intent = new Intent(HomeActivity.this, SearchLocationActivity.class);
        intent.putExtra("current_lat",posManager.getPosition().getCoordinate().getLatitude());
        intent.putExtra("current_long",posManager.getPosition().getCoordinate().getLongitude());
        startActivity(intent);
    }

    @OnClick(R.id.current_location)
    public void getCurrentLocationClicked() {
//        getCurrentLocation();
        if(posManager != null && posManager.getPosition() != null && posManager.getPosition().getCoordinate() != null) {
            map.setCenter(posManager.getPosition().getCoordinate(), Map.Animation.BOW);
        }
    }

    private void getCurrentLocation() {
        if(posManager != null) {
            posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
        }
    }

    private void initialize() {
        // Search for the map fragment to finish setup by calling init().
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    map = mapFragment.getMap();
                    // Set the map center to the Vancouver region (no animation)

                    // Set the zoom level to the average between min and max
                    map.addTransformListener(HomeActivity.this);
                    posManager = PositioningManager.getInstance();

                    posManager.addListener(new WeakReference<PositioningManager.OnPositionChangedListener>(
                            HomeActivity.this));
                    posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);

                    GeoPosition position = posManager.getPosition();
                    if(position != null) {
                        GeoCoordinate cor = position.getCoordinate();
                        map.setCenter(posManager.getPosition().getCoordinate(), Map.Animation.LINEAR);
//                        map.setCenter(new GeoCoordinate(12.917143,80.192349), Map.Animation.NONE);
                        map.setZoomLevel(14);

                        // Create a custom marker image
                        com.here.android.mpa.common.Image myImage =
                                new com.here.android.mpa.common.Image();

                        try {
                            myImage.setImageResource(R.drawable.marker);
                        } catch (IOException e) {
                            finish();
                        }

// Create the MapMarker
                        MapMarker myMapMarker =
                                new MapMarker(new GeoCoordinate(cor.getLatitude(),cor.getLongitude()), myImage);

                        map.addMapObject(myMapMarker);

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

    // Resume positioning listener on wake up
    public void onResume() {
        super.onResume();
        if (posManager != null) {
            posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
        }
    }

    // To pause positioning listener
    public void onPause() {
        if (posManager != null) {
//            posManager.stop();
        }
        super.onPause();
        paused = true;
    }

    // To remove the positioning listener
    public void onDestroy() {
        if (posManager != null) {
            // Cleanup
            posManager.removeListener(
                    HomeActivity.this);
        }
        map = null;
        super.onDestroy();
    }

    @Override
    public void onMapTransformStart() {

    }

    @Override
    public void onMapTransformEnd(MapState mapState) {

    }

    @Override
    public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
        map.setCenter(geoPosition.getCoordinate(),
                Map.Animation.NONE);
        map.getPositionIndicator().setVisible(true);
    }

    @Override
    public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {
        //ignored
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
