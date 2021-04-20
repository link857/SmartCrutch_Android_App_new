package deltazero.smartcrutch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import deltazero.smartcrutch.R;
import deltazero.smartcrutch.core.API;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextInputLayout tiUsername, tiPassword;
    private EditText etUsername, etPassword;
    private MaterialButton btLogin;
    private final API api = new API();

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

    public void loginCallback(int status, String msg) {
        switch (status) {
            case 0: // SUCCESS
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
            default: // UNKNOWN_ERROR
                Toast.makeText(this,
                        msg,
                        Toast.LENGTH_LONG).show();
                break;
            case -2: // VALIDATION_ERROR
                Toast.makeText(this,
                        getString(R.string.error_validation).concat(msg),
                        Toast.LENGTH_LONG).show();
                break;
        }
        btLogin.setEnabled(true);
    }


}