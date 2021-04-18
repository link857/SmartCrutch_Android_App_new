package deltazero.smartcrutch.core;

import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


class LoginJsonResp {
    public int code;
    public String msg;
}

public class AccountManager {

    public String serverUrl = "http://192.168.3.18:8000/";

    private final OkHttpClient client = new OkHttpClient();
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<LoginJsonResp> loginJsonAdapter = moshi.adapter(LoginJsonResp.class);

    private final static String TAG = "AccountManager";

    public enum LoginStatus {
        SUCCESS,
        USER_NOT_EXIST,
        PASSWORD_ERROR,
        VALIDATION_ERROR,
        UNKNOWN_ERROR,
        NETWORK_ERROR
    }

    public static class LoginResult {

        public LoginStatus status;
        public String msg;

        public LoginResult (LoginStatus status, String msg) {
            this.status = status;
            this.msg = msg;
        }
    }

    public LoginResult login(String username, String password) {

        Log.d(TAG, "Called login func");

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(serverUrl.concat("app/login"))
                .post(formBody)
                .build();

        try {

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                Log.w(TAG, "Login failed: network error" + response);
                return new LoginResult(LoginStatus.NETWORK_ERROR, "Unexpected code " + response);
            }


            LoginJsonResp resp = this.loginJsonAdapter.fromJson(response.body().source());
            Log.d(TAG, "Login response: " + resp.msg);

            // LoginResp process
            return new LoginResult(LoginStatus.PASSWORD_ERROR, resp.msg);

        } catch (IOException e) {
            Log.w(TAG, "Login failed: network error" + e.toString());
            return new LoginResult(LoginStatus.NETWORK_ERROR, e.toString());
        }

    }

}