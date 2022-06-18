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
import deltazero.smartcrutch.core.API;
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
        finish();
    }


    public void handleRegisterButtonClick(View view) {

        String uuid = etUuid.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (uuid.isEmpty()) {
            tiUuid.setError(getString(R.string.error_uuid_empty));
            tiPassword.setError(null);
            tiUsername.setError(null);
            return;
        }

        if (username.isEmpty()) {
            tiUsername.setError(getString(R.string.error_username_empty));
            tiPassword.setError(null);
            tiUuid.setError(null);
            return;
        }

        if (password.isEmpty()) {
            tiPassword.setError(getString(R.string.error_password_empty));
            tiUsername.setError(null);
            tiUuid.setError(null);
            return;
        }

        tiUuid.setError(null);
        tiPassword.setError(null);
        tiUsername.setError(null);

        API.bind(uuid, username, password, this);

        btRegister.setEnabled(false);

    }

    public void BindCallback(int code, String msg) {
        switch (code) {
            case 0: // SUCCESS
                Log.e("Bind", "Bind successfully");
                Toast.makeText(this,
                        getString(R.string.register_success),
                        Toast.LENGTH_LONG).show();
                finish();
                break;
            case 1: // UUID_NOT_INITIALIZE
                tiUuid.setError(getString(R.string.error_uuid_not_initialize));
                break;
            case 2: // UUID_ONCE_BINDED
                tiUuid.setError(getString(R.string.error_uuid_once_binded));
                break;
            case 3: // PASSWORD_NULL
                tiPassword.setError(getString(R.string.error_password_empty));
                break;
            case 4: // USERNAME_NULL
                tiUsername.setError(getString(R.string.error_username_empty));
                break;
            case 5: // USERNAME_ONCE_USED
                tiUsername.setError(getString(R.string.error_username_once_used));
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
        btRegister.setEnabled(true);
    }

}
