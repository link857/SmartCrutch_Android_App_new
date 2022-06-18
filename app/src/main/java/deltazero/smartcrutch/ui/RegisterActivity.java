package deltazero.smartcrutch.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import deltazero.smartcrutch.R;
//import deltazero.smartcrutch.core.API;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout tiUuid, tiUsername, tiPassword;
    private EditText etUuid, etUsername, etPassword;
    private MaterialButton btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tiUuid = findViewById(R.id.register_uuid_text_input);
        etUuid = findViewById(R.id.register_uuid_edit_text);
        tiUsername = findViewById(R.id.register_username_text_input);
        etUsername = findViewById(R.id.register_username_edit_text);
        tiPassword = findViewById(R.id.register_password_text_input);
        etPassword = findViewById(R.id.register_password_edit_text);
        btRegister = findViewById(R.id.register_button);
        TextView tvAppInfo = findViewById(R.id.register_app_info);


        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // Make compiler happy
        }
        tvAppInfo.setText(String.format(getString(R.string.app_short_info), info.versionName));

    }

    public void handleCancelButtonClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void handleRegisterButtonClick(View view) {
        Intent intent = new Intent(this, EasterEggActivity.class);
        startActivity(intent);
    }

}
