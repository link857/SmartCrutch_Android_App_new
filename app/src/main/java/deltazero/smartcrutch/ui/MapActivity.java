package deltazero.smartcrutch.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;

import java.util.Timer;

import deltazero.smartcrutch.R;
import deltazero.smartcrutch.core.API;


public class MapActivity extends AppCompatActivity {

    public API api;
    public Timer timer;
    public String uuid;
    private MapView mMapView = null;
    private AMap mAMap;
    private TextView tvLocDescription, tvLoc;
    public LatLng pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Init map
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);

        // Init UI
        tvLocDescription = findViewById(R.id.map_loc_description);
        tvLoc = findViewById(R.id.map_loc);
        getSupportActionBar().setTitle(getString(R.string.map_activity_title));

        // Get uuid
        uuid = getSharedPreferences("deltazero.smartcrutch.prefs", MODE_PRIVATE)
                .getString("uuid", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
//        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        API.getLoc(this, uuid);
//        timer = new Timer();
//        timer.scheduleAtFixedRate(new utils.GetLocTimerTask(this, uuid), 0, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
//        timer.cancel();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    public void updateLoc(int code, String msg, float latitude, float longitude) {
        switch (code) {
            case 0:
                // TODO: Update location info in real-time
                tvLoc.setText(String.format(getString(R.string.location_latlng), latitude, longitude));
                tvLocDescription.setText(getString(R.string.unknown_location));

                pos = new LatLng(latitude, longitude);
                mAMap.addMarker(new MarkerOptions().position(pos).title(getString(R.string.crutch_marker_tag)));
                mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12));
                break;

            case 1:
                // invalid uuid
                // TODO: Force re-login
                Log.w("MapActivity", msg);
                break;
            case -1:
                // no loc info
                tvLocDescription.setText(getString(R.string.no_location_info));
                tvLoc.setText(getString(R.string.no_location));
                break;
            case -2:
                // Network err
                tvLocDescription.setText(getString(R.string.no_location_info));
                tvLoc.setText(getString(R.string.no_location));
                break;
        }
    }

    public void moveCamera(View view) {
        mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12));
    }
}