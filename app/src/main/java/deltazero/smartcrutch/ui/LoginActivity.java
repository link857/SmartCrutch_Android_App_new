package deltazero.smartcrutch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import deltazero.smartcrutch.R;

import deltazero.smartcrutch.core.AccountManager;
import deltazero.smartcrutch.core.AccountManager.LoginResult;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private final AccountManager accountManager = new AccountManager();
    private TextInputLayout tiUsername, tiPassword;
    private EditText etUsername, etPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tiUsername = findViewById(R.id.login_username_text_input);
        etUsername = findViewById(R.id.login_username_edit_text);
        tiPassword = findViewById(R.id.login_password_text_input);
        etPassword = findViewById(R.id.login_password_edit_text);
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

        this.accountManager.login(username, password, this);

    }

    public void loginCallback(AccountManager.LoginStatus status, String msg) {
        switch (status) {
            case SUCCESS:
                break;
            case USER_NOT_EXIST:
                tiUsername.setError(getString(R.string.error_user_not_exist));
                break;
            case PASSWORD_ERROR:
                tiPassword.setError(getString(R.string.error_password_incorrect));
                break;
            case NETWORK_ERROR:
                Toast.makeText(this,
                        getString(R.string.error_network).concat(msg),
                        Toast.LENGTH_LONG).show();
                break;
            case UNKNOWN_ERROR:
                Toast.makeText(this,
                        login_result.msg,
                        Toast.LENGTH_LONG).show();
                break;
            case VALIDATION_ERROR:
                Toast.makeText(this,
                        getString(R.string.error_validation).concat(msg),
                        Toast.LENGTH_LONG).show();
                break;
        }
    }


}