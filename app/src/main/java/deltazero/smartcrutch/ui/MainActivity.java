package deltazero.smartcrutch.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps2d.MapView;

import deltazero.smartcrutch.R;

public class MainActivity extends AppCompatActivity {

//    public static final String API_BUNDLE = "deltazero.smartcrutch.api";
//    public final API api = new API();


    MapView mMapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Launch login activity
        Intent intent = new Intent(this, LoginActivity.class);
//        intent.putExtra(API_BUNDLE, api);
        startActivity(intent);
    }

}