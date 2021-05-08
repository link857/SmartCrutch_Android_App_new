package deltazero.smartcrutch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.Optional;

import deltazero.smartcrutch.R;
import deltazero.smartcrutch.core.API;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Init UI
        getSupportActionBar().setTitle(getString(R.string.settings_activity_title));
    }

    public void setEmergencyTel(View view) {
    }

    public void setHomeLoc(View view) {
    }

    public void setPassword(View view) {
    }

    public void loadSettings(int i, String null_uuid, API.Settings settings) {
    }
}