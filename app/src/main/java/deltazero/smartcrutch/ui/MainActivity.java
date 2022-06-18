package deltazero.smartcrutch.ui;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Timer;

import deltazero.smartcrutch.R;
import deltazero.smartcrutch.core.utils;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "Emergency notification";
    final String LOGTAG = "MainActivity";
    Timer timer = new Timer();
    private String uuid;
    private TextView tvUserInfo, tvStatus, tvStatusInfo;
    private MaterialButton btViewMap;
    private MaterialCardView cvStatus;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mPrefEditor;
    private String appVersionName;

    private int activate, notificationsend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get uuid & init api

        mPrefs = getSharedPreferences("deltazero.smartcrutch.prefs", MODE_PRIVATE);
        mPrefEditor = mPrefs.edit();

        uuid = mPrefs.getString("uuid", null);

        if (uuid == null) {

            // Launch login activity
            Log.i(LOGTAG, "No uuid cache found, start login activity");

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            uuid = mPrefs.getString("uuid", null);

        } else {
            Log.i(LOGTAG, String.format("uuid cache found, skip login: uuid=%s", uuid));
        }

        // Get app version

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // Make compiler happy
        }
        appVersionName = info.versionName;


        // Init UI

        cvStatus = findViewById(R.id.main_card_status);
        tvStatus = findViewById(R.id.main_status_text_view);
        tvStatusInfo = findViewById(R.id.main_status_info_text_view);
        tvUserInfo = findViewById(R.id.main_user_info);
        btViewMap = findViewById(R.id.main_view_map_button);

        tvUserInfo.setText(String.format(getString(R.string.user_info_text_view), uuid));
//        tvStatus.setText(getString(R.string.status_loading));
//        tvStatusInfo.setText(getString(R.string.status_info_loading));
//        cvStatus.setCardBackgroundColor(getColor(R.color.LightSlateGray));

    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uuid = mPrefs.getString("uuid", null);
        tvUserInfo.setText(String.format(getString(R.string.user_info_text_view), uuid));
        if (uuid != null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new utils.GetStatusTimerTask(this, uuid), 0, 1000);
        } else {
            Log.e(LOGTAG, "Got null uuid!");
        }
    }

    private String createNotificationChannel(String channelID, String channelNAME, int level) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelID, channelNAME, level);
            manager.createNotificationChannel(channel);
            return channelID;
        } else {
            return null;
        }
    }


    public void updateStatus(int code, String msg, String status) {
        switch (code) {
            case 0:
                switch (status) {
                    case "ok":
                        tvStatus.setText(getString(R.string.status_ok));
                        tvStatusInfo.setText(getString(R.string.status_info_ok));
                        cvStatus.setCardBackgroundColor(getColor(R.color.BlueViolet));
                        btViewMap.setEnabled(true);
                        break;

                    case "emergency":
                        tvStatus.setText(getString(R.string.status_emergency));
                        tvStatusInfo.setText(getString(R.string.status_info_emergency));
                        cvStatus.setCardBackgroundColor(getColor(R.color.OrangeRed));
                        btViewMap.setEnabled(true);

                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                        String channelId = createNotificationChannel("my_channel_ID", "my_channel_NAME", NotificationManager.IMPORTANCE_MAX);

                        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId)
                                .setContentTitle(getString(R.string.status_emergency))
                                .setContentText(getString(R.string.status_info_emergency))
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(R.drawable.ic_alarm)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        notificationManager.notify(100, notification.build());


                        break;

                    case "offline":
                        tvStatus.setText(getString(R.string.status_offline));
                        tvStatusInfo.setText(getString(R.string.status_info_offline));
                        cvStatus.setCardBackgroundColor(getColor(R.color.LightSlateGray));
                        btViewMap.setEnabled(false);
                        break;
                }
                break;

            case 1:
                // invalid uuid
                Log.w(LOGTAG, msg);
                break;

            case -1:
                tvStatus.setText(getString(R.string.status_network_err));
                tvStatusInfo.setText(msg);
                cvStatus.setCardBackgroundColor(getColor(R.color.LightSlateGray));
                btViewMap.setEnabled(false);
                break;

        }

    }

    public void launchMapView(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void launchSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void handle_logout(View view) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.confirm_logout_title))
                .setMessage(getString(R.string.confirm_logout))
                .setNegativeButton(getString(R.string.button_cancel), null)
                .setPositiveButton(getString(R.string.button_logout), (dialogInterface, i) -> logout())
                .show();
    }

    public void logout() {
        timer.cancel();

        Log.i(LOGTAG, "Logged out, start login activity");
        mPrefEditor.putString("uuid", null);
        mPrefEditor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void showAbout(View view) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.about_title))
                .setMessage(String.format(getString(R.string.about_content), appVersionName))
                .setPositiveButton(getString(R.string.button_cancel), null)
                .show();
    }

}