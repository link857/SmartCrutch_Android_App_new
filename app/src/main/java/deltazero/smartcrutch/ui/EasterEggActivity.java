package deltazero.smartcrutch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import deltazero.smartcrutch.R;

public class EasterEggActivity extends AppCompatActivity {

    VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter_agg);

        getSupportActionBar().setTitle(getString(R.string.easter_egg_activity_title));

        vv = (VideoView) findViewById(R.id.easter_egg_video_view);
        MediaController mc = new MediaController(this);
        vv.setMediaController(mc);
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.damedane));
        vv.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vv.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vv.resume();
    }
}
