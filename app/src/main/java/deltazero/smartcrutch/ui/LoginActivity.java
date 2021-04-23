package deltazero.smartcrutch.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import deltazero.smartcrutch.R;
import deltazero.smartcrutch.core.API;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tiUsername, tiPassword;
    private EditText etUsername, etPassword;
    private MaterialButton btLogin;

//    private boolean doubleBackToExitPressedOnce;

    public String uuid = null;
    private final API api = new API();

//    private final API api = (API) getIntent().getSerializableExtra(MainActivity.API_BUNDLE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tiUsername = findViewById(R.id.login_username_text_input);
        etUsername = findViewById(R.id.login_username_edit_text);
        tiPassword = findViewById(R.id.login_password_text_input);
        etPassword = findViewById(R.id.login_password_edit_text);

        btLogin = findViewById(R.id.login_button);
    }

    @Override
    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
////            super.onBackPressed();
//            android.os.Process.killProcess(android.os.Process.myPid());
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, getString(R.string.back_again_to_exit), Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
        Toast.makeText(this, getString(R.string.toast_require_login), Toast.LENGTH_SHORT).show();
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

        this.api.login(username, password, this);

        btLogin.setEnabled(false);

    }

    public void loginCallback(int status, String msg, String uuid) {
        switch (status) {
            case 0: // SUCCESS
                this.uuid = uuid;
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


}