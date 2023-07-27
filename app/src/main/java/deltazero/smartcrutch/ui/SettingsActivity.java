package deltazero.smartcrutch.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import deltazero.smartcrutch.R;
import deltazero.smartcrutch.core.API;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvLanguageInfo;
    private EditText etPhoneInfo, etHomeInfo, etPasswordInfo;

    private int languageChoiceCount = 0;
    private API.Settings settings;

    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Init UI
        getSupportActionBar().setTitle(getString(R.string.settings_activity_title));
        etPhoneInfo = findViewById(R.id.settings_tv_phone_info);
        etHomeInfo = findViewById(R.id.settings_tv_home_info);
        etPasswordInfo = findViewById(R.id.settings_tv_password_info);
        tvLanguageInfo = findViewById(R.id.settings_tv_language_info);



        Log.d("settings", "Language: " + Locale.getDefault().getLanguage());

        // 读取之前保存的结果
        switch (Locale.getDefault().getLanguage()) {
            case "zh":
                tvLanguageInfo.setText(getString(R.string.language_zh));
                break;
            default:
                tvLanguageInfo.setText(getString(R.string.language_en));
        }

        // Get settings
        String uuid = getSharedPreferences("deltazero.smartcrutch.prefs", MODE_PRIVATE)
                        .getString("uuid", null);
        API.getSettings(this, uuid);
    }

    public void setEmergencyTel(View view) {
        languageChoiceCount ++;
        if (languageChoiceCount > 20) {
            startActivity(new Intent(this, EasterEggActivity.class));
        }
    }

    public void setHomeLoc(View view) {
        languageChoiceCount ++;
        if (languageChoiceCount > 20) {
            startActivity(new Intent(this, EasterEggActivity.class));
        }
    }

    public void setPassword(View view) {
        languageChoiceCount ++;
        if (languageChoiceCount > 20) {
            startActivity(new Intent(this, EasterEggActivity.class));
        }
    }

    public void setLanguage(View view) {
        // 保存语言后得重启应用，且修改语言得所有地方都修改

        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        languageChoiceCount ++;
        if (languageChoiceCount % 2 == 0) {
            tvLanguageInfo.setText(getString(R.string.language_zh));
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (languageChoiceCount % 2 == 1) {
            tvLanguageInfo.setText(getString(R.string.language_en));
            locale = Locale.ENGLISH;
        }

        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, displayMetrics);

        if (languageChoiceCount > 20) {
            startActivity(new Intent(this, EasterEggActivity.class));
            languageChoiceCount = 0;
        }
    }

    public void loadSettings(int code, String msg, API.Settings settings) {
        switch (code) {
            case 0:
                this.settings = settings;

                if (settings.phone != null)
                    etPhoneInfo.setText(settings.phone);

                if (settings.home != null)
                    etHomeInfo.setText(settings.home);

                if (settings.password != null)
                    etPasswordInfo.setText(settings.password);

                break;

            case 1:
                Toast.makeText(this, getString(R.string.error_login_expired), Toast.LENGTH_SHORT).show();
            case -1:
                Toast.makeText(this, getString(R.string.error_network) + msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void handleUpdateButtonClick(View view) {
        String phone = etPhoneInfo.getText().toString();
        String password = etPasswordInfo.getText().toString();
        String home = etHomeInfo.getText().toString();
        String uuid = getSharedPreferences("deltazero.smartcrutch.prefs", MODE_PRIVATE)
                .getString("uuid", null);

        API.update_settings(uuid, home, phone, password, this);

    }


    public void UpdatesettingsCallback(int code, String msg) {
        switch (code) {
            case 0: // SUCCESS
//                mPrefEditor.putString("uuid", uuid);
//                mPrefEditor.commit();
                Log.e("update_settings", "Update_settings successfully!");
                Toast.makeText(this,
                        getString(R.string.update_settings_success),
                        Toast.LENGTH_LONG).show();
                finish();
                break;
            case 1: // INVALID_UUID
                Toast.makeText(this,
                        getString(R.string.error_invalid_uuid),
                        Toast.LENGTH_LONG).show();
                break;
            case 2: // PASSWORD_EMPTY
                etPasswordInfo.setError(getString(R.string.error_password_empty));
                break;
            case -1: // NETWORK_ERROR
                Toast.makeText(this,
                        getString(R.string.error_network).concat(msg),
                        Toast.LENGTH_LONG).show();
                break;
            case -2: // VALIDATION_ERROR
                Toast.makeText(this,
                        getString(R.string.error_validation).concat(msg),
                        Toast.LENGTH_LONG).show();
                break;
            default: // UNKNOWN_ERROR
                Toast.makeText(this,
                        msg,
                        Toast.LENGTH_LONG).show();
                break;
        }
    }
}