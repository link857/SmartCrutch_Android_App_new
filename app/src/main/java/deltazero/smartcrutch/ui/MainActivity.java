package deltazero.smartcrutch.ui;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import java.util.TimerTask;

import deltazero.smartcrutch.R;
import deltazero.smartcrutch.core.utils;

public class MainActivity extends AppCompatActivity {

    final String LOGTAG = "MainActivity";

    Timer timer;
    TimerTask timertask;

    private String uuid;
    private TextView tvUserInfo, tvStatus, tvStatusInfo;
    private MaterialButton btViewMap;
    private MaterialCardView cvStatus;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mPrefEditor;
    private String appVersionName;

    private int notificationCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w("Create", "create the activity");

        // 锁屏显示
        setShowWhenLocked(true);


        // Get uuid & init api
        mPrefs = getSharedPreferences("deltazero.smartcrutch.prefs", MODE_PRIVATE);
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


        timer = new Timer();
        timertask = new utils.GetStatusTimerTask(this, uuid);
        timer.scheduleAtFixedRate(timertask, 0, 1000);


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

        // 检查通知权限是否打开
        NotificationManagerCompat notification_settings = NotificationManagerCompat.from(this);
        boolean isEnabled = notification_settings.areNotificationsEnabled();

        if (!isEnabled) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请在“通知”中打开通知与锁屏通知权限！")
                    .setNegativeButton("取消", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("去设置", (dialog, which) -> {
                        dialog.cancel();
                        Intent intent = new Intent();

                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                        intent.putExtra("android.provider.extra.APP_PACKAGE", MainActivity.this.getPackageName());

//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
//                                intent.putExtra("android.provider.extra.APP_PACKAGE", MainActivity.this.getPackageName());
//                            }
//                            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
//                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
//                                intent.putExtra("app_package", MainActivity.this.getPackageName());
//                                intent.putExtra("app_uid", MainActivity.this.getApplicationInfo().uid);
//                                startActivity(intent);
//                            }
//                            else if (Build.VERSION.SDK_INT >= 15) {
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                                intent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), null));
//                            }

                        startActivity(intent);

                    })
                    .create();

            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        }

    }



    @Override
    public void onPause() {
        super.onPause();

        Log.w("pause", "pause");

//        if (timer == null) {
//            Log.w("pause_timer", "open here");
//            timer = new Timer();
//            timertask = new utils.GetStatusTimerTask(this, uuid);
//            timer.scheduleAtFixedRate(timertask, 0, 1000);
//        }

//        timer.cancel();
//        timer = null;
//        timertask.cancel();
//        timertask = null;
//
////         Android 12
//        uuid = mPrefs.getString("uuid", null);
//        tvUserInfo.setText(String.format(getString(R.string.user_info_text_view), uuid));
//        if (uuid != null) {
//            timer = new Timer();
//            timertask = new utils.GetStatusTimerTask(this, uuid);
//            timer.scheduleAtFixedRate(timertask, 0, 1000);
//        } else {
//            Log.e(LOGTAG, "Got null uuid!");
//        }
    }



    @Override
    public void onBackPressed(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }



    @Override
    protected void onResume() {
        super.onResume();

        Log.w("resume", "reopen the activity");

//        timer.cancel();
//        timer = null;
//        timertask = null;

        uuid = mPrefs.getString("uuid", null);
        tvUserInfo.setText(String.format(getString(R.string.user_info_text_view), uuid));

//        if (uuid != null) {
////            timer.cancel();
//            timer = new Timer();
//            timertask = new utils.GetStatusTimerTask(this, uuid);
//            timer.scheduleAtFixedRate(timertask, 0, 1000);
//        } else {
//            Log.e(LOGTAG, "Got null uuid!");
//        }

//        if (timer == null & timertask == null) {
//            Log.w("resume_timer", "open here");
//
//            timer = new Timer();
//            timertask = new utils.GetStatusTimerTask(this, uuid);
//            timer.scheduleAtFixedRate(timertask, 0, 1000);
//        }

        if (uuid == null) {
            Log.e(LOGTAG, "Got null uuid!");
        }

    }

    private String createNotificationChannel(String channelID, String channelNAME, int level) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelID, channelNAME, level);
        channel.enableLights(true);
        channel.enableVibration(true);
        manager.createNotificationChannel(channel);
        return channelID;
        }



    public void updateStatus(int code, String msg, String status) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String channelId = createNotificationChannel("emergency", "Emergency Notification", NotificationManager.IMPORTANCE_MAX);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.status_emergency))
                .setContentText(getString(R.string.status_info_emergency))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_alarm)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        switch (code) {
            case 0:
                switch (status) {
                    case "emergency":
                        tvStatus.setText(getString(R.string.status_emergency));
                        tvStatusInfo.setText(getString(R.string.status_info_emergency));
                        cvStatus.setCardBackgroundColor(getColor(R.color.OrangeRed));
                        btViewMap.setEnabled(true);

                        if (notificationCount == 0) {
                            notificationManager.notify(821, notification.build());

                            notificationCount = 1;
                        }

                        break;

                    case "ok":
                        tvStatus.setText(getString(R.string.status_ok));
                        tvStatusInfo.setText(getString(R.string.status_info_ok));
                        cvStatus.setCardBackgroundColor(getColor(R.color.BlueViolet));
                        btViewMap.setEnabled(true);

                        notificationCount = 0;
                        notificationManager.cancelAll();

                        break;

                    case "offline":
                        tvStatus.setText(getString(R.string.status_offline));
                        tvStatusInfo.setText(getString(R.string.status_info_offline));
                        cvStatus.setCardBackgroundColor(getColor(R.color.LightSlateGray));
                        btViewMap.setEnabled(false);

                        notificationCount = 0;
                        notificationManager.cancelAll();

                        break;
                }
                break;

            case 1:
                // invalid uuid
                Log.w(LOGTAG, msg);

                notificationCount = 0;
                notificationManager.cancelAll();
                break;

            case -1:
                tvStatus.setText(getString(R.string.status_network_err));
                tvStatusInfo.setText(msg);
                cvStatus.setCardBackgroundColor(getColor(R.color.LightSlateGray));
                btViewMap.setEnabled(false);

                notificationCount = 0;
                notificationManager.cancelAll();
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

    public void launchMonitor(View view) {
        Intent intent = new Intent(this, MonitorActivity.class);
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
        timer = null;
        timertask.cancel();
        timertask = null;

        Log.i(LOGTAG, "Logged out, start login activity");
        mPrefEditor = mPrefs.edit();
        mPrefEditor.putString("uuid", null);
        mPrefEditor.apply();

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