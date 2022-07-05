package deltazero.smartcrutch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import deltazero.smartcrutch.R;


public class MonitorActivity extends AppCompatActivity {

    private VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        vv = findViewById(R.id.monitor_video_view);

        MediaController mc = new MediaController(this);
        vv.setMediaController(mc);
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1));
        vv.start();
    }

    public void launchVideo1(View view) {
        MediaController mc = new MediaController(this);
        vv.setMediaController(mc);
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1));
        vv.start();
    }

    public void setFrontView(View view) {
        MediaController mc = new MediaController(this);
        vv.setMediaController(mc);
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video2));
        vv.start();
    }

    public void setBackView(View view) {
        MediaController mc = new MediaController(this);
        vv.setMediaController(mc);
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video3));
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
