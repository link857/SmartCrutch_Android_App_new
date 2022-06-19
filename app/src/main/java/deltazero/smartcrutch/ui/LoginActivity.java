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

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mPrefEditor;

    private TextInputLayout tiUsername, tiPassword;
    private EditText etUsername, etPassword;
    private MaterialButton btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tiUsername = findViewById(R.id.login_username_text_input);
        etUsername = findViewById(R.id.login_username_edit_text);
        tiPassword = findViewById(R.id.login_password_text_input);
        etPassword = findViewById(R.id.login_password_edit_text);
        btLogin = findViewById(R.id.login_button);
        TextView tvAppInfo = findViewById(R.id.login_app_info);

        mPrefs = getSharedPreferences("deltazero.smartcrutch.prefs", MODE_PRIVATE);
        mPrefEditor = mPrefs.edit();

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

//    @Override
//    public void onBackPressed() {
//        Toast.makeText(this, getString(R.string.toast_require_login), Toast.LENGTH_SHORT).show();
////        super.onBackPressed();
//    }

    // 按返回键退回桌面
    @Override
    public void onBackPressed(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    public void handleLoginButtonClick(View view) {

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty()) {
            tiUsername.setError(getString(R.string.error_username_empty));
            tiPassword.setError(null);
            return;
        }

        if (password.isEmpty()) {
            tiPassword.setError(getString(R.string.error_password_empty));
            tiUsername.setError(null);
            return;
        }

        tiPassword.setError(null);
        tiUsername.setError(null);

        API.login(username, password, this);

        btLogin.setEnabled(false);

    }

    public void loginCallback(int status, String msg, String uuid) {
        switch (status) {
            case 0: // SUCCESS
                mPrefEditor.putString("uuid", uuid);
                mPrefEditor.commit();
                Log.e("Login", String.format("Login successfully: uuid=%s", uuid));
                Toast.makeText(this,
                        getString(R.string.login_success),
                        Toast.LENGTH_LONG).show();
                finish();
                break;
            case 1: // USER_NOT_EXIST
                tiUsername.setError(getString(R.string.error_user_not_exist));
                break;
            case 2: // PASSWORD_ERROR
                tiPassword.setError(getString(R.string.error_password_incorrect));
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
        btLogin.setEnabled(true);
    }

    public void handleRegisterButtonClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}